package com.yashwant.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavController
import com.yashwant.R
import com.yashwant.model.openCustomTab
import com.yashwant.navigation.Screen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    appViewModel: AppViewModel
) {

    val state = profileViewModel.state.value
    val darkTheme by appViewModel.isDarkTheme.collectAsState()
    val context = LocalContext.current


    val bgColor =
        if (darkTheme) Color(0xFF121212)
        else Color(0xFFF4F8FB)

    val cardColor =
        if (darkTheme) Color(0xFF1E1E1E)
        else Color.White

    val textColor =
        if (darkTheme) Color.White
        else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Box {

            Card(
                shape = CircleShape
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(R.drawable.animeduel),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
            }

            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.name.ifEmpty { "Yashwant" },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = .15f)
        ) {

            Text(
                text = state.email.ifEmpty {
                    "example@gmail.com"
                },
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ),
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column {

                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    title = "Edit Profile"
                ) {
                    navController.navigate(Screen.EditProfile.route)
                }

                ProfileMenuItem(
                    icon = Icons.Default.History,
                    title = "Calculator History"
                ) {
                    navController.navigate("history")
                }

                ProfileMenuItem(
                    icon = Icons.Default.Code,
                    title = "GitHub"
                ) {
                    openCustomTab(
                        context,
                        "https://github.com/yashwant2005"
                    )
                }

                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    title = "Portfolio"
                ) {
                    openCustomTab(
                        context,
                        "https://yashwantvashisthportfolio.vercel.app"
                    )
                }

                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Clear Profile",
                    color = Color.Red,
                    isLast = true
                ) {
                    profileViewModel.clearProfile()
                }
            }
        }
    }
}