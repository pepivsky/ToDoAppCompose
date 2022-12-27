package com.pepivsky.todocompose.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.data.repositories.ToDoRepository
import com.pepivsky.todocompose.util.RequestState
import com.pepivsky.todocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    // lista de tareas, se inicializa en Idle
    private val _allToDoTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks = _allToDoTasks

    fun getAllTasks() {
        // loading when get tasks
        _allToDoTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.getAllTasks.collect {
                    _allToDoTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allToDoTasks.value = RequestState.Error(e)
        }
    }
}