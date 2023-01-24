package com.pepivsky.todocompose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.saveable.rememberSaveable
import com.pepivsky.todocompose.data.models.Priority
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.pepivsky.todocompose.R

import com.pepivsky.todocompose.ui.theme.*


@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    // val to animate
    val angle: Float by animateFloatAsState(targetValue = if (expanded) 180F else 0F)

    var parentSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                // guardando el tamano del row
                parentSize = it.size
            }
            .height(PRIORITY_DROP_DOWN_HEIGHT)
            .border(
                width = 1.dp,
                // copy changes border color
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                // add rounded corners to row
                shape = MaterialTheme.shapes.small
            )
            .clickable {
                expanded = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(SMALL_PADDING))
        Box(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .clip(CircleShape)
                .background(priority.color)

        )
        Spacer(modifier = Modifier.size(SMALL_PADDING))
        Text(
            modifier = Modifier.weight(1F),
            text = priority.name,
            style = MaterialTheme.typography.subtitle2
        )

        // flecha que muestra el dropdown
        IconButton(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(angle),
            onClick = { expanded = true }) {

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.drop_down_arrow)
            )
        }
        DropdownMenu(
            // asignandole el tamano exacto del row que guardamos en parentSize
            modifier = Modifier.width(with(LocalDensity.current) { parentSize.width.toDp() }),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {

            // creando un item por cada elemento priority excepto para none
            Priority.values().filter { it != Priority.NONE }.forEach { priority ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onPrioritySelected(priority)
                }) {
                    PriorityItem(priority = priority)
                }
            }

        }
    }
}

@Composable
@Preview
fun PriorityDropDownPreview() {
    PriorityDropDown(priority = Priority.MEDIUM, onPrioritySelected = {})
}