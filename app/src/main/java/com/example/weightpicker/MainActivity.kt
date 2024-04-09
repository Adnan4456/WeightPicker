package com.example.weightpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.weightpicker.ui.theme.WeightPickerTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeightPickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DrawingScreen()
                }
            }
        }
    }
}

@Composable
fun DrawingScreen(){

    val lines = remember {
        mutableStateListOf<Line>()
    }

    val scope = rememberCoroutineScope()

    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(true) {
            detectDragGestures { change, dragAmount ->
                change.consume()

                val line = Line(
                    start = change.position - dragAmount,
                    end = change.position
                )
                lines.add(line)
            }
        }
    ){
        lines.forEach{line ->

            drawLine(
                color = line.color,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx()
            )
        }
    }

}

data class Line(
    val start : Offset,
    val end : Offset,
    val color : Color= Color.Black,
    val strokeWidth: Dp = 1.dp
    )
