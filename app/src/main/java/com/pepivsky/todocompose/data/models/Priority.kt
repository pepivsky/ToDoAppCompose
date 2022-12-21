package com.pepivsky.todocompose.data.models

import androidx.compose.ui.graphics.Color
import com.pepivsky.todocompose.ui.theme.HighPriorityColor
import com.pepivsky.todocompose.ui.theme.LowPriorityColor
import com.pepivsky.todocompose.ui.theme.MediumPriorityColor
import com.pepivsky.todocompose.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor);
}