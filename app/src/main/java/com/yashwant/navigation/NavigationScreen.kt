package com.yashwant.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import androidx.navigation.*
import com.yashwant.ui.screen.*
import com.yashwant.viewmodel.CalculatorViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yashwant.ui.profile.EditProfileScreen
import kotlinx.coroutines.launch
import com.yashwant.ui.profile.ProfileScreen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.FoodHomeViewModel

// =========================================================
// 1. ALL ROUTES (Food App Focused)
// =========================================================
sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object FoodStart   : Screen("food_onboarding")
    object Login       : Screen("login")

    // Main Food Tabs
    object FoodHome    : Screen("food_home")
    object Cart        : Screen("cart")           // Naya
    object Search      : Screen("search")         // Naya
    object History     : Screen("history")        // Purana Calculator history ya Order history
    object Profile     : Screen("profile")

    object FoodDetail : Screen("food_detail/{foodName}") {
        fun createRoute(foodName: String) = "food_detail/$foodName"
    }

    object Home        : Screen("home")           // Portfolio Home
    object Calculator  : Screen("calculator")
    object EditProfile : Screen("edit_profile")
    object HandCricket : Screen("hand_cricket")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    appViewModel: AppViewModel,
    calculatorViewModel: CalculatorViewModel = viewModel()
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val foodHomeViewModel: FoodHomeViewModel = viewModel()

    val darkTheme by appViewModel.isDarkTheme.collectAsState()
    val scaffoldBg = if (darkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)

    // Logic: In screens par Bottom Bar aur Top Bar DIKHANA hai
    val mainAppScreens = listOf(
        Screen.FoodHome.route,
        Screen.Search.route,
        Screen.Cart.route,
        Screen.Profile.route,
        Screen.Home.route
    )
    val showBars = currentRoute in mainAppScreens

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
            // TOP APP BAR
            topBar = {
                if (showBars) {
                    TopAppBar(
                        title = {
                            val title = when(currentRoute) {
                                Screen.FoodHome.route -> "Waves of Food"
                                Screen.Search.route -> "Search Food"
                                Screen.Cart.route -> "Your Cart"
                                Screen.Profile.route -> "My Profile"
                                Screen.Home.route -> "Portfolio"
                                else -> "App"
                            }
                            Text(title)
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, null)
                            }
                        }
                    )
                }
            },

            containerColor = scaffoldBg,

            // FULL BOTTOM NAVIGATION
            bottomBar = {
                if (showBars) {
                    NavigationBar (
                        containerColor = scaffoldBg,
                    ){
                        // 1. Home
                        NavigationBarItem(
                            selected = currentRoute == Screen.FoodHome.route,
                            onClick = { navController.navigate(Screen.FoodHome.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Home") }
                        )

                        // 2. Search
                        NavigationBarItem(
                            selected = currentRoute == Screen.Search.route,
                            onClick = { navController.navigate(Screen.Search.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Search, null) },
                            label = { Text("Search") }
                        )

                        // 3. Cart
                        NavigationBarItem(
                            selected = currentRoute == Screen.Cart.route,
                            onClick = { navController.navigate(Screen.Cart.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.ShoppingCart, null) },
                            label = { Text("Cart") }
                        )

                        // 4. Profile
                        NavigationBarItem(
                            selected = currentRoute == Screen.Profile.route,
                            onClick = { navController.navigate(Screen.Profile.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Person, null) },
                            label = { Text("Profile") }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(padding)
            ) {
                // --- ENTRY FLOW ---
                composable(Screen.Splash.route) { SplashScreen(navController) }
                composable(Screen.FoodStart.route) { FoodOnboardingScreen(navController) }
                composable(Screen.Login.route) { LoginScreen(navController) }

                // --- MAIN FOOD APP ---
                composable(Screen.FoodHome.route) {
                    FoodHomeScreen(navController = navController, appViewModel = appViewModel)
                }

                composable(Screen.Cart.route) {
                    CartScreen(
                        navController = navController,
                        appViewModel = appViewModel
                    )
                }

                composable(
                    route = Screen.FoodDetail.route,
                    arguments = listOf(navArgument("foodName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("foodName") ?: ""
                    FoodDetailScreen(
                        navController = navController,
                        foodName = name,
                        foodViewModel = foodHomeViewModel // SHARED VIEWMODEL
                    )
                }

                // --- PORTFOLIO & TOOLS ---
                composable(Screen.Home.route) { HomeScreen(appViewModel) }
                composable(Screen.Profile.route) {
                    ProfileScreen(navController, viewModel(), appViewModel)
                }
                composable(Screen.Calculator.route) {
                    CalculatorScreen(calculatorViewModel, navController, appViewModel)
                }
                composable(Screen.History.route) {
                    HistoryScreen(navController, calculatorViewModel, appViewModel)
                }
                composable(Screen.EditProfile.route) {
                    EditProfileScreen(navController, appViewModel)
                }
                composable(Screen.HandCricket.route) {
                    HandCricketScreen(navController)
                }

                composable(Screen.Search.route) {
                    SearchScreen(navController = navController, appViewModel = appViewModel)
                }
            }
        }
    }
}