package com.pepivsky.todocompose.ui.screens.task

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.pepivsky.todocompose.util.Action


@Composable
fun TaskScreen(navigateToListScreen: (Action) -> Unit) {
    Scaffold(
        topBar = {
            TaskAppBar(navigateToListScreen = navigateToListScreen)
        }, content = {

        }
    )

}
