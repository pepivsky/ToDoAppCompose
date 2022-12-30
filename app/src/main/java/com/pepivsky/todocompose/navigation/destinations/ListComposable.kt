package com.pepivsky.todocompose.navigation.destinations

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pepivsky.todocompose.ui.screens.list.ListScreen
import com.pepivsky.todocompose.ui.viewmodels.SharedViewModel
import com.pepivsky.todocompose.util.Constants.LIST_ARGUMENT_KEY
import com.pepivsky.todocompose.util.Constants.LIST_SCREEN
import com.pepivsky.todocompose.util.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) { navBackStackEntry ->
        // get string argument and convert to action
        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()
        Log.d("listComposable", action.name)

        // cuando action cambia su valor se actualiza el valor de la variable del viewModel
        LaunchedEffect(key1 = action, block = {
            sharedViewModel.action.value = action
        })

        // llamando a la funcion que dibuja la pantalla
        ListScreen(navigateToTaskScreen = navigateToTaskScreen, sharedViewModel =  sharedViewModel)
    }
}