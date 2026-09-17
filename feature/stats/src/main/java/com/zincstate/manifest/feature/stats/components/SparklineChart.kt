package com.zincstate.manifest.feature.stats.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SparklineChart(
    dataPoints: List<Double>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 4f
) {
    if (dataPoints.isEmpty()) {
        Canvas(modifier = modifier.fillMaxWidth().height(100.dp)) {}
        return
    }

    val maxVal = dataPoints.maxOrNull() ?: 1.0
    val minVal = dataPoints.minOrNull() ?: 0.0
    val range = maxVal - minVal

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val width = size.width
        val height = size.height

        val stepX = width / (dataPoints.size - 1).coerceAtLeast(1).toFloat()
        val path = Path()

        dataPoints.forEachIndexed { index, value ->
            val normalizedValue = if (range == 0.0) 0.5f else ((value - minVal) / range).toFloat()
            val x = index * stepX
            val y = height - (normalizedValue * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = strokeWidth)
        )
    }
}
