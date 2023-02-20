package com.pepivsky.todocompose.ui.screens.task

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.pepivsky.todocompose.R
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

    val context = LocalContext.current

    // for handle when back button is pressed
    BackHandler {
        Log.d("BACK", "Triggered")
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(navigateToListScreen = { action ->
                // validate when is empty, when is true then show a Toast
                if (action == Action.NO_ACTION || sharedViewModel.validateFields()) {
                    navigateToListScreen(action)
                } else {
                    displayToast(context = context)
                }
            }, selectedTask = selectedTask)
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

fun displayToast(context: Context) =
    Toast.makeText(context, context.getText(R.string.empty_fields), Toast.LENGTH_SHORT).show()
