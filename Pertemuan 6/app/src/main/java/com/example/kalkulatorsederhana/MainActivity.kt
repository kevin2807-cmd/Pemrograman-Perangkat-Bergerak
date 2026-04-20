package com.example.kalkulatorsederhana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.kalkulatorsederhana.ui.theme.KalkulatorSederhanaTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalkulatorSederhanaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Hasil:") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = number1,
            onValueChange = { number1 = it },
            label = { Text("Angka 1") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = number2,
            onValueChange = { number2 = it },
            label = { Text("Angka 2") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Button(onClick = {
                result = "Hasil: ${(number1.toDoubleOrNull() ?: 0.0) + (number2.toDoubleOrNull() ?: 0.0)}"
            }) {
                Text("+")
            }

            Button(onClick = {
                result = "Hasil: ${(number1.toDoubleOrNull() ?: 0.0) - (number2.toDoubleOrNull() ?: 0.0)}"
            }) {
                Text("-")
            }

            Button(onClick = {
                result = "Hasil: ${(number1.toDoubleOrNull() ?: 0.0) * (number2.toDoubleOrNull() ?: 0.0)}"
            }) {
                Text("×")
            }

            Button(onClick = {
                val n2 = number2.toDoubleOrNull() ?: 0.0
                result = if (n2 == 0.0) {
                    "Tidak bisa dibagi 0"
                } else {
                    "Hasil: ${(number1.toDoubleOrNull() ?: 0.0) / n2}"
                }
            }) {
                Text("÷")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result, style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    KalkulatorSederhanaTheme {
        CalculatorApp()
    }
}