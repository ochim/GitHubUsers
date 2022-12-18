package com.example.githubusers.ui.details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.githubusers.R
import com.example.githubusers.domain.User
import com.example.githubusers.ui.FetchNetworkModelState
import com.example.githubusers.ui.theme.GitHubUsersTheme
import java.text.SimpleDateFormat

@Composable
fun DetailsScreen(
    detailsViewModel: DetailsViewModel,
    popBackStack: () -> Unit,
    username: String
) {
    detailsViewModel.reset()
    detailsViewModel.userInfo(username)
    Scaffold(
        topBar = {
            DetailsTopBar(popBackStack)
        },
        content = {
            DetailsContent(it, detailsViewModel, username)
        }
    )
}

@Composable
fun DetailsTopBar(
    popBackStack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.details),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h6
            )
        },
        navigationIcon = {
            IconButton(onClick = { popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Icon",
                )
            }
        },
    )
}

@Composable
fun DetailsContent(
    paddingValues: PaddingValues,
    detailsViewModel: DetailsViewModel,
    username: String
) {
    val users = detailsViewModel.userLiveState.observeAsState()
    when (users.value) {
        is FetchNetworkModelState.NeverFetched -> {
            Text(username)
        }

        is FetchNetworkModelState.Fetching -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FetchNetworkModelState.RefreshedOK -> {
            val user = (users.value as? FetchNetworkModelState.RefreshedOK<User>)?.data
            if (user != null) {
                DetailsUser(user = user)
            } else {
                Text(username)
            }
        }

        is FetchNetworkModelState.FetchedError -> {
            Text(username)
            val exception = (users.value as FetchNetworkModelState.FetchedError).exception
            Toast.makeText(LocalContext.current, exception.message, Toast.LENGTH_LONG).show()
        }

        else -> {}
    }
}

@Composable
fun DetailsUser(user: User) {
    Column(
        modifier = Modifier.padding(all = 8.dp),
    ) {
        user.avatar_url?.let {
            Image(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .width(100.dp)
                    .height(100.dp),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = it)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            scale(Scale.FILL)
                        }).build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Text(style = MaterialTheme.typography.h6, text = user.login)
        Text(style = MaterialTheme.typography.h6, text = user.name ?: "")

        val styledText = buildAnnotatedString {
            // ここから先の処理でappendされたテキストにAnnotationを追加
            // pop()が呼ばれるまでが対象
            pushStringAnnotation(tag = "URL", annotation = user.html_url)

            withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append("GitHub")
            }

            pop()
        }

        val uriHandler = LocalUriHandler.current
        ClickableText(
            text = styledText,
            style = MaterialTheme.typography.body1,
            onClick = { pos ->
                // クリックされた箇所からAnnotationを取得
                val annotation = styledText.getStringAnnotations(start = pos, end = pos).firstOrNull()
                annotation?.let { range ->
                    // クリックされた箇所のURLを開く
                    // pushStringAnnotationで設定した情報が取得できる
                    uriHandler.openUri(range.item)
                }
            }
        )

        val textStyle = MaterialTheme.typography.subtitle1
        Text(style = textStyle, text = user.location ?: "")
        Text(style = textStyle, text = "email: ${user.email ?: ""}")
        Text(style = textStyle, text = user.bio ?: "")
        user.twitter_username?.let {
            Text(style = textStyle, text = "twitter: @${user.twitter_username}")
        }
        Text(style = textStyle, text = "${user.public_repos} public_repos")
        Text(style = textStyle, text = "${user.public_gists} public_gists")
        Text(style = textStyle, text = "${user.followers} followers")
        Text(style = textStyle, text = "${user.following} following")
        Text(style = textStyle, text = "created_at: ${convertFormattedString(user.created_at)}")
        Text(style = textStyle, text = "updated_at: ${convertFormattedString(user.updated_at)}")
    }
}

private fun convertFormattedString(string: String?): String {
    return try {
        val s = string ?: return ""
        val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = parseFormat.parse(s)
        val d = date ?: return ""
        SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(d)
    } catch (e: Exception) {
        e.message ?: "exception"
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 480)
@Preview("Dark Theme", widthDp = 360, heightDp = 480)
@Composable
fun DetailUserPreview() {
    val u = User(
        login = "yamada",
        id = 1,
        avatar_url = "https://avatars.githubusercontent.com/u/7196624?v=4",
        html_url = "https://github.com/ochim",
        name = "taro yamada",
        location = "Tokyo",
        email = "aaa@example.com",
        bio = "Mobile Application Engineer",
        twitter_username = "yamada_t",
        public_repos = 10,
        public_gists = 10,
        followers = 100,
        following = 100,
        created_at = "2014-04-06T15:06:58Z",
        updated_at = "2022-11-29T10:28:46Z",
    )
    GitHubUsersTheme {
        DetailsUser(user = u)
    }
}