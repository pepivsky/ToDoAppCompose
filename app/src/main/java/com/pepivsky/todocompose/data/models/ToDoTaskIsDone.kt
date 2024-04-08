package com.pepivsky.todocompose.data.models

import androidx.room.Entity


data class ToDoTaskIsDone(
    val id: Int,
    val isDone: Boolean,
    )
