package com.example.bankapp.charts.columnChart

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs
import kotlin.math.sqrt

@SuppressLint("DefaultLocale")
@Composable
fun ColumnChart(
    modifier: Modifier = Modifier,
    values: DoubleArray
) {

    val monthNamesNom = listOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )

    val maxAbsValue = (values.maxOf { abs(it) }).takeIf { it != 0.0 } ?: 1.0
    val maxValueForScaling = sqrt(maxAbsValue)

    val selectedIndex = remember { mutableStateOf(-1) }

    Canvas(
        modifier = modifier
            .pointerInput(values) {
                detectTapGestures { offset ->
                    val columnWidth = size.width / 12
                    val index = (offset.x / columnWidth).toInt()
                    selectedIndex.value = if (index in values.indices) index else -1
                }
            }
    ) {
        val columnWidth = size.width / 12
        val zeroY = size.height / 2

        values.forEachIndexed { index, value ->
            val x = index * columnWidth + columnWidth / 4
            val scaledHeightRatio = sqrt(abs(value)) / maxValueForScaling
            val barHeight = scaledHeightRatio.toFloat() * (size.height / 2 - 20f)

            val isPositive = value >= 0
            val top = if (isPositive) zeroY - barHeight else zeroY
            val bottom = if (isPositive) zeroY else zeroY + barHeight

            drawRect(
                color = if (isPositive) Color(0xFF26EA7F) else Color(0xFFFF5722),
                topLeft = Offset(x, top),
                size = Size(columnWidth / 2, bottom - top)
            )

            if (index == selectedIndex.value) {
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 36f
                        isAntiAlias = true
                    }

                    // Название месяца полностью, на русском
                    val monthLabel = monthNamesNom[index]

                    // Позиция суммы
                    val labelY = if (isPositive) top - 10 else bottom + 40

                    // Позиция месяца (чуть ниже суммы)
                    val monthY = if (isPositive) bottom + 40 else bottom + 80

                    // Отрисовка суммы
                    drawText(
                        String.format("%.2f", value),
                        x + columnWidth / 4,
                        labelY,
                        paint
                    )

                    // Отрисовка месяца
                    drawText(
                        monthLabel,
                        x + columnWidth / 4,
                        monthY,
                        paint
                    )
                }
            }

        }

        drawLine(
            color = Color.Gray,
            start = Offset(0f, zeroY),
            end = Offset(size.width, zeroY),
            strokeWidth = 2f
        )
    }

}
