package com.pepivsky.todocompose.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.pepivsky.todocompose.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 2, exportSchema = true, autoMigrations = [AutoMigration(from = 1, to = 2)])
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun toDoDAO(): ToDoDAO
}