package com.yashwant.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Star
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
import com.yashwant.R
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.CartItem
import com.yashwant.navigation.Screen
import com.yashwant.viewmodel.FoodHomeViewModel // Import Food ViewModel
import kotlinx.coroutines.launch

@Composable
fun FoodDetailScreen(
    navController: NavController,
    foodName: String,
    foodViewModel: FoodHomeViewModel = viewModel() // 1. ViewModel connect kiya
) {
    // 2. ViewModel se asli food item nikaalo
    val foodItem = remember(foodName) { foodViewModel.getFoodByName(foodName) }

    // Quantity state
    var quantity by remember { mutableIntStateOf(1) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val repository = UserRepository(SettingsManager(context), context)

    // Agar data nahi mila toh loader dikhao
    if (foodItem == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF65B741))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. DYNAMIC IMAGE
        Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
            AsyncImage(
                model = foodItem.image, // Asli photo URL ya local ID
                contentDescription = foodItem.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.food_start),
                error = painterResource(R.drawable.food_start)
            )

            // Back Button
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
                    tint = Color(0xFF65B741),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 2. CONTENT SECTION
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = foodItem.name, // Dynamic Name
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB800), modifier = Modifier.size(20.dp))
                    Text(text = " ${foodItem.rating}", fontWeight = FontWeight.Bold)
                }
            }

            Text(text = foodItem.restaurantName, color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // 3. DYNAMIC DESCRIPTION
            Text(
                text = foodItem.description, // Dynamic Description
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // 4. BOTTOM CONTROLS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFF3F3F3), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp)
                ) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF65B741))
                    }
                    Text(text = "$quantity", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { quantity++ }) {
                        Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF65B741))
                    }
                }

                // Add to Cart Button
                Button(
                    onClick = {
                        scope.launch {
                            repository.addToCart(
                                CartItem(
                                    name = foodItem.name,
                                    price = foodItem.price, // Asli price
                                    image = foodItem.image as? Int ?: R.drawable.food_start, // Logic handle
                                    quantity = quantity,
                                    restaurantName = foodItem.restaurantName
                                )
                            )
                            navController.navigate(Screen.Cart.route)
                        }
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                        .padding(start = 16.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF65B741))
                ) {
                    // Price calculation in button
                    val totalPrice = foodItem.price * quantity
                    Text("Add to Cart - $${String.format("%.2f", totalPrice)}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}