package com.yashwant.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yashwant.R
import com.yashwant.model.openCustomTab
import com.yashwant.navigation.Screen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    profileViewModel: ProfileViewModel
) {
    val profileState by profileViewModel.state
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

    // Theme Colors
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val brandGreen = Color(0xFF65B741)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- 1. USER HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.img), // User Image
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Edit Icon Overlay
                IconButton(
                    onClick = { navController.navigate(Screen.EditProfile.route) },
                    modifier = Modifier.align(Alignment.BottomEnd).size(24.dp).background(brandGreen, CircleShape)
                ) {
                    Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = profileState.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(text = profileState.email, fontSize = 14.sp, color = Color.Gray)
            }
        }
        Spacer(Modifier.height(24.dp))

        // --- 3. QUICK OPTIONS ---
        Text("Account Settings", fontWeight = FontWeight.Bold, color = textColor, modifier = Modifier.padding(bottom = 12.dp))
        ProfileMenuItem("My Orders", Icons.Default.History, textColor) {
            navController.navigate(Screen.OrderHistory.route)
        }
        ProfileMenuItem("My Addresses", Icons.Default.LocationOn, textColor) { }
        ProfileMenuItem("Payment Methods", Icons.Default.Payment, textColor) { }

        // Old Tools as Shortcuts
        ProfileMenuItem("Calculator", Icons.Default.Calculate, textColor) { navController.navigate("calculator") }
        ProfileMenuItem("Hand Cricket", Icons.Default.SportsCricket, textColor) { navController.navigate("hand_cricket") }

        Spacer(Modifier.weight(1f))

        // --- 4. LOGOUT ---
        TextButton(
            onClick = { /* Firebase Logout logic */ },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp)
        ) {
            Text("Log Out", color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: ImageVector, textColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = textColor.copy(0.7f), modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f), color = textColor, fontSize = 16.sp)
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}