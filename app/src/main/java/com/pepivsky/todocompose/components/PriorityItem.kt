package com.pepivsky.todocompose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pepivsky.todocompose.data.models.Priority
import com.pepivsky.todocompose.ui.theme.LARGE_PADDING
import com.pepivsky.todocompose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.pepivsky.todocompose.ui.theme.Typography

@Composable
fun PriorityItem(priority: Priority) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // otra solucion para dibujar un circulo usando el canvas
        /*
        Canvas(modifier = Modifier.(16.dp)) {
            drawCircle(color = Priority.color)
        }
         */
        // dibuja un circulo
        Box(modifier = Modifier
            .size(PRIORITY_INDICATOR_SIZE)
            .clip(CircleShape)
            .background(priority.color))
        Spacer(modifier = Modifier.size(LARGE_PADDING))
        Text(
            text = priority.name,
            style = Typography.subtitle2,
            color = MaterialTheme.colors.onSurface
        )
    }
}

// for preview
@Preview
@Composable
fun PriorityItemPreview() {
    PriorityItem(priority = Priority.HIGH)
}