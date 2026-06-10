package com.yashwant.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.yashwant.model.CartItem
import com.yashwant.model.OrderItem
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()
    val isOnline by appViewModel.isOnline.collectAsState()
    val isProcessing by cartViewModel.isProcessing.collectAsState()

    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val brandGreen = Color(0xFF65B741)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val totalBill = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = backgroundColor
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your Cart",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (cartItems.isEmpty()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.ShoppingCart, null, tint = Color.Gray, modifier = Modifier.size(80.dp))
                            Spacer(Modifier.height(10.dp))
                            Text("Your cart is feeling light...", color = Color.Gray)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(cartItems) { item ->
                            CartItemRow(
                                item = item,
                                viewModel = cartViewModel,
                                cardColor = cardColor,
                                textColor = textColor,
                                brandGreen = brandGreen
                            )
                        }
                    }
                }

                if (cartItems.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(containerColor = brandGreen),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Subtotal", color = Color.White.copy(0.7f), fontSize = 14.sp)
                                Text(
                                    text = "$${String.format("%.2f", totalBill)}",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    if (!isOnline) {
                                        scope.launch { snackbarHostState.showSnackbar("Please check your internet connection") }
                                        return@Button
                                    }
                                    
                                    if (cartItems.isNotEmpty() && !isProcessing) {
                                        val currentTime = System.currentTimeMillis()
                                        val randomDeliveryMinutes = (20..45).random()
                                        val deliveryTimeInMillis = currentTime + (randomDeliveryMinutes * 60 * 1000)

                                        val newOrder = OrderItem(
                                            orderId = "ORD${currentTime}",
                                            userId = cartViewModel.uid ?: "",
                                            items = cartItems,
                                            totalAmount = totalBill,
                                            orderTime = currentTime,
                                            expectedDeliveryTime = deliveryTimeInMillis,
                                            status = "Confirmed"
                                        )

                                        cartViewModel.placeOrder(newOrder) { success ->
                                            if (success) {
                                                navController.navigate("order_success")
                                            } else {
                                                scope.launch { 
                                                    snackbarHostState.showSnackbar("Failed to place order. Check internet.") 
                                                }
                                            }
                                        }
                                    }
                                },
                                enabled = isOnline && !isProcessing,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isOnline) Color.White else Color.White.copy(alpha = 0.6f),
                                    disabledContainerColor = Color.White.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                            ) {
                                if (isProcessing) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = brandGreen, strokeWidth = 2.dp)
                                } else {
                                    Text(
                                        text = if (isOnline) "Place Order" else "Offline",
                                        color = if (isOnline) brandGreen else Color.Gray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
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
                                "You are currently offline. Please turn on your internet to place an order.",
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
}


@Composable
fun CartItemRow(
    item: CartItem,
    viewModel: CartViewModel,
    cardColor: Color,
    textColor: Color,
    brandGreen: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = cardColor,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = null,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.food_start),
                error = painterResource(id = R.drawable.food_start)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = item.restaurantName, color = Color.Gray, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                Text(text = "$${item.price}", color = brandGreen, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(brandGreen.copy(0.1f), RoundedCornerShape(10.dp))
                ) {
                    IconButton(onClick = { viewModel.updateQuantity(item, item.quantity - 1) }) {
                        Icon(Icons.Default.Remove, null, tint = brandGreen, modifier = Modifier.size(20.dp))
                    }
                    Text(text = "${item.quantity}", color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    IconButton(onClick = { viewModel.updateQuantity(item, item.quantity + 1) }) {
                        Icon(Icons.Default.Add, null, tint = brandGreen, modifier = Modifier.size(20.dp))
                    }
                }

                IconButton(onClick = { viewModel.removeItem(item.name) }) {
                    Icon(Icons.Default.DeleteOutline, null, tint = Color.Red.copy(0.6f))
                }
            }
        }
    }
}