package com.pepivsky.todocompose.data.repositories

import com.pepivsky.todocompose.data.ToDoDAO
import com.pepivsky.todocompose.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// inyectando el dao con hilt, no es necesario proveerlo manualmente
class ToDoRepository @Inject constructor(private val toDoDAO: ToDoDAO) {

    val getAllTasks: Flow<List<ToDoTask>> = toDoDAO.getAllTask()

    val sortByLowPriority: Flow<List<ToDoTask>> = toDoDAO.sortByLowPriority()

    val sortByHighPriority: Flow<List<ToDoTask>> = toDoDAO.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoTask> {
        return toDoDAO.getSelectedTask(taskId = taskId)
    }

    suspend fun addTask(toDoTask: ToDoTask) {
        toDoDAO.addTask(toDoTask = toDoTask)
    }

    suspend fun updateTask(toDoTask: ToDoTask) {
        toDoDAO.updateTask(toDoTask = toDoTask)
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