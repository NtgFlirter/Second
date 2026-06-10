package com.yashwant.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.SearchOff
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
    val orders by historyViewModel.orderList.collectAsState()
    val isLoading by historyViewModel.isLoading.collectAsState()
    val isOnline by appViewModel.isOnline.collectAsState()
    val order = orders.find { it.orderId == orderId }
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val repository = UserRepository(SettingsManager(context), context)

    val bgColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val brandGreen = Color(0xFF65B741)

    Box(modifier = Modifier.fillMaxSize()) {
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
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = brandGreen)
                    }
                }
                order == null -> {
                    Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SearchOff, null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("Order not found", color = textColor, fontWeight = FontWeight.Bold)
                            Text("This order might have been deleted.", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                        Text("Order #${order.orderId.takeLast(6)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                        val date = remember(order.orderTime) {
                            val formatter = java.text.SimpleDateFormat("dd MMMM yyyy, hh:mm a", java.util.Locale.getDefault())
                            formatter.format(java.util.Date(order.orderTime))
                        }
                        Text(date, color = Color.Gray, fontSize = 14.sp)

                        HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color.Gray.copy(0.2f))

                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(order.items) { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = item.image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(15.dp)),
                                        contentScale = ContentScale.Crop,
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

                        Button(
                            onClick = {
                                if (isOnline) {
                                    scope.launch {
                                        order.items.forEach { repository.addToCart(it) }
                                        navController.navigate("cart")
                                    }
                                }
                            },
                            enabled = isOnline,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOnline) brandGreen else brandGreen.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = if (isOnline) "Re-order All Items" else "Offline",
                                color = Color.White,
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