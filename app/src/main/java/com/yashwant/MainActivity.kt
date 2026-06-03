package com.yashwant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yashwant.navigation.NavigationScreen
import com.yashwant.ui.theme.SecondTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.yashwant.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()   // ← ADD THIS (must be before super.onCreate)

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val appViewModel: AppViewModel by viewModels()

        setContent {
            val darkTheme by appViewModel.isDarkTheme.collectAsState()

            SecondTheme(darkTheme = darkTheme) {
                NavigationScreen(
                    appViewModel = appViewModel
                )
            }
        }
    }
}