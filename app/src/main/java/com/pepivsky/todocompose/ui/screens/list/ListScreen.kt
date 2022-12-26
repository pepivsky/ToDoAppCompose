package com.pepivsky.todocompose.ui.screens.list

import android.util.Log
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.pepivsky.todocompose.R
import com.pepivsky.todocompose.ui.theme.fabBackgroundColor
import com.pepivsky.todocompose.ui.viewmodels.SharedViewModel
import com.pepivsky.todocompose.util.SearchAppBarState

@Composable
fun ListScreen(navigateToTaskScreen: (taskId: Int) -> Unit, sharedViewModel: SharedViewModel) {

    // se dispara solo cuando es la primera vez que se ejecuta esta funcion composable, no se ejecuta durante la recomposition
    LaunchedEffect(key1 = true, block = {
        Log.d("ListScreen", "LaunchEffect triggered!")
        sharedViewModel.getAllTasks()
    })

    // lista de objetos todoTask, se transforma a un estado con collectAsState para poder ser obsrvado por la ui
    val allTasks by sharedViewModel.allTasks.collectAsState() // usando el by se puede tratar como una lista normal y no como un estado

    /*for (task in allTasks.value) {
        Log.d("ListScreen", "for loop" + task.title)
    }*/

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    Scaffold(
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                tasks = allTasks,
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

// funcion que sirve solo para ver el preview
/*
@Composable
@Preview
private fun ListScreenPreview() {
    ListScreen(navigateToTaskScreen = {}, sharedViewModel =)
}*/
