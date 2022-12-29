package com.pepivsky.todocompose.ui.screens.task

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pepivsky.todocompose.data.models.Priority
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.ui.viewmodels.SharedViewModel
import com.pepivsky.todocompose.util.Action


@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: SharedViewModel
) {
    // variables que observan el viewModel
    val title: String by sharedViewModel.title
    val description by sharedViewModel.description
    val priority by sharedViewModel.priority

    Scaffold(
        topBar = {
            TaskAppBar(navigateToListScreen = navigateToListScreen, selectedTask = selectedTask)
        }, content = {
            TaskContent(
                title = title,
                onTitleChange = {
                    // controla el tamano del string que se escribe en textField del title, solo puede contener menos de 20 caracteres
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChange = {
                    sharedViewModel.description.value = it
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                }
            )
        }
    )

}
