package com.pepivsky.todocompose.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepivsky.todocompose.data.models.Priority
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.data.repositories.ToDoRepository
import com.pepivsky.todocompose.util.Action
import com.pepivsky.todocompose.util.Constants
import com.pepivsky.todocompose.util.RequestState
import com.pepivsky.todocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

// anotacion para hacer el viewModel inyectable
@HiltViewModel
class SharedViewModel @Inject constructor( // inyectando el toDoRepository en el viewModel
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    // val to observe that action is sended
    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

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

    // for search a task
    private val _searchedToDoTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks = _searchedToDoTasks

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
            toDoRepository.getSelectedTask(taskId = taskId).collect() { task ->
                _selectedTask.value = task
            }
        }
    }

    // update fields of this class
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

    fun updateTitle(newTitle: String) {
        if (newTitle.length < Constants.MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    // validate that title and description not are empty
    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

    // add task to DB
    private fun addTask() {
        // codigo que se ejecuta en una corutina
        viewModelScope.launch(Dispatchers.IO) {
            // creando el obketo que se va a guardar en la bd
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.addTask(toDoTask)
        }
        // close appBarSearch when new task is added
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.deleteTask(toDoTask = toDoTask)
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> addTask()
            Action.UPDATE -> updateTask()
            Action.DELETE -> deleteTask()
            Action.DELETE_ALL -> deleteAllTasks()
            Action.UNDO -> addTask()
            // else se llama cuando es NO_ACTION
            else -> {}

        }
        // finally setting default value to mutable state
        this.action.value = Action.NO_ACTION
    }

    fun searchInDB(searchQuery: String) {
        // loading when get tasks
        _searchedToDoTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.searchInDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedTasks ->
                        _searchedToDoTasks.value = RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedToDoTasks.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteAllTask()
        }
    }
}