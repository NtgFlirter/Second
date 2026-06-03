package com.yashwant.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.yashwant.ui.screen.*
import com.yashwant.viewmodel.CalculatorViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yashwant.ui.profile.EditProfileScreen
import kotlinx.coroutines.launch
import com.yashwant.ui.profile.ProfileScreen
import com.yashwant.viewmodel.AppViewModel

sealed class Screen(val route: String) {

    object Splash      : Screen("splash")      
    object Home        : Screen("home")
    object Profile     : Screen("profile")
    object Calculator  : Screen("calculator")
    object EditProfile : Screen("edit_profile")
    object HandCricket : Screen("hand_cricket")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    appViewModel: AppViewModel,
    viewModel: CalculatorViewModel = viewModel()
) {

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val isGameOrSplash =
        currentRoute == Screen.Splash.route ||
                currentRoute == Screen.Calculator.route ||
                currentRoute == Screen.HandCricket.route

    val showBottomBar = !isGameOrSplash

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {
            CustomDrawer(
                navController = navController,
                drawerState = drawerState,
                appViewModel = appViewModel
            )
        }

    ) {

        Scaffold(

       
            topBar = {

                if (!isGameOrSplash) {

                    when (currentRoute) {

                        Screen.Home.route -> {
                            TopAppBar(
                                title = { Text("Home") },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch { drawerState.open() }
                                        }
                                    ) {
                                        Icon(Icons.Default.Menu, null)
                                    }
                                }
                            )
                        }

                        Screen.Profile.route -> {
                            TopAppBar(
                                title = { Text("Profile") },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch { drawerState.open() }
                                        }
                                    ) {
                                        Icon(Icons.Default.Menu, null)
                                    }
                                }
                            )
                        }
                    }
                }
            },

            bottomBar = {

                if (showBottomBar) {

                    NavigationBar {

                        NavigationBarItem(
                            selected = currentRoute == Screen.Home.route,
                            onClick = {
                                navController.navigate(Screen.Home.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Home") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Profile.route,
                            onClick = {
                                navController.navigate(Screen.Profile.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.Person, null) },
                            label = { Text("Profile") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.HandCricket.route,
                            onClick = {
                                navController.navigate(Screen.HandCricket.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.Default.SportsCricket,
                                    contentDescription = null
                                )
                            },
                            label = { Text("Cricket") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Calculator.route,
                            onClick = {
                                navController.navigate(Screen.Calculator.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.Calculate, null) },
                            label = { Text("Calc") }
                        )
                    }
                }
            }

        ) { padding ->

            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,   // ← CHANGED
                modifier = Modifier.padding(padding)
            ) {

                // ── SPLASH ──
                composable(Screen.Splash.route) {
                    SplashScreen(navController = navController)
                }

                composable(Screen.Home.route) {
                    HomeScreen(appViewModel = appViewModel)
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        navController = navController,
                        profileViewModel = viewModel(),
                        appViewModel = appViewModel
                    )
                }

                composable(Screen.Calculator.route) {
                    CalculatorScreen(
                        viewModel = viewModel,
                        navController = navController,
                        appViewModel = appViewModel
                    )
                }

                composable("history") {
                    HistoryScreen(
                        navController = navController,
                        viewModel = viewModel,
                        appViewModel = appViewModel
                    )
                }

                composable(Screen.EditProfile.route) {
                    EditProfileScreen(
                        navController = navController,
                        appViewModel = appViewModel
                    )
                }

                composable(Screen.HandCricket.route) {
                    HandCricketScreen(
                        navController = navController
                    )
                }
            }
        }
    }
}
