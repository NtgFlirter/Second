package com.yashwant.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.navigation.NavController
import com.yashwant.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val scale = remember { Animatable(0f) }

    LaunchedEffect(true) {

        // logo pops in
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 700,
                easing = EaseOutBack
            )
        )

        delay(1200)  // hold screen for 1.2 seconds

        // go to Profile and remove splash from back stack
        navController.navigate(Screen.Profile.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1220)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "🏏",
                fontSize = 80.sp,
                modifier = Modifier.scale(scale.value)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Yashwant",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(scale.value)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Portfolio App",
                color = Color(0xFF4D7CFE),
                fontSize = 16.sp,
                modifier = Modifier.scale(scale.value)
            )
        }
    }
}