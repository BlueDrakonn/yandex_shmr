package com.example.bankapp.charts.pieChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.bankapp.features.analysis.models.AnalysisCategoryUi

@Composable
fun PieChart(
    pieChartData: List<AnalysisCategoryUi>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 32f,
) {
    val colors = PieChartColors
    val data = pieChartData.sortedByDescending { it.percent }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            data.forEachIndexed { i, category ->
                val sweepAngle = category.percent * 360f / 100
                drawArc(
                    color = colors[i],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth),
                    size = Size(size.width, size.height),
                    topLeft = Offset.Zero
                )

                startAngle += sweepAngle
            }
        }
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(strokeWidth.dp)
            ) {
                items(data) { category ->
                    val name = "${(category.percent).toInt()}% ${category.category.name}"
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
            }
        }
    }
}


private val PieChartColors = listOf(
    Color(0xFFE91E63),
    Color(0xFFD7CCC8),
    Color(0xFFFF5722),
    Color(0xFFB0BEC5),
    Color(0xFFE6EE9C),
    Color(0xFF03A9F4),
    Color(0xFF795548),
    Color(0xFFCDDC39),
    Color(0xFF2196F3),
    Color(0xFFFFEB3B),
    Color(0xFF8BC34A),
    Color(0xFFBA68C8),
    Color(0xFFFFAB91),
    Color(0xFF4CAF50),
    Color(0xFF009688),
    Color(0xFFA1887F),
    Color(0xFF607D8B),
    Color(0xFF9C27B0),
    Color(0xFF80CBC4),
    Color(0xFF00BCD4),
    Color(0xFF26EA7F),
    Color(0xFFFF9800),
    Color(0xFF90A4AE),
    Color(0xFFFFC107),
    Color(0xFFFFF59D),
    Color(0xFF3F51B5),
    Color(0xFFCE93D8),
    Color(0xFF673AB7),
    Color(0xFFB2DFDB),
    Color(0xFFFFCC80)
)
