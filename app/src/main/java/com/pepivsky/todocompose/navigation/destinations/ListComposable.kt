package com.pepivsky.todocompose.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pepivsky.todocompose.ui.screens.list.ListScreen
import com.pepivsky.todocompose.ui.viewmodels.SharedViewModel
import com.pepivsky.todocompose.util.Constants.LIST_ARGUMENT_KEY
import com.pepivsky.todocompose.util.Constants.LIST_SCREEN

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) {
        // llamando a la funcion que dibuja la pantalla
        ListScreen(navigateToTaskScreen = navigateToTaskScreen, sharedViewModel =  sharedViewModel)
    }
}