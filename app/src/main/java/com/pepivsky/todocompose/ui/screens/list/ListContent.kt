package com.pepivsky.todocompose.ui.screens.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.pepivsky.todocompose.R
import com.pepivsky.todocompose.data.models.Priority
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.ui.theme.*
import com.pepivsky.todocompose.util.Action
import com.pepivsky.todocompose.util.RequestState
import com.pepivsky.todocompose.util.SearchAppBarState

@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    searchAppBarState: SearchAppBarState,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortState: RequestState<Priority>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onCheckBoxPressed: (Action, ToDoTask) -> Unit
) {

    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete,
                        onCheckBoxPressed = onCheckBoxPressed
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = allTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onCheckBoxPressed = onCheckBoxPressed
                    )
                }
            }
            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onCheckBoxPressed = onCheckBoxPressed
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onCheckBoxPressed = onCheckBoxPressed
                )
            }
        }
    }


}

@Composable
fun HandleListContent(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onCheckBoxPressed: (Action, ToDoTask) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        ShowTasks(
            tasks = tasks,
            navigateToTaskScreen = navigateToTaskScreen,
            onSwipeToDelete = onSwipeToDelete,
            onCheckBoxPressed = onCheckBoxPressed

        )
    }
}

// muestra la lista de Tasks
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowTasks(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onCheckBoxPressed: (Action, ToDoTask) -> Unit
) {
    LazyColumn {
        items(items = tasks, key = {
            it.id
        }) { task ->
            // funcionalidad swipe to delete, al hacer un swipe un 20% o completamente, el item se borra
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)

            // llamando la lambda
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                onSwipeToDelete(Action.DELETE, task)
            }

            // grados para animar
            val degrees by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) {
                    0F
                } else {
                    -45F
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = { FractionalThreshold(fraction = 0.5F) },
                background = { RedBackground(degrees = degrees) },
                dismissContent = {
                    // contenido que queremos borrar
                    TaskItem(
                        toDoTask = task,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onCheckBoxPressed = onCheckBoxPressed
                    )
                }


            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onCheckBoxPressed: (Action, ToDoTask) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = AppBarDefaults.TopAppBarElevation,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }
    ) {
        var state by rememberSaveable { mutableStateOf(toDoTask.isDone) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state, onCheckedChange = {
                state = it
                val newToDoTask = toDoTask.copy(isDone = state)
                onCheckBoxPressed(Action.UPDATE, newToDoTask)
            })
            Column(
                modifier = Modifier
                    .padding(all = LARGE_PADDING)
                    .fillMaxWidth()
            ) {
                Row {
                    Text(
                        modifier = Modifier.weight(1F),
                        text = toDoTask.title,
                        color = MaterialTheme.colors.taskItemTextColor,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textDecoration = if (state) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Box(
                        modifier = Modifier
                            .size(PRIORITY_INDICATOR_SIZE)
                            .clip(CircleShape)
                            .background(toDoTask.priority.color)
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = toDoTask.description,
                    color = MaterialTheme.colors.taskItemTextColor,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }


    }
}

// composable para la funcionalidad swipe to delete
@Composable
fun RedBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = LARGEST_PADDING),
        contentAlignment = Alignment.CenterEnd

    ) {
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = Color.White
        )
    }
}
/*
@Preview
@Composable
fun DoneCheck() {
    Box {
        Checkbox(checked = true, onCheckedChange = { })
    }
}
*/

@Preview
@Composable
fun TaskItemPreview() {
    /*TaskItem(
        toDoTask = ToDoTask(0, "Wash car", "Some random text", Priority.HIGH),
        navigateToTaskScreen = {},
        onCheckBoxPressed = {}
    )*/
}
