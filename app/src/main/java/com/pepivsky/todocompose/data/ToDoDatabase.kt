package com.pepivsky.todocompose.data

import androidx.room.Database
import com.pepivsky.todocompose.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase {

    abstract fun toDoDAO(): ToDoDAO
}