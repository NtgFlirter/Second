package com.yashwant.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    viewModel: ProfileViewModel = viewModel()
) {
    // 1. Firebase se data aur Theme observe karein
    val profileState by viewModel.state
    val isDark by appViewModel.isDarkTheme.collectAsState()
    val scrollState = rememberScrollState()

    // 2. Local input variables (Inhe hum LaunchedEffect se update karenge)
    var name by remember { mutableStateOf(profileState.name) }
    var phone by remember { mutableStateOf(profileState.phone) }
    var email by remember { mutableStateOf(profileState.email) }
    var role by remember { mutableStateOf(profileState.role) }
    var location by remember { mutableStateOf(profileState.location) }

    // 3. ✨ MAGIC FIX: Jab data internet se load ho kar aaye, tab in boxes ko bhar do
    LaunchedEffect(profileState) {
        name = profileState.name
        phone = profileState.phone
        email = profileState.email
        role = profileState.role
        location = profileState.location
    }

    // 4. Dynamic Theme Colors
    val backgroundColor = if (isDark) Color(0xFF0B1220) else Color(0xFFF5F7FF)
    val headColor = if (isDark) Color.White else Color.Black
    val fieldLabelColor = if (isDark) Color(0xFFB0B8C5) else Color.Gray

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ───────── HEADER ─────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .background(
                            if (isDark) Color.White.copy(0.1f) else Color.Black.copy(0.05f),
                            CircleShape
                        )
                ) {
                    // Modern back arrow
                    Text("←", color = headColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    color = headColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // ───────── INPUT FIELDS ─────────
            GlassField("Name", name, isDark) { name = it }
            GlassField("Phone", phone, isDark) { phone = it }
            GlassField("Email", email, isDark) { email = it }
            GlassField("Role", role, isDark) { role = it }
            GlassField("Location", location, isDark) { location = it }

            Spacer(Modifier.height(10.dp))

            // ───────── SAVE BUTTON ─────────
            Button(
                onClick = {
                    // Pehle state update karo, phir Firebase mein save karo
                    viewModel.updateState(
                        profileState.copy(
                            name = name,
                            phone = phone,
                            email = email,
                            role = role,
                            location = location
                        )
                    )
                    viewModel.saveProfile()

                    // Back to profile screen
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4D7CFE) // Professional Blue
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Save Profile", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun GlassField(
    label: String,
    value: String,
    isDark: Boolean,
    onChange: (String) -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val textColor = if (isDark) Color.White else Color.Black
    val fieldBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    Column {
        Text(
            text = label,
            color = if (isDark) Color(0xFFB0B8C5) else Color.Gray,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(fieldBg),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4D7CFE),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF4D7CFE),
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            ),
            shape = shape,
            singleLine = true
        )
    }
}