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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.pepivsky.todocompose.util.Action


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

        // poniendolo como estado evitamos el bug que sucede al recrear la activity
        var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

        // cuando myAction cambia su valor se actualiza el valor de la variable del viewModel
        LaunchedEffect(key1 = myAction, block = {
            if (action != myAction) {
                myAction = action
                sharedViewModel.action.value = action
            }

        })

        // observando la actiion
        val databaseAction by sharedViewModel.action

        // llamando a la funcion que dibuja la pantalla
        ListScreen(
            action = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel =  sharedViewModel
        )
    }
}