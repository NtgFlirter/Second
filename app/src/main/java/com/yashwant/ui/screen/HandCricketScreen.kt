package com.yashwant.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yashwant.R
import com.yashwant.viewmodel.HandCricketViewModel

// ─────────────────────────────────────────────
// MAIN SCREEN
// ─────────────────────────────────────────────
@Composable
fun HandCricketScreen(
    navController: NavController,
    vm: HandCricketViewModel = viewModel()
) {

    val state by vm.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        // ───────── BACKGROUND ─────────
        Image(
            painter = painterResource(R.drawable.stadium_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        // ───────── MAIN UI ─────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(30.dp))

            // SCOREBOARD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreCard("YOU", state.playerScore, Color(0xFF3B82F6))
                ScoreCard("AI", state.aiScore, Color(0xFFEF4444))
            }

            Spacer(Modifier.height(40.dp))

            Spacer(Modifier.height(40.dp))

            // STATUS
            Text(
                text = if (state.isPlayerBatting)
                    "You are Batting"
                else
                    "You are Bowling",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(30.dp))

            // NUMBER PAD
            NumberPad { vm.play(it) }
        }

        // ───────── CLOSE BUTTON ─────────
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .size(42.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
        ) {
            Text("✖", color = Color.White)
        }

        // ───────── POPUP (BLOCKS GAME) ─────────
        if (state.showEvent) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.85f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = state.eventMessage,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                vm.hideEvent()

                                if (state.firstInningsCompleted) {
                                    vm.startSecondInnings()
                                }
                            }
                        ) {
                            Text("Continue")
                        }
                    }
                }
            }
        }

        if (state.gameOver) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.85f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = state.eventMessage,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                vm.resetGame()   // 🔥 restart
                            }
                        ) {
                            Text("Play Again 🔁")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ScoreCard(title: String, score: Int, color: Color) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.9f)
        ),
        modifier = Modifier.width(140.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(title, color = Color.White)

            Text(
                text = score.toString(),
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun NumberPad(onClick: (Int) -> Unit) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            (1..3).forEach { NumberButton(it, onClick) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            (4..6).forEach { NumberButton(it, onClick) }
        }
    }
}

@Composable
fun NumberButton(number: Int, onClick: (Int) -> Unit) {

    Card(
        onClick = { onClick(number) },
        modifier = Modifier.size(65.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = number.toString(),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}