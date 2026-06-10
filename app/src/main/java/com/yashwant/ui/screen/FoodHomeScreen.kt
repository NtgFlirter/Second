package com.yashwant.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yashwant.R
import com.yashwant.model.FoodItem
import com.yashwant.navigation.Screen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.FoodHomeViewModel

@Composable
fun FoodHomeScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    foodViewModel: FoodHomeViewModel = viewModel()
) {
    // --- States observe karein ---
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()
    val foodItems by foodViewModel.foodList.collectAsState() // API Data
    val isLoading by foodViewModel.isLoading.collectAsState() // Loading state

    // --- Theme Colors ---
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val searchBgColor = if (isDarkTheme) Color(0xFF252525) else Color(0xFFF3F3F3)
    val subTextColor = if (isDarkTheme) Color.LightGray else Color.Gray
    val brandGreen = Color(0xFF65B741)

    // --- API Data ko Category (Cuisine) ke hisaab se group karein ---
    val groupedItems = remember(foodItems) { foodItems.groupBy { it.cuisine } }

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {

        // 3. Loading Spinner: Jab tak API se data nahi aata
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = brandGreen
            )
        } else {
            // 4. Main Content (LazyColumn)
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // --- Header ---
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Find Your \nFavorite Food",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            lineHeight = 36.sp
                        )
                        IconButton(
                            onClick = { },
                            modifier = Modifier.background(searchBgColor, RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Default.Notifications, null, tint = brandGreen)
                        }
                    }
                }

                // --- Clickable Search Bar (Navigate to Search Screen) ---
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { navController.navigate(Screen.Search.route) }
                    ) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("What do you want to order?", color = subTextColor) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            readOnly = true,
                            shape = RoundedCornerShape(15.dp),
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = brandGreen) },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color.Transparent,
                                disabledContainerColor = searchBgColor,
                                disabledPlaceholderColor = subTextColor
                            )
                        )
                    }
                }

                // --- Promo Banner ---
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(150.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(containerColor = brandGreen)
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier.weight(1f).padding(20.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Special Deal \nfor You", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        // Redirect to Cart Screen
                                        navController.navigate(Screen.Cart.route) {
                                            // launchSingleTop ensures we don't open multiple copies of the same screen
                                            launchSingleTop = true
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Buy Now", color = brandGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                            Image(
                                painter = painterResource(id = R.drawable.food_logo),
                                contentDescription = null,
                                modifier = Modifier.size(130.dp).align(Alignment.CenterVertically).padding(end = 10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }

                // --- DYNAMIC CUISINE SECTIONS ---
                groupedItems.forEach { (cuisine, items) ->
                    item {
                        Column(modifier = Modifier.padding(bottom = 32.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(cuisine, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                                Text("View All", color = brandGreen, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(items) { food ->
                                    FoodVerticalCard(food, cardColor, textColor, brandGreen) {
                                        navController.navigate(Screen.FoodDetail.createRoute(food.name))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodVerticalCard(
    food: FoodItem,
    cardColor: Color,
    textColor: Color,
    brandGreen: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(160.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Asli API Image
            AsyncImage(
                model = food.image,
                contentDescription = food.name,
                modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.food_start),
                error = painterResource(R.drawable.food_start)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(food.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(food.restaurantName, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("$${food.price}", color = brandGreen, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB800), modifier = Modifier.size(14.dp))
                    Text(" ${food.rating}", color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}