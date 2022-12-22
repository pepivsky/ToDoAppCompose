package com.pepivsky.todocompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.data.repositories.ToDoRepository
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