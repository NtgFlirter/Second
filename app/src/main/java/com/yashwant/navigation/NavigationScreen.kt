package com.yashwant.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import androidx.navigation.*
import com.yashwant.ui.screen.*
import com.yashwant.viewmodel.CalculatorViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ManageSearch
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ManageSearch
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    object OrderHistory : Screen("order_history")

    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }

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

    val ActiveColor = Color(0xFFBA5D00) // The Orange from your screenshot
    val InactiveColor = Color(0xFF616161) // Grey for unselected

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
                    NavigationBar(
                        // Container color scaffold ke background se match rakhein
                        containerColor = scaffoldBg,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            //  FIX: Fixed height ki jagah system padding use karein
                            .navigationBarsPadding(),
                        windowInsets = NavigationBarDefaults.windowInsets
                    ) {
                        // --- 1. HOME ---
                        NavigationBarItem(
                            selected = currentRoute == Screen.FoodHome.route,
                            onClick = { navController.navigate(Screen.FoodHome.route) { launchSingleTop = true } },
                            icon = { Icon(if (currentRoute == Screen.FoodHome.route) Icons.Filled.Home else Icons.Outlined.Home, null) },
                            label = {
                                Text("Home", fontWeight = if (currentRoute == Screen.FoodHome.route) FontWeight.Bold else FontWeight.Normal)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ActiveColor,
                                selectedTextColor = ActiveColor,
                                unselectedIconColor = InactiveColor,
                                unselectedTextColor = InactiveColor,
                                indicatorColor = Color.Transparent // ✅ REMOVES THE PILL BACKGROUND
                            )
                        )

                        // --- 2. CATEGORIES (Search) ---
                        NavigationBarItem(
                            selected = currentRoute == Screen.Search.route,
                            onClick = { navController.navigate(Screen.Search.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Outlined.ManageSearch, null) },
                            label = {
                                Text("Categories", fontWeight = if (currentRoute == Screen.Search.route) FontWeight.Bold else FontWeight.Normal)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ActiveColor,
                                selectedTextColor = ActiveColor,
                                unselectedIconColor = InactiveColor,
                                unselectedTextColor = InactiveColor,
                                indicatorColor = Color.Transparent
                            )
                        )

                        // --- 3. CART ---
                        NavigationBarItem(
                            selected = currentRoute == Screen.Cart.route,
                            onClick = { navController.navigate(Screen.Cart.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Outlined.ShoppingCart, null) },
                            label = {
                                Text("Cart", fontWeight = if (currentRoute == Screen.Cart.route) FontWeight.Bold else FontWeight.Normal)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ActiveColor,
                                selectedTextColor = ActiveColor,
                                unselectedIconColor = InactiveColor,
                                unselectedTextColor = InactiveColor,
                                indicatorColor = Color.Transparent
                            )
                        )

                        // --- 4. ACCOUNT (Profile) ---
                        NavigationBarItem(
                            selected = currentRoute == Screen.Profile.route,
                            onClick = { navController.navigate(Screen.Profile.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Outlined.Person, null) },
                            label = {
                                Text("Account", fontWeight = if (currentRoute == Screen.Profile.route) FontWeight.Bold else FontWeight.Normal)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ActiveColor,
                                selectedTextColor = ActiveColor,
                                unselectedIconColor = InactiveColor,
                                unselectedTextColor = InactiveColor,
                                indicatorColor = Color.Transparent
                            )
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

                composable(Screen.OrderHistory.route) {
                    OrderHistoryScreen(navController, appViewModel)
                }

                // --- PORTFOLIO & TOOLS ---
                composable(Screen.Home.route) { HomeScreen(appViewModel) }
                composable(Screen.Profile.route) {
                    // Sequence: NavController, AppViewModel, ProfileViewModel
                    ProfileScreen(navController, appViewModel, viewModel())
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

                composable("order_success") {
                    OrderSuccessScreen(
                        navController = navController,
                        appViewModel = appViewModel
                    )
                }

                composable(
                    route = Screen.OrderDetail.route,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                    OrderDetailScreen(navController, orderId, appViewModel)
                }
            }
        }
    }
}