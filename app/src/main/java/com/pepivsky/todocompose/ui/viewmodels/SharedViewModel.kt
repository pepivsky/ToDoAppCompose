package com.pepivsky.todocompose.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.data.repositories.ToDoRepository
import com.pepivsky.todocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

// anotacion para hacer el viewModel inyectable
@HiltViewModel
class SharedViewModel @Inject constructor( // inyectando el toDoRepository en el viewModel
    private val toDoRepository: ToDoRepository
    ) : ViewModel() {

    // default state is closed, when is open then show searchAppBar
    val searchAppBarState = mutableStateOf(SearchAppBarState.CLOSED)

    // text writed on searchAppBar
    val searchTextState = mutableStateOf("")

    // lista de
    private val _allToDoTasks = MutableStateFlow<List<ToDoTask>>(emptyList())
    val allTasks = _allToDoTasks

        fun getAllTask() {
            viewModelScope.launch {
                toDoRepository.getAllTasks.collect {
                    _allToDoTasks.value = it
                }
            }
        }
}