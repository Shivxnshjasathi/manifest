package com.zincstate.manifest.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

data class PieChartData(
    val label: String,
    val value: Double,
    val color: Color
)

@Composable
fun PieChartComposable(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 60f
) {
    if (data.isEmpty()) {
        Box(modifier = modifier.aspectRatio(1f))
        return
    }

    val total = data.sumOf { it.value }
    var startAngle = -90f

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val radius = (size.minDimension - strokeWidth) / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        data.forEach { item ->
            val sweepAngle = ((item.value / total) * 360f).toFloat()
            drawArc(
                color = item.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweepAngle
        }
    }
}
