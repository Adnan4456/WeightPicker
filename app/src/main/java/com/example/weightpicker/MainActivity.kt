package com.example.weightpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weightpicker.ui.theme.WeightPickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var weight by remember {
                mutableStateOf(80)
            }
            WeightPickerTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row {
                        Text(text = "$weight",
                            style = TextStyle(
                                fontSize = 34.sp,
                                color = Color.Black
                            )
                        )
                        Text(text = "KG",
                        textAlign = TextAlign.End)
                    }


                    Spacer(modifier = Modifier.height(50.dp))
                    Scale(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
//                            .align(Alignment.Center)
                    ) {

                        weight = it
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun PreView(){
}