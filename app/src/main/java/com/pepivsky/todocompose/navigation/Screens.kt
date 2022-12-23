package com.pepivsky.todocompose.navigation

import androidx.navigation.NavHostController
import com.pepivsky.todocompose.util.Action
import com.pepivsky.todocompose.util.Constants.LIST_SCREEN

class Screens(navHostController: NavHostController) {

    val list: (Action) -> Unit = { action ->
        navHostController.navigate(route = "list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }

    }

    val task: (Int) -> Unit = {taskId ->
        navHostController.navigate(route = "task/$taskId")
    }
}