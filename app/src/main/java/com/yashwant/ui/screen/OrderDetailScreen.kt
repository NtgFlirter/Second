package com.yashwant.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.HistoryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.yashwant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: String,
    appViewModel: AppViewModel,
    historyViewModel: HistoryViewModel = viewModel()
) {
    val orders by historyViewModel.orderList.collectAsState(initial = emptyList())
    val order = orders.find { it.orderId == orderId }
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val repository = UserRepository(SettingsManager(context), context)

    val bgColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val brandGreen = Color(0xFF65B741)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Summary", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null, tint = brandGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor, titleContentColor = textColor)
            )
        },
        containerColor = bgColor
    ) { padding ->
        if (order == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

                // --- Order Info ---
                Text("Order #${order.orderId.takeLast(6)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                // 1. 'remember' use karne se ye calculation sirf ek baar hogi
                val date = remember(order.orderTime) {
                    val formatter = java.text.SimpleDateFormat("dd MMMM yyyy, hh:mm a", java.util.Locale.getDefault())
                    formatter.format(java.util.Date(order.orderTime))
                }
                Text(date, color = Color.Gray, fontSize = 14.sp)

                HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color.Gray.copy(0.2f))

                // --- Items List ---
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(order.items) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = item.image, // Coil will handle Int or String automatically
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp) // Size depends on the screen
                                    .clip(RoundedCornerShape(15.dp)),
                                contentScale = ContentScale.Crop,
                                // IMPORTANT: This shows while the image is loading or if it fails
                                placeholder = painterResource(id = R.drawable.food_start),
                                error = painterResource(id = R.drawable.food_start)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold, color = textColor)
                                Text("Qty: ${item.quantity}", color = Color.Gray, fontSize = 12.sp)
                            }
                            Text("$${item.price}", fontWeight = FontWeight.Bold, color = brandGreen)
                        }
                    }
                }

                // --- Re-order Button ---
                Button(
                    onClick = {
                        scope.launch {
                            // Loop through items and add back to cart
                            order.items.forEach { repository.addToCart(it) }
                            navController.navigate("cart") // Go to cart to see them
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = brandGreen)
                ) {
                    Text("Re-order All Items", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}