package com.example.starbucksappclone.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

data class RewardItem(val name: String, val pointsRequired: Int)

val rewardList = listOf(
    RewardItem("Espresso", 50),
    RewardItem("Cappuccino", 100),
    RewardItem("Latte Gratis", 150)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    navController: NavController,
    memberId: Int,
    viewModel: CoffeeBlissViewModel
) {
    val member by viewModel.currentMember.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Redeem Reward", color = Color.White) },
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
            member?.let {
                Text(
                    text = "Poin Anda: ${it.points}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1E212D),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(rewardList) { reward ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(reward.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("${reward.pointsRequired} Poin", color = Color.Gray, fontSize = 14.sp)
                                }
                                Button(
                                    onClick = {
                                        navController.navigate("reward_detail_screen/$memberId/${reward.name}/${reward.pointsRequired}")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2A365))
                                ) {
                                    Text("Redeem", color = Color.White)
                                }
                            }
                        }
                    }
                }
            } ?: run {
                CircularProgressIndicator()
            }
        }
    }
}
