package com.example.githubusers.ui.details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.githubusers.R

@Composable
fun DetailsScreen(
    popBackStack: () -> Unit,
    username: String
) {
    Scaffold(
        topBar = {
            DetailsTopBar(popBackStack)
        },
        content = {
            DetailsContent(paddingValues = it, username = username)
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
    username: String
) {
    Text(username)
}