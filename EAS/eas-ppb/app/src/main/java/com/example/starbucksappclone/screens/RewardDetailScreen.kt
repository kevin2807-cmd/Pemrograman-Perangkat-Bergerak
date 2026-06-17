package com.example.starbucksappclone.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.starbucksappclone.viewmodel.CoffeeBlissViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardDetailScreen(
    navController: NavController,
    memberId: Int,
    rewardName: String,
    rewardPoints: Int,
    viewModel: CoffeeBlissViewModel
) {
    val member by viewModel.currentMember.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("REWARD DETAIL", color = Color.White) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(rewardName, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF1E212D))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$rewardPoints Poin", color = Color.Gray, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Nikmati secangkir $rewardName lezat dari kami.", color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Total Points Anda: ${member?.points ?: 0}", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.redeemReward(
                        memberId = memberId,
                        pointsRequired = rewardPoints,
                        onSuccess = {
                            navController.navigate("redeem_success_screen/${(member?.points ?: 0) - rewardPoints}") {
                                popUpTo("reward_screen/$memberId") { inclusive = false }
                            }
                        },
                        onError = {
                            Toast.makeText(context, "Poin tidak cukup", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2A365)),
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text("Redeem", fontSize = 18.sp)
            }
        }
    }
}
