package com.pepivsky.todocompose.data.repositories

import com.pepivsky.todocompose.data.ToDoDAO
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.data.models.ToDoTaskIsDone
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// anotacion para que la instancia de ToDoRepository viva durante toda el ciclo de vida del viewModel
@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDAO: ToDoDAO) { // inyectando el dao con hilt, no es necesario proveerlo manualmente


    val getAllTasks: Flow<List<ToDoTask>> = toDoDAO.getAllTask()

    val sortByLowPriority: Flow<List<ToDoTask>> = toDoDAO.sortByLowPriority()

    val sortByHighPriority: Flow<List<ToDoTask>> = toDoDAO.sortByHighPriority()

    // no necesita ser una funcion suspendida ya que devuelve un flow y este objeto es asincrono por defecto
    fun getSelectedTask(taskId: Int): Flow<ToDoTask> {
        return toDoDAO.getSelectedTask(taskId = taskId)
    }

    suspend fun addTask(toDoTask: ToDoTask) {
        toDoDAO.addTask(toDoTask = toDoTask)
    }

    suspend fun updateTask(toDoTask: ToDoTask) {
        toDoDAO.updateTask(toDoTask = toDoTask)
    }


    suspend fun updateIsDone(toDoTaskIsDone: ToDoTaskIsDone) {
        toDoDAO.updateIsDone(toDoTaskIsDone)
    }

    suspend fun updateAllTasksToUndone() {
        toDoDAO.updateAllTasksToUndone()
    }

    suspend fun deleteTask(toDoTask: ToDoTask) {
        toDoDAO.deleteTask(toDoTask = toDoTask)
    }

    suspend fun deleteAllTask() {
        toDoDAO.deleteAllTask()
    }

    fun searchInDatabase(searchQuery: String): Flow<List<ToDoTask>> {
        return toDoDAO.searchInDatabase(searchQuery = searchQuery)
    }



}