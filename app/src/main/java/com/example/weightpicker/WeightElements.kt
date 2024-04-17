package com.example.weightpicker

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import kotlin.math.*

@Composable
fun Scale(

    modifier: Modifier = Modifier,
    scale:ScaleStyle = ScaleStyle(),
    minWeight: Int  = 20,
    maxWeight: Int = 250,
    initialWeight: Int = 80,
    onWeightChange: (Int) -> Unit

){
    val radius = scale.radius
    val scaleWidth = scale.scaleWidth

    var center by remember {
        mutableStateOf(Offset.Zero)
    }

    var circleCenter by remember{
        mutableStateOf(Offset.Zero)
    }

    var angle by remember {
        mutableStateOf(0f)
    }

    var dragStartAngle by remember{
        mutableStateOf(0f)
    }

    var oldAngle by remember{
        mutableStateOf(angle)
    }


    Canvas(modifier = modifier
        .pointerInput(true){
            detectDragGestures(
                onDragStart ={offset ->
                    dragStartAngle = -atan2(
                        circleCenter.x - offset.x,
                        circleCenter.y - offset.y
                    ) * (180f / PI.toFloat())
                },
                onDragEnd = {
                    oldAngle = angle
                }
            ) { change, _ ->
                val touchAngel = -atan2(
                    circleCenter.x - change.position.x,
                    circleCenter.y - change.position.y
                ) * (180f / PI.toFloat())

                val newAngle = oldAngle + (touchAngel - dragStartAngle)
                angle = newAngle.coerceIn(
                    minimumValue = initialWeight - maxWeight.toFloat(),
                    maximumValue = initialWeight - minWeight.toFloat()

                )

                onWeightChange((initialWeight - angle).roundToInt())
//                change.position
            }
        }
    ){

        center = this.center
        circleCenter = Offset(x = center.x , y = scaleWidth.toPx()/2f + radius.toPx())

        val outerRadius = radius.toPx() + scaleWidth.toPx() / 2f

        val innerRadius = radius.toPx() - scaleWidth.toPx()/ 2f

        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    strokeWidth = scaleWidth.toPx()
                    color = Color.WHITE
                    setStyle(
                        Paint.Style.STROKE
                    )
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        Color.argb(50,0,0,0)
                    )
                }
            )
        }

        //Draw line using for loop
        for(i in minWeight..maxWeight){

            Log.d("Line = ", "$i")
            Log.d("initialWeight = ", "$initialWeight")
            Log.d("angle = ", "$angle")
            val angleInRadius = (i - initialWeight + angle - 90) *(PI/ 180f).toFloat()
            Log.d("angleInRadius = ", "$angleInRadius")
            val lineType = when{
                i % 10 == 0 -> LineType.TenStep
                i % 5 == 0 -> LineType.FiveStep
                else -> LineType.Normal
            }

            val lineColor  = when (lineType){
                LineType.TenStep -> scale.tenStepLineColor
                LineType.FiveStep -> scale.fiveStepLineColor
                LineType.Normal -> scale.normalLineColor
            }

            val lineLength  = when (lineType){
                LineType.TenStep -> scale.tenStepLineLength.toPx()
                LineType.FiveStep -> scale.fiveStepLineLength.toPx()
                LineType.Normal -> scale.normalLineLength.toPx()
            }

            val lineStart = Offset(
                x = (outerRadius - lineLength) * cos(angleInRadius) + circleCenter.x,
                y = (outerRadius - lineLength) * sin(angleInRadius) + circleCenter.y
            )

            val lineEnd = Offset(
                x = outerRadius  * cos(angleInRadius) + circleCenter.x,
                y = outerRadius  * sin(angleInRadius) + circleCenter.y
            )

            drawContext.canvas.nativeCanvas.apply {
                if (lineType is LineType.TenStep){
                    val x  = (outerRadius - lineLength - 5.dp.toPx() -  scale.textSize.toPx())*
                            cos(angleInRadius ) + circleCenter.x
                    val y  = (outerRadius - lineLength - 5.dp.toPx() -  scale.textSize.toPx())*
                            sin(angleInRadius ) + circleCenter.y
                    withRotation(
                        degrees = angleInRadius * (180f / PI.toFloat()) + 90f,
                        pivotX = x,
                        pivotY = y,
                    ) {
                        drawText(
                            abs(i).toString(),
                            x,
                            y,
                            Paint().apply {
                                textSize = scale.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }

                }
            }

            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

            val middleTop = Offset(
                x = circleCenter.x,
                y = circleCenter.y - innerRadius   - scale.scaleIndicatorLength.toPx()
            )

            val bottomLeft = Offset(
                x = circleCenter.x - 4f,
                y = circleCenter.y - innerRadius
            )
            val bottomRight = Offset(
                x = circleCenter.x + 4f,
                y = circleCenter.y - innerRadius
            )

            val indicator = Path().apply {
                moveTo(middleTop.x, middleTop.y)
                lineTo(bottomLeft.x,bottomLeft.y)
                lineTo(bottomRight.x,bottomRight.y)
                lineTo(middleTop.x, middleTop.y)

            }
            drawPath(
                path = indicator,
                color = scale.scaleIndicatorColor
            )
        }
    }
}