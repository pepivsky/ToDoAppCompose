package com.pepivsky.todocompose.navigation.destinations

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pepivsky.todocompose.ui.screens.task.TaskScreen
import com.pepivsky.todocompose.util.Action
import com.pepivsky.todocompose.util.Constants
import com.pepivsky.todocompose.util.Constants.TASK_ARGUMENT_KEY

fun NavGraphBuilder.taskComposable(navigateToListScreen: (Action) -> Unit) {
    composable(
        route = Constants.TASK_SCREEN,
        arguments = listOf(navArgument(Constants.TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        })
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)
        Log.d("taskComposable", taskId.toString())
        
        TaskScreen(navigateToListScreen = navigateToListScreen)
    }
}