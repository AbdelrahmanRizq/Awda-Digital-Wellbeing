package com.awda.app.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

enum class NodeType {
    FIRST,
    MIDDLE,
    LAST,
    SPACER
}

@Composable
fun SingleNode(
    color: Color,
    nodeType: NodeType,
    nodeSize: Float,
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    isDashed: Boolean = false
) {
    Canvas(
        modifier = modifier
            .fillMaxHeight()
            .width((nodeSize / 2).dp)
    ) {
        val nodeRadius = nodeSize / 2
        val lineWidth = (3f / 4 * nodeRadius).coerceAtMost(40f)

        when (nodeType) {
            NodeType.FIRST -> {
                drawNodeCircle(isChecked, color, nodeRadius)
                drawBottomLine(isDashed, color, lineWidth, nodeRadius)
            }

            NodeType.MIDDLE -> {
                drawTopLine(isDashed, color, lineWidth, nodeRadius)
                drawNodeCircle(isChecked, color, nodeRadius)
                drawBottomLine(isDashed, color, lineWidth, nodeRadius)
            }

            NodeType.LAST -> {
                drawTopLine(isDashed, color, lineWidth, nodeRadius)
                drawNodeCircle(isChecked, color, nodeRadius)
            }

            NodeType.SPACER -> {
                drawSpacerLine(isDashed, color, lineWidth)
            }
        }
    }
}

fun DrawScope.drawNodeCircle(isChecked: Boolean, color: Color, radius: Float) {
    val centerOffset = Offset(size.width / 2, size.height / 2)

    if (isChecked) {
        drawCircle(
            color,
            radius,
            centerOffset
        )
    } else {
        val strokeWidth = radius / 2

        drawCircle(
            color,
            radius - strokeWidth / 2,
            centerOffset,
            style = Stroke(
                strokeWidth
            )
        )
    }
}

fun DrawScope.drawSpacerLine(
    isDashed: Boolean,
    color: Color,
    width: Float
) {
    val centerX = size.width / 2

    var topPoint = Offset(centerX, 0f)
    var bottomPoint = Offset(centerX, size.height)

    if (isDashed) {
        var spaceBetween = width
        var singleLinePart = width * 3

        topPoint = topPoint.plus(Offset(0f, spaceBetween / 2))
        bottomPoint = bottomPoint.minus(Offset(0f, spaceBetween / 2))

        val numberOfDoubleLines =
            (((bottomPoint.y - topPoint.y) - singleLinePart) / (singleLinePart + spaceBetween)).toInt()
        val onePartHeight = 4f * (bottomPoint.y - topPoint.y) / (4f * numberOfDoubleLines + 3)

        spaceBetween = onePartHeight / 4f
        singleLinePart = onePartHeight / 4f * 3f

        var currentTopPoint = topPoint.copy()
        var currentBottomPoint = currentTopPoint.plus(Offset(0f, singleLinePart))

        for (a in 0 until numberOfDoubleLines) {
            drawLine(
                color,
                currentTopPoint,
                currentBottomPoint,
                strokeWidth = width
            )

            currentTopPoint = currentTopPoint.plus(Offset(0f, singleLinePart + spaceBetween))
            currentBottomPoint = currentBottomPoint.plus(Offset(0f, singleLinePart + spaceBetween))
        }

        drawLine(
            color,
            currentTopPoint,
            currentBottomPoint,
            strokeWidth = width
        )
    } else {
        drawLine(
            color,
            topPoint,
            bottomPoint,
            strokeWidth = width
        )
    }
}

fun DrawScope.drawTopLine(isDashed: Boolean, color: Color, width: Float, circleRadius: Float) {
    val centerX = size.width / 2

    val topPoint = Offset(centerX, 0f)
    val bottomPoint = Offset(centerX, size.height / 2 - circleRadius + 3)

    if (isDashed) {
        val spaceBetween = width
        val singleLinePart = width * 3

        var currentTopPoint = Offset(centerX, spaceBetween / 2)
        var currentBottomPoint = Offset(centerX, singleLinePart + spaceBetween / 2)

        val addingSet = Offset(0f, spaceBetween + singleLinePart)

        while (currentBottomPoint.y < bottomPoint.y) {
            drawLine(
                color,
                currentTopPoint,
                currentBottomPoint,
                strokeWidth = width
            )

            currentTopPoint = currentTopPoint.plus(addingSet)
            currentBottomPoint = currentBottomPoint.plus(addingSet)
        }

        drawLine(
            color,
            currentTopPoint,
            bottomPoint,
            strokeWidth = width
        )
    } else {
        drawLine(
            color,
            topPoint,
            bottomPoint,
            strokeWidth = width
        )
    }
}

fun DrawScope.drawBottomLine(
    isDashed: Boolean,
    color: Color,
    width: Float,
    circleRadius: Float
) {
    val centerX = size.width / 2

    val topPoint = Offset(centerX, size.height / 2 + circleRadius - 3)
    val bottomPoint = Offset(centerX, size.height)

    if (isDashed) {
        val spaceBetween = width
        val singleLinePart = width * 3

        var currentTopPoint = Offset(centerX, size.height - spaceBetween / 2 - singleLinePart)
        var currentBottomPoint = Offset(centerX, size.height - spaceBetween / 2)

        val addingSet = Offset(0f, spaceBetween + singleLinePart)

        while (currentTopPoint.y > topPoint.y) {
            drawLine(
                color,
                currentTopPoint,
                currentBottomPoint,
                strokeWidth = width
            )

            currentTopPoint = currentTopPoint.minus(addingSet)
            currentBottomPoint = currentBottomPoint.minus(addingSet)
        }

        drawLine(
            color,
            topPoint,
            currentBottomPoint,
            strokeWidth = width
        )
    } else {
        drawLine(
            color,
            topPoint,
            bottomPoint,
            strokeWidth = width
        )
    }
}