package com.yashwant.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.yashwant.model.AddressState
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    viewModel: ProfileViewModel = viewModel()
) {
    // 1. States observe karein
    val addressState by viewModel.addressState
    val isDark by appViewModel.isDarkTheme.collectAsState()
    val scrollState = rememberScrollState()

    // 2. Local input variables
    var street by remember { mutableStateOf(addressState.street) }
    var city by remember { mutableStateOf(addressState.city) }
    var stateName by remember { mutableStateOf(addressState.state) }
    var country by remember { mutableStateOf(addressState.country) }
    var zipCode by remember { mutableStateOf(addressState.zipCode) }
    var addressType by remember { mutableStateOf(addressState.addressType) }

    // Spinner (Dropdown) state
    var expanded by remember { mutableStateOf(false) }

    // 3. ✨ Sync Logic: Firebase se data aate hi boxes bhar do
    LaunchedEffect(addressState) {
        street = addressState.street
        city = addressState.city
        stateName = addressState.state
        country = addressState.country
        zipCode = addressState.zipCode
        addressType = addressState.addressType
    }

    // 4. Theme Colors
    val backgroundColor = if (isDark) Color(0xFF0B1220) else Color(0xFFF5F7FF)
    val headColor = if (isDark) Color.White else Color.Black

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
                    Text("←", color = headColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.width(20.dp))

                Text(
                    text = "Edit Address",
                    style = MaterialTheme.typography.headlineMedium,
                    color = headColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // ───────── INPUT FIELDS ─────────
            GlassField("Street Address", street, isDark) { street = it }
            GlassField("City", city, isDark) { city = it }
            GlassField("State", stateName, isDark) { stateName = it }
            GlassField("Country", country, isDark) { country = it }
            GlassField("Zip / Postal Code", zipCode, isDark) { zipCode = it }

            // ───────── ADDRESS TYPE SPINNER (Dropdown) ─────────
            Column {
                Text(
                    text = "Address Type",
                    color = if (isDark) Color(0xFFB0B8C5) else Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(6.dp))

                Box {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { expanded = true },
                        color = if (isDark) Color.White.copy(0.06f) else Color.Black.copy(0.04f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = addressType, color = headColor)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = headColor)
                        }
                    }

                    // Asli Dropdown Menu
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(if (isDark) Color(0xFF1A2235) else Color.White)
                            .fillMaxWidth(0.9f)
                    ) {
                        val options = listOf("Home", "Office", "Other")
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option, color = headColor) },
                                onClick = {
                                    addressType = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // ───────── SAVE BUTTON ─────────
            Button(
                onClick = {
                    viewModel.updateAddressState(
                        addressState.copy(
                            street = street,
                            city = city,
                            state = stateName,
                            country = country,
                            zipCode = zipCode,
                            addressType = addressType
                        )
                    )
                    viewModel.saveAddress()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D7CFE)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Save Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}