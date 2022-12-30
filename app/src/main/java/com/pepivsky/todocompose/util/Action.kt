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
    return when (this) {
        "ADD" -> Action.ADD
        "UPDATE" -> Action.UPDATE
        "DELETE" -> Action.DELETE
        "DELETE_ALL" -> Action.DELETE_ALL
        "UNDO" -> Action.UNDO
        else -> Action.NO_ACTION
    }
}