package com.yashwant.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
fun SearchScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    foodViewModel: FoodHomeViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()
    val allLiveItems by foodViewModel.foodList.collectAsState()

    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val brandGreen = Color(0xFF65B741)

    val filteredItems = remember(searchQuery, allLiveItems) {
        if (searchQuery.isEmpty()) {
            allLiveItems.take(10)
        } else {
            allLiveItems.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.cuisine.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // 1. Root Column mein padding hata di taaki background full dikhe
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // 2. Padding sirf upar wale content par lagayi
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            Text(
                text = "Search Food",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Wave of Food..", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = brandGreen) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = brandGreen,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = if (isDarkTheme) Color(0xFF252525) else Color(0xFFF3F3F3),
                    unfocusedContainerColor = if (isDarkTheme) Color(0xFF252525) else Color(0xFFF3F3F3),
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredItems.isEmpty() && searchQuery.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No results found for '$searchQuery'", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            ) {
                items(filteredItems) { item ->
                    FoodListItem(
                        food = item,
                        cardColor = cardColor,
                        textColor = textColor,
                        subTextColor = Color.Gray,
                        brandGreen = brandGreen
                    ) {
                        navController.navigate(Screen.FoodDetail.createRoute(item.name))
                    }
                }
            }
        }
    }
}

@Composable
fun FoodListItem(
    food: FoodItem,
    cardColor: Color,
    textColor: Color,
    subTextColor: Color,
    brandGreen: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = cardColor,
        shadowElevation = if (cardColor == Color.White) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = food.image,
                contentDescription = food.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.food_start),
                error = painterResource(id = R.drawable.food_start)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = food.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                Text(text = food.restaurantName, color = subTextColor, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB800), modifier = Modifier.size(12.dp))
                    Text(text = " ${food.rating}", color = textColor, fontSize = 11.sp)
                }
            }

            Text(
                text = "$${food.price}",
                color = brandGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}