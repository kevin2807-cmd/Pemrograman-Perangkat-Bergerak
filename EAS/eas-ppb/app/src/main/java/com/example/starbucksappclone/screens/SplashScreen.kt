package com.example.starbucksappclone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.starbucksappclone.R

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E212D)), // Dark Green
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.coffee_image), // Menggunakan gambar baru yang Anda tambahkan
                contentDescription = "Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "HITA HITA COFFEE",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "MEMBERSHIP",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = { navController.navigate("login_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2A365)),
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text("Start", fontSize = 18.sp)
            }
        }
    }
}
