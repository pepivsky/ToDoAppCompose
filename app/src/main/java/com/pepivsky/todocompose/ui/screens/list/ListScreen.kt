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
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {


    // effect que se dispara cuando el valor de action cambia
    LaunchedEffect(key1 = action, block = {
        sharedViewModel.handleDatabaseActions(action = action)
    })


    // lista de objetos todoTask, se transforma a un estado con collectAsState para poder ser obsrvado por la ui
    val allTasks by sharedViewModel.allTasks.collectAsState() // usando el by se puede tratar como una lista normal y no como un estado

    // lista de objetos buscados desde el appBar
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState() // usando el by se puede tratar como una lista normal y no como un estado

    // observar el sort state del viewmodel
    val sortState by sharedViewModel.sortState.collectAsState()

    // tareas con prioridad baja
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()

    // tareas con prioridad alta
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    // pass action to viewModel function
    // estado del scaffold
    val scaffoldState = rememberScaffoldState()

    // muestra el snackBar
    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = { sharedViewModel.action.value = it },
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
                navigateToTaskScreen = navigateToTaskScreen,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                onSwipeToDelete = { action, toDoTask ->
                    sharedViewModel.action.value = action
                    sharedViewModel.updateTaskFields(selectedTask = toDoTask)
                }
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
    onComplete: (Action) -> Unit,
    taskTitle: String,
    action: Action,
    onUndoClicked: (Action) -> Unit
) {

    val scope = rememberCoroutineScope()
    // cuando la variable action cambia se dispara el lauch effect
    LaunchedEffect(key1 = action, block = {
        Log.d("DisplaySnackBar", "triggered ${action.name}")
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action, taskTitle),
                    actionLabel = setActionLabel(action = action)
                )
                // undo delete
                undoDeletedTask(
                    action = action,
                    snackbarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            // call onComplete
            onComplete(Action.NO_ACTION)
        }
    })
}

// set delete all message
private fun setMessage(action: Action, taskTitle: String): String {
    return when(action) {
        Action.DELETE_ALL -> "All Tasks Removed."
        else -> "${action.name}: $taskTitle"
    }
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
