package com.yashwant.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yashwant.R
import com.yashwant.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Animation ke liye (Optional but looks professional)
    val scale = remember { Animatable(0f) }

    // Logic: 2 second ruko aur phir Onboarding par jao
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(durationMillis = 800)
        )
        delay(2000) // 2 second ka wait

        // Navigation logic
        navController.navigate(Screen.FoodStart.route) {
            // Splash ko backstack se hata do taaki back karne pe splash na dikhe
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    // Splash UI Design
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Figma background color
    ) {
        Image(
            painter = painterResource(id = R.drawable.food_logo), // Aapka logo
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .scale(scale.value)
        )
    }
}