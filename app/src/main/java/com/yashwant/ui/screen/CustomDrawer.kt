package com.yashwant.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yashwant.R
import com.yashwant.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun CustomDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    appViewModel: AppViewModel
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    //  GLOBAL THEME
    val darkTheme by appViewModel.isDarkTheme.collectAsState()

    val bgColor = if (darkTheme) Color(0xFF121212) else Color.White
    val textColor = if (darkTheme) Color.White else Color.Black
    val subTextColor = if (darkTheme) Color.LightGray else Color.Gray
    val itemBg = if (darkTheme) Color(0xFF1E1E1E) else Color(0xFFF1F1F1)

    val currentRoute =
        navController.currentBackStackEntry?.destination?.route

    ModalDrawerSheet(
        modifier = Modifier.width(280.dp),
        drawerContainerColor = bgColor
    ) {

        //  THEME BUTTON 
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(onClick = {
                appViewModel.toggleTheme()
            }) {

                Icon(
                    imageVector =
                        if (darkTheme) Icons.Default.LightMode
                        else Icons.Default.DarkMode,
                    contentDescription = "Theme",
                    tint = textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        //  PROFILE 
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Yashwant Vashisth",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = textColor
            )

            Text(
                text = "Android Developer",
                color = subTextColor,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = subTextColor.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(16.dp))

        //  MENU 
        val menuItems = listOf(
            "Home" to R.drawable.home,
            "Profile" to R.drawable.profile,
            "Calculator" to R.drawable.calculate
        )

        val routeMap = mapOf(
            "Home" to "home",
            "Profile" to "profile",
            "Calculator" to "calculator"
        )

        menuItems.forEach { item ->

            val isSelected = currentRoute == routeMap[item.first]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) itemBg else Color.Transparent)
                    .clickable {

                        scope.launch { drawerState.close() }

                        when (item.first) {

                            "Home" -> navController.navigate("home")
                            "Profile" -> navController.navigate("profile")
                            "Calculator" -> navController.navigate("calculator")

                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = item.second),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(
                        if (isSelected) textColor else subTextColor
                    )
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = item.first,
                    fontSize = 17.sp,
                    color = if (isSelected) textColor else subTextColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
