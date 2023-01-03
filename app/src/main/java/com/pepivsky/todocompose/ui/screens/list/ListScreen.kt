package com.pepivsky.todocompose.ui.screens.list

import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.pepivsky.todocompose.R
import com.pepivsky.todocompose.ui.theme.fabBackgroundColor
import com.pepivsky.todocompose.ui.viewmodels.SharedViewModel
import com.pepivsky.todocompose.util.SearchAppBarState
import androidx.compose.runtime.*
import com.pepivsky.todocompose.util.Action
import kotlinx.coroutines.launch

@Composable
fun ListScreen(navigateToTaskScreen: (taskId: Int) -> Unit, sharedViewModel: SharedViewModel) {

    // se dispara solo cuando es la primera vez que se ejecuta esta funcion composable, no se ejecuta durante la recomposition
    LaunchedEffect(key1 = true, block = {
        Log.d("ListScreen", "LaunchEffect triggered!")
        sharedViewModel.getAllTasks()
    })
    // observe action from here
    val action by sharedViewModel.action

    // lista de objetos todoTask, se transforma a un estado con collectAsState para poder ser obsrvado por la ui
    val allTasks by sharedViewModel.allTasks.collectAsState() // usando el by se puede tratar como una lista normal y no como un estado

    // lista de objetos buscados desde el appBar
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState() // usando el by se puede tratar como una lista normal y no como un estado

    /*for (task in allTasks.value) {
        Log.d("ListScreen", "for loop" + task.title)
    }*/

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    // pass action to viewModel function
    // estado del scaffold
    val scaffoldState = rememberScaffoldState()

    // muestra el snackBar
    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action) },
        taskTitle = sharedViewModel.title.value,
        action = action,
        onUndoClicked = {
            sharedViewModel.action.value = it
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                searchAppBarState = searchAppBarState,
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFAB(onFabClicked = navigateToTaskScreen)
        })
}

@Composable
fun ListFAB(onFabClicked: (taskId: Int) -> Unit) {
    // taskId -1 para probar
    FloatingActionButton(
        onClick = { onFabClicked(-1) },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    action: Action,
    onUndoClicked: (Action) -> Unit
) {
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    // cuando la variable action cambia se dispara el lauch effect
    LaunchedEffect(key1 = action, block = {
        Log.d("DisplaySnackBar", "triggered ${action.name}")
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${action.name}: $taskTitle",
                    actionLabel = setActionLabel(action = action)
                )
                // undo delete
                undoDeletedTask(
                    action = action,
                    snackbarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    })
}



// show undo when delete a task
private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

// undo
private fun undoDeletedTask(
    action: Action,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackbarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}

// funcion que sirve solo para ver el preview
/*
@Composable
@Preview
private fun ListScreenPreview() {
    ListScreen(navigateToTaskScreen = {}, sharedViewModel =)
}*/
