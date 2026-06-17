package com.example.starbucksappclone.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.starbucksappclone.viewmodel.CoffeeBlissViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    memberId: Int,
    viewModel: CoffeeBlissViewModel
) {
    var amountText by remember { mutableStateOf("") }
    val pointsEarned = (amountText.toDoubleOrNull() ?: 0.0) / 10000
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Transaksi", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E212D))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF3E0))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Nominal Pembelian") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Poin Didapat: ${pointsEarned.toInt()}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E212D)
            )

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.addTransaction(memberId, amount) { earned, total ->
                            navController.navigate("transaction_success_screen/$earned/$total") {
                                popUpTo("add_transaction_screen/$memberId") { inclusive = true }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Masukkan nominal yang valid", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E212D))
            ) {
                Text("Simpan", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
