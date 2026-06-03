package com.yashwant.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

    val state = viewModel.state.value
    val darkTheme by appViewModel.isDarkTheme.collectAsState()

    var name by remember { mutableStateOf(state.name) }
    var phone by remember { mutableStateOf(state.phone) }
    var email by remember { mutableStateOf(state.email) }
    var role by remember { mutableStateOf(state.role) }
    var location by remember { mutableStateOf(state.location) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1220))
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
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
                        .background(Color.Black.copy(0.3f), CircleShape)
                ) {
                    Text("←", color = Color.White, fontSize = 30.sp)
                }

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }


            // ───────── FIELDS ─────────
            GlassField("Name", name) { name = it }
            GlassField("Phone", phone) { phone = it }
            GlassField("Email", email) { email = it }
            GlassField("Role", role) { role = it }
            GlassField("Location", location) { location = it }

            Spacer(Modifier.height(10.dp))

            // ───────── SAVE BUTTON ─────────
            Button(
                onClick = {

                    viewModel.updateState(
                        state.copy(
                            name = name,
                            phone = phone,
                            email = email,
                            role = role,
                            location = location
                        )
                    )

                    viewModel.saveProfile()

                    navController.navigate("profile") {
                        popUpTo("edit_profile") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4D7CFE)
                )
            ) {
                Text("Save Profile", fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun GlassField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {

    val shape = RoundedCornerShape(14.dp)

    Column {

        Text(
            text = label,
            color = Color(0xFFB0B8C5),
            fontSize = MaterialTheme.typography.labelMedium.fontSize
        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(Color.White.copy(alpha = 0.06f)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4D7CFE),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF4D7CFE),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = shape
        )
    }
}