package com.example.starbucksappclone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionSuccessScreen(
    navController: NavController,
    pointsEarned: Int,
    totalPoints: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HITA HITA COFFEE", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E212D))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF3E0))
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = "Success", tint = Color(0xFF1E212D), modifier = Modifier.size(64.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Transaksi Berhasil!", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF1E212D))
            Text("+$pointsEarned Poin", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFFF2A365))
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Points", color = Color.Gray, fontSize = 16.sp)
                    Text("$totalPoints", fontWeight = FontWeight.Bold, fontSize = 48.sp, color = Color(0xFF1E212D))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E212D)),
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text("OK", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
