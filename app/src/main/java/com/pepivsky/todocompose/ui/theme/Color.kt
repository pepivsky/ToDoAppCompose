package com.pepivsky.todocompose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Colors

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

// colors for darktheme and lightheme
val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0XFF9C9C9C)
val DarkGray = Color(0xFF141414)


// colors for priority
val LowPriorityColor = Color(0xFF00c980)
val MediumPriorityColor = Color(0xFFFFC114)
val HighPriorityColor = Color(0xFFFF4646)
val NonePriorityColor = Color(0xFFFFFFFF)

// set color for elements in appBar
val Colors.topAppBarContentColor: Color
@Composable
get() = if (isLight) Color.White else LightGray

// set color for appBar background
val Colors.topAppBarBackgroundColor: Color
    @Composable
    get() = if (isLight) Purple500 else Color.Black

// set color for floating action button
val Colors.fabBackgroundColor: Color
@Composable
get() = if (isLight) Teal200 else Purple700

// color for item task
val Colors.taskItemBackgroundColor: Color
@Composable
get() = if (isLight) Color.White else DarkGray


// color for text on item task
val Colors.taskItemTextColor: Color
    @Composable
    get() = if (isLight) DarkGray else LightGray