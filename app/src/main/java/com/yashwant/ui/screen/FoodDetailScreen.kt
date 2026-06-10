package com.yashwant.ui.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yashwant.R
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.CartItem
import com.yashwant.navigation.Screen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.FoodHomeViewModel
import kotlinx.coroutines.launch

@Composable
fun FoodDetailScreen(
    navController: NavController,
    foodName: String,
    appViewModel: AppViewModel, // Added AppViewModel
    foodViewModel: FoodHomeViewModel = viewModel()
) {
    val foodItems by foodViewModel.foodList.collectAsState()
    val isLoading by foodViewModel.isLoading.collectAsState()
    val isOnline by appViewModel.isOnline.collectAsState() // Observe online status
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()
    
    // 1. Decode the name and use flexible matching (trim and case-insensitive)
    val decodedName = remember(foodName) { Uri.decode(foodName).trim() }
    val foodItem = remember(foodItems, decodedName) { 
        foodItems.find { it.name.trim().equals(decodedName, ignoreCase = true) } 
    }

    var quantity by remember { mutableIntStateOf(1) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val repository = UserRepository(SettingsManager(context), context)
    
    val brandGreen = Color(0xFF65B741)
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White

    Box(modifier = Modifier.fillMaxSize()) {
        // --- Main Content ---
        if (foodItem == null) {
            Box(modifier = Modifier.fillMaxSize().background(backgroundColor), contentAlignment = Alignment.Center) {
                if (isLoading) {
                    CircularProgressIndicator(color = brandGreen)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Icon(Icons.Default.SearchOff, null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(16.dp))
                        Text("Food Item Not Found", color = textColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "We couldn't find '$decodedName'. Please try again.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = brandGreen)) {
                            Text("Go Back")
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().background(backgroundColor)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                    AsyncImage(
                        model = foodItem.image,
                        contentDescription = foodItem.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.food_start),
                        error = painterResource(R.drawable.food_start)
                    )

                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.White.copy(0.7f), RoundedCornerShape(12.dp))
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back",
                            tint = brandGreen,
                            modifier = Modifier.size(20.dp).padding(start = 5.dp)
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = foodItem.name, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFB800), modifier = Modifier.size(20.dp))
                            Text(text = " ${foodItem.rating}", color = textColor, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(text = foodItem.restaurantName, color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Description", color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = foodItem.description, color = Color.Gray, fontSize = 14.sp, lineHeight = 22.sp)

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.background(if(isDarkTheme) Color(0xFF252525) else Color(0xFFF3F3F3), RoundedCornerShape(10.dp)).padding(horizontal = 8.dp)
                        ) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }, enabled = isOnline) {
                                Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = if(isOnline) brandGreen else Color.Gray)
                            }
                            Text(text = "$quantity", color = textColor, modifier = Modifier.padding(horizontal = 16.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { quantity++ }, enabled = isOnline) {
                                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = if(isOnline) brandGreen else Color.Gray)
                            }
                        }

                        Button(
                            onClick = {
                                if (isOnline) {
                                    scope.launch {
                                        repository.addToCart(
                                            CartItem(
                                                name = foodItem.name,
                                                price = foodItem.price,
                                                image = foodItem.image,
                                                quantity = quantity,
                                                restaurantName = foodItem.restaurantName
                                            )
                                        )
                                        navController.navigate("cart")
                                    }
                                }
                            },
                            enabled = isOnline,
                            modifier = Modifier.height(56.dp).weight(1f).padding(start = 16.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOnline) brandGreen else brandGreen.copy(alpha = 0.5f)
                            )
                        ) {
                            val totalPrice = foodItem.price * quantity
                            Text(
                                text = if (isOnline) "Add to Cart - $${String.format("%.2f", totalPrice)}" else "Offline",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- STRICT OFFLINE OVERLAY ---
        if (!isOnline) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .pointerInput(Unit) {}, // BLOCK ALL INPUTS
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = brandGreen)
                        Spacer(modifier = Modifier.height(16.dp))
                        Icon(Icons.Default.WifiOff, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "PLEASE CHECK YOUR INTERNET CONNECTION",
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "You are currently offline. Please turn on your internet to continue.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
