package com.pepivsky.todocompose.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepivsky.todocompose.data.models.Priority
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.data.repositories.ToDoRepository
import com.pepivsky.todocompose.util.RequestState
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

    // lista de tareas, se inicializa en Idle
    private val _allToDoTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks = _allToDoTasks

    // selected task
    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask = _selectedTask

    // values of task
    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

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

    // fun to get tasks
    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            toDoRepository.getSelectedTask(taskId = taskId). collect() { task ->
                _selectedTask.value = task
            }
        }
    }

    // update Task fields
    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else { // se llama cuando se da click en el FAB
            // setea los valores por defecto
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW

        }
    }
}