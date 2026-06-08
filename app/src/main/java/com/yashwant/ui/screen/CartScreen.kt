package com.yashwant.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.yashwant.model.OrderItem
import com.yashwant.navigation.Screen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    cartViewModel: CartViewModel = viewModel()
) {
    // 1. Listen to Real-time Data
    val cartItems by cartViewModel.cartItems.collectAsState()
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

    // 2. Dynamic Theme Colors
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val brandGreen = Color(0xFF65B741)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val repository = UserRepository(SettingsManager(context), context)

    // 3. Billing Logic
    val totalBill = cartItems.sumOf { it.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // --- HEADER ---
        Text(
            text = "Your Cart",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- CONTENT AREA ---
        if (cartItems.isEmpty()) {
            // Empty Cart View
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingCart, null, tint = Color.Gray, modifier = Modifier.size(80.dp))
                    Spacer(Modifier.height(10.dp))
                    Text("Your cart is feeling light...", color = Color.Gray)
                }
            }
        } else {
            // List of Items
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

        // --- FOOTER BILLING SECTION ---
        if (cartItems.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = brandGreen),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // <--- YE LINE ZAROORI HAI: Taaki Row puri width le sake
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // Ab ye sahi kaam karega
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Side: Price Info
                    Column {
                        Text("Subtotal", color = Color.White.copy(0.7f), fontSize = 14.sp)
                        Text(
                            text = "$${String.format("%.2f", totalBill)}",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Right Side: Button
                    Button(
                        onClick = {
                            if (cartItems.isNotEmpty()) {
                                val currentTime = System.currentTimeMillis()
                                // Randomly choose between 20 and 45 minutes
                                val randomDeliveryMinutes = (20..45).random()
                                val deliveryTimeInMillis = currentTime + (randomDeliveryMinutes * 60 * 1000)

                                val newOrder = OrderItem(
                                    orderId = "ORD${currentTime}",
                                    userId = cartViewModel.uid ?: "",
                                    items = cartItems,
                                    totalAmount = totalBill,
                                    orderTime = currentTime,
                                    expectedDeliveryTime = deliveryTimeInMillis, // This is the fix!
                                    status = "Confirmed"
                                )

                                cartViewModel.placeOrder(newOrder) { success ->
                                    if (success) {
                                        navController.navigate("order_success")
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text("Place Order", color = brandGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
            // Food Image
            AsyncImage(
                model = item.image, // Coil automatically handles URL (String) or Drawable (Int)
                contentDescription = null,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.food_start),
                error = painterResource(id = R.drawable.food_start)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Info Section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = item.restaurantName,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "$${item.price}",
                    color = brandGreen,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }

            // Quantity Controls
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(brandGreen.copy(0.1f), RoundedCornerShape(10.dp))
                ) {
                    IconButton(onClick = { viewModel.updateQuantity(item, item.quantity - 1) }) {
                        Icon(Icons.Default.Remove, null, tint = brandGreen, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = "${item.quantity}",
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    IconButton(onClick = { viewModel.updateQuantity(item, item.quantity + 1) }) {
                        Icon(Icons.Default.Add, null, tint = brandGreen, modifier = Modifier.size(20.dp))
                    }
                }

                // Delete Button
                IconButton(onClick = { viewModel.removeItem(item.name) }) {
                    Icon(Icons.Default.DeleteOutline, null, tint = Color.Red.copy(0.6f))
                }
            }
        }
    }
}