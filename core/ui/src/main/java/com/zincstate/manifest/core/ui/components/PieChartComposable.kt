package com.zincstate.manifest.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlin.math.atan2
import kotlin.math.PI

data class PieChartData(
    val label: String,
    val value: Double,
    val color: Color
)

@Composable
fun PieChartComposable(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 60f,
    gapAngle: Float = 2f // Gap between segments for border effect
) {
    if (data.isEmpty()) {
        Box(modifier = modifier.aspectRatio(1f))
        return
    }

    var selectedLabel by remember { mutableStateOf<String?>(null) }
    var tooltipOffset by remember { mutableStateOf(Offset.Zero) }

    val total = data.sumOf { it.value }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(data) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val angle = (atan2(offset.y - center.y, offset.x - center.x) * (180 / PI)).toFloat()
                            // Normalize angle to 0..360
                            var normalizedAngle = (angle + 90f)
                            if (normalizedAngle < 0) normalizedAngle += 360f

                            var currentStartAngle = 0f
                            data.forEach { item ->
                                val sweepAngle = ((item.value / total) * 360f).toFloat()
                                if (normalizedAngle >= currentStartAngle && normalizedAngle <= currentStartAngle + sweepAngle) {
                                    selectedLabel = item.label
                                    tooltipOffset = offset
                                    return@detectTapGestures
                                }
                                currentStartAngle += sweepAngle
                            }
                        },
                        onTap = { selectedLabel = null }
                    )
                }
        ) {
            val radius = (size.minDimension - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            var currentStartAngle = -90f

            data.forEach { item ->
                val sweepAngle = ((item.value / total) * 360f).toFloat()
                
                // Draw segment with a small gap to simulate borders
                drawArc(
                    color = item.color,
                    startAngle = currentStartAngle + (gapAngle / 2f),
                    sweepAngle = sweepAngle - gapAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                
                currentStartAngle += sweepAngle
            }
        }

        // Tooltip Popup
        selectedLabel?.let { label ->
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(tooltipOffset.x.toInt(), tooltipOffset.y.toInt() - 80),
                onDismissRequest = { selectedLabel = null }
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 4.dp
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
