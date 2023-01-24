package com.pepivsky.todocompose.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

// convert string to Action object
fun String?.toAction(): Action {
    // return an action object
    return if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
}