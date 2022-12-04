package com.example.githubusers.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    onNavigateToDetails: () -> Unit
) {
    Text("Home", modifier = Modifier.clickable { onNavigateToDetails() })
}