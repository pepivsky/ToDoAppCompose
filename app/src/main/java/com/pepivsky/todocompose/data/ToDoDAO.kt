package com.pepivsky.todocompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pepivsky.todocompose.data.models.ToDoTask
import com.pepivsky.todocompose.data.models.ToDoTaskIsDone
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDAO {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTask(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE id = :taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    // partial update
    @Update(entity = ToDoTask::class)
    suspend fun updateIsDone(toDoTaskIsDone: ToDoTaskIsDone)

    // Actualizar todos los registros de isDone a true
    @Query("UPDATE todo_table SET isDone = 0")
    suspend fun updateAllTasksToUndone()

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTask()

    // search by title or description
    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LiKE :searchQuery")
    fun searchInDatabase(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        """
        SELECT * FROM todo_table ORDER BY
    CASE
        WHEN priority LIKE 'L%' THEN 1
        WHEN priority LIKE 'M%' THEN 2
        WHEN priority LIKE 'H%' THEN 3 
    END
    """
    )
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query(
        """
        SELECT * FROM todo_table ORDER BY 
    CASE
       WHEN priority LIKE 'H%' THEN 1
       WHEN priority LIKE 'M%' THEN 2
       WHEN priority LIKE 'L%' THEN 3
    END
    """
    )
    fun sortByHighPriority(): Flow<List<ToDoTask>>
}