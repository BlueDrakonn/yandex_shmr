package com.example.bankapp.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoundedPlusIcon() {
    Canvas(modifier = Modifier.size(15.56.dp)) {
        val strokeWidth = 2.dp.toPx()
        val cornerRadius = 2.dp.toPx()


        drawRoundRect(
            color = Color.White,
            topLeft = Offset(0f, size.height / 2 - strokeWidth / 2),
            size = Size(size.width, strokeWidth),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )


        drawRoundRect(
            color = Color.White,
            topLeft = Offset(size.width / 2 - strokeWidth / 2, 0f),
            size = Size(strokeWidth, size.height),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    }
}