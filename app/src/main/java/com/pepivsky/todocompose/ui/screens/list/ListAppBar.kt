package com.pepivsky.todocompose.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pepivsky.todocompose.R
import com.pepivsky.todocompose.data.models.Priority
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.pepivsky.todocompose.components.DisplayAlertDialog
import com.pepivsky.todocompose.components.PriorityItem
import com.pepivsky.todocompose.ui.theme.*
import com.pepivsky.todocompose.ui.viewmodels.SharedViewModel
import com.pepivsky.todocompose.util.Action
import com.pepivsky.todocompose.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    // show by default the AppBar DefaultListAppBar
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = { sharedViewModel.persistSortState(it) },
                onDeleteAllConfirmed = {
                    sharedViewModel.action.value = Action.DELETE_ALL
                })
        }
        // when SearchAppBarState is OPENED then shoe searchAppBar
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { newText ->
                    sharedViewModel.searchTextState.value = newText
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""

                },
                // se dispara cuando el usuario presiona el boton de buscar del teclado
                onSearchClicked = {
                    sharedViewModel.searchInDB(searchQuery = it)
                }
            )
        }
    }
}

// app bar que se muestra por default
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_bar_title), color = MaterialTheme.colors.topAppBarContentColor) },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {

    // for manage dialog
    var openDialog by rememberSaveable { mutableStateOf(false) }
    
    DisplayAlertDialog(title = stringResource(id = R.string.delete_all_tasks), message = stringResource(
        id = R.string.delete_all_tasks_confirmation
    ), openDialog = openDialog, closeDialog = { openDialog = false }, onYesClicked = {
    onDeleteAllConfirmed()
    })

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllConfirmed = { openDialog = true })
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.FilterList,
            contentDescription = stringResource(id = R.string.sort_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
    // dropdown menu with prioritys
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        // creando un item por cada valor de priority excepto del MEDIUM
        Priority.values().filter { it != Priority.MEDIUM }.forEach { priority ->
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(priority)
            }) {
                PriorityItem(priority = priority)
            }
        }
    }
}

// delete all action
@Composable
fun DeleteAllAction(onDeleteAllConfirmed: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
    // dropdown menu with prioritys
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(onClick = {
            expanded = false
            onDeleteAllConfirmed()
        }) {
            Text(
                //modifier = Modifier.padding(start = LARGE_PADDING),
                text = stringResource(id = R.string.delete_all_action),
                style = Typography.subtitle2
            )
        }

    }
}

// search AppBar
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = stringResource(id = R.string.search_placeholder),
                    color = Color.White

                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    onClick = {  }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    // cuando hay texto y se le da tap a la x entonces lo setea a vacio
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else {
                        // cuando esta vacio se ejecuta el onClose y se cierra el appBar de busqueda
                        onCloseClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            // envia el texto del textField cuando se presiona el boton de buscar en el teclado
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

// for preview only
/*
@Preview
@Composable
private fun DefaultListAppBarPreview() {
    ListAppBar()
}
*/

// for preview only
@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(text = "Search", onTextChange = {}, onCloseClicked = {}, onSearchClicked = {})
}