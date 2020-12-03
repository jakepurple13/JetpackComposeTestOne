package com.programmersbox.jetpackcomposetestone

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathSegment
import androidx.core.graphics.drawable.toBitmap

data class DiamondModel(
    var progress: Int,
    var asset: ImageAsset? = null,
    var loadingWidth: Dp = 5.dp,
    var progressColor: Color = Color.Blue,
    var emptyColor: Color = Color.Gray
) {
    constructor(
        progress: Int,
        asset: Bitmap,
        loadingWidth: Dp,
        progressColor: Color,
        emptyColor: Color
    ) : this(progress, asset.asImageAsset(), loadingWidth, progressColor, emptyColor)

    constructor(
        progress: Int,
        asset: Drawable,
        loadingWidth: Dp,
        progressColor: Color,
        emptyColor: Color
    ) : this(progress, asset.toBitmap().asImageAsset(), loadingWidth, progressColor, emptyColor)
}

@Composable
fun DiamondLoaderCompose(model: DiamondModel, modifier: Modifier = Modifier) {
    DrawDiamondLoader(model = remember { model }, modifier)
}

@Composable
private fun DrawDiamondLoader(model: DiamondModel, modifier: Modifier) {
    Surface {
        val imagePaint = newStrokePaint(model.loadingWidth.value)
        val emptyStroke = newStrokePaint(model.loadingWidth.value)
        val progressStroke = newStrokePaint(model.loadingWidth.value)
        Canvas(modifier) {
            val (width, height) = size
            val halfHeight = width / 2f
            val halfWidth = height / 2f
            drawContext.canvas.withSaveLayer(bounds = drawContext.size.toRect(), paint = Paint()) {
                addImage(model.asset, halfWidth, halfHeight, halfWidth, halfHeight, imagePaint)
                drawRhombus(x = halfWidth, y = halfHeight, width = halfWidth, height = halfHeight, paint = model.emptyColor, stroke = emptyStroke)
                drawProgress(model.progress, halfWidth, halfHeight, width, height, model.progressColor, progressStroke)
            }
        }
    }
}

private fun newStrokePaint(width: Float) = Stroke(
    width = width * 2,
    cap = StrokeCap.Butt
)

private fun DrawScope.drawRhombus(x: Float, y: Float, width: Float, height: Float, paint: Color, stroke: Stroke) {
    val path = Path()
    path.moveTo(x, y + height) // Top
    path.lineTo(x - width, y) // Left
    path.lineTo(x, y - height) // Bottom
    path.lineTo(x + width, y) // Right
    path.lineTo(x, y + height) // Back to Top
    path.close()
    drawPath(path, paint, style = stroke)
    path.reset()
}

private fun DrawScope.addImage(imageAsset: ImageAsset?, x: Float, y: Float, width: Float, height: Float, stroke: Stroke) {
    val path = Path()
    path.moveTo(x, y + height) // Top
    path.lineTo(x - width, y) // Left
    path.lineTo(x, y - height) // Bottom
    path.lineTo(x + width, y) // Right
    path.lineTo(x, y + height) // Back to Top
    path.close()
    clipPath(path) { imageAsset?.let { drawImage(it, topLeft = Offset(x - it.width / 2, y - it.height / 2), style = stroke) } }
    path.reset()
}

private fun PathSegment.toPoint(progress: Int, max: Int, next: Int): PointF {
    val fProg = if (progress >= max) 1f else {
        (progress - (next * 25)) / 25f
    }

    return PointF(
        start.x + fProg * (end.x - start.x),
        start.y + fProg * (end.y - start.y)
    )
}

private fun DrawScope.drawProgress(progress: Int, x: Float, y: Float, width: Float, height: Float, paint: Color, stroke: Stroke) {

    val halfWidth = width / 2
    val halfHeight = height / 2
    val path = Path()
    path.moveTo(x, y - halfHeight)

    //top to right
    if (progress > 0) {
        val pathSegment = PathSegment(
            PointF(x, y - halfHeight), 0f,
            PointF(x + halfWidth, y), 1f
        )
        val p2 = pathSegment.toPoint(progress, 25, 0)
        path.lineTo(p2.x, p2.y)
    }

    //right to bottom
    if (progress > 25) {
        val pathSegment = PathSegment(
            PointF(x + halfWidth, y), 0f,
            PointF(x, y + halfHeight), 1f
        )
        val p2 = pathSegment.toPoint(progress, 50, 1)
        path.lineTo(p2.x, p2.y)
    }

    //bottom to left
    if (progress > 50) {
        val pathSegment = PathSegment(
            PointF(x, y + halfHeight), 0f,
            PointF(x - halfWidth, y), 1f
        )
        val p2 = pathSegment.toPoint(progress, 75, 2)
        path.lineTo(p2.x, p2.y)
    }

    //left to top
    if (progress > 75) {
        val pathSegment = PathSegment(
            PointF(x - halfWidth, y), 0f,
            PointF(x, y - halfHeight), 1f
        )
        val p2 = pathSegment.toPoint(progress, 100, 3)
        path.lineTo(p2.x, p2.y)
    }

    //finished!
    if (progress >= 100) {
        path.close()
    }

    drawPath(path, paint, style = stroke)
    path.reset()
}