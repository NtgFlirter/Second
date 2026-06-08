package com.yashwant.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yashwant.model.OrderItem
import com.yashwant.navigation.Screen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    historyViewModel: HistoryViewModel = viewModel()
) {
    val orders by historyViewModel.orderList.collectAsState(initial = emptyList())
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val brandGreen = Color(0xFF65B741)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, null, tint = brandGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor, titleContentColor = textColor)
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No orders yet!", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(orders.reversed()) { order ->
                    // ✅ FIX: navController yahan pass kiya card ko
                    OrderHistoryCard(order, navController, isDarkTheme, brandGreen, textColor)
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: OrderItem,
    navController: NavController, // ✅ FIX: Ye parameter add kiya
    isDark: Boolean,
    brandGreen: Color,
    textColor: Color
) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(30000)
            currentTime = System.currentTimeMillis()
        }
    }

    val diffMillis = order.expectedDeliveryTime - currentTime
    val remainingMins = (diffMillis / (1000 * 60)).toInt()

    val (statusText, statusColor) = when {
        remainingMins <= 0 -> "Delivered" to brandGreen
        remainingMins <= 5 -> "Almost there!" to Color(0xFFFFB800)
        else -> "Arriving in ${remainingMins}m" to Color(0xFFFFB800)
    }

    val dateFormatted = remember(order.orderTime) {
        java.text.SimpleDateFormat("dd MMM, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(order.orderTime))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // ✅ FIX: Card ko clickable banaya navigation ke liye
            .clickable {
                navController.navigate(Screen.OrderDetail.createRoute(order.orderId))
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(56.dp), shape = RoundedCornerShape(12.dp), color = brandGreen.copy(0.1f)) {
                Icon(Icons.Default.Restaurant, null, tint = brandGreen, modifier = Modifier.padding(12.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Order #${order.orderId.takeLast(5)}", fontWeight = FontWeight.Bold, color = textColor)
                Text(dateFormatted, color = Color.Gray, fontSize = 12.sp)
                Text("${order.items.size} Items • $${String.format("%.2f", order.totalAmount)}", color = textColor.copy(0.8f), fontSize = 14.sp)
            }
            Surface(color = statusColor.copy(0.1f), shape = RoundedCornerShape(8.dp)) {
                Text(statusText, color = statusColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}