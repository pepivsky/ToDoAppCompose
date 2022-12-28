package com.pepivsky.todocompose.ui.screens.task

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.pepivsky.todocompose.data.models.Priority
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.util.Action


@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit) {
    Scaffold(
        topBar = {
            TaskAppBar(navigateToListScreen = navigateToListScreen, selectedTask = selectedTask)
        }, content = {
            TaskContent(
                title = "",
                onTitleChange = {},
                description = "",
                onDescriptionChange = {},
                priority = Priority.HIGH,
                onPrioritySelected = {}
            )
        }
    )

}
