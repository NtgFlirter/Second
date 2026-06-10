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
import com.yashwant.ui.profile.EditAddressScreen
import com.yashwant.ui.profile.EditProfileScreen
import kotlinx.coroutines.launch
import com.yashwant.ui.profile.ProfileScreen
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.FoodHomeViewModel
import com.yashwant.viewmodel.ProfileViewModel

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object FoodStart   : Screen("food_onboarding")
    object Login       : Screen("login")
    object FoodHome    : Screen("food_home")
    object Cart        : Screen("cart")
    object Search      : Screen("search")
    object History     : Screen("history")
    object Profile     : Screen("profile")
    object FoodDetail : Screen("food_detail/{foodName}") {
        fun createRoute(foodName: String) = "food_detail/$foodName"
    }

    object EditAddress : Screen("edit_address")
    object Home        : Screen("home")
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

    // Create SHARED ViewModel here
    val foodHomeViewModel: FoodHomeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val ActiveColor = Color(0xFFBA5D00)
    val InactiveColor = Color(0xFF616161)


    val darkTheme by appViewModel.isDarkTheme.collectAsState()
    val scaffoldBg = if (darkTheme) Color(0xFF121212) else Color(0xFFFBFBFB)

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
            bottomBar = {
                if (showBars) {
                    NavigationBar(
                        containerColor = scaffoldBg,
                        tonalElevation = 8.dp,
                        modifier = Modifier.navigationBarsPadding(),
                        windowInsets = NavigationBarDefaults.windowInsets
                    ) {
                        NavigationBarItem(
                            selected = currentRoute == Screen.FoodHome.route,
                            onClick = { navController.navigate(Screen.FoodHome.route) { launchSingleTop = true } },
                            icon = { Icon(if (currentRoute == Screen.FoodHome.route) Icons.Filled.Home else Icons.Outlined.Home, null) },
                            label = { Text("Home") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ActiveColor,
                                selectedTextColor = ActiveColor,
                                unselectedIconColor = InactiveColor,
                                unselectedTextColor = InactiveColor,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = currentRoute == Screen.Search.route,
                            onClick = { navController.navigate(Screen.Search.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Outlined.ManageSearch, null) },
                            label = { Text("Categories") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ActiveColor,
                                selectedTextColor = ActiveColor,
                                unselectedIconColor = InactiveColor,
                                unselectedTextColor = InactiveColor,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = currentRoute == Screen.Cart.route,
                            onClick = { navController.navigate(Screen.Cart.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Outlined.ShoppingCart, null) },
                            label = { Text("Cart") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ActiveColor,
                                selectedTextColor = ActiveColor,
                                unselectedIconColor = InactiveColor,
                                unselectedTextColor = InactiveColor,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = currentRoute == Screen.Profile.route,
                            onClick = { navController.navigate(Screen.Profile.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Outlined.Person, null) },
                            label = { Text("Account") },
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
                composable(Screen.Splash.route) { SplashScreen(navController) }
                composable(Screen.FoodStart.route) { FoodOnboardingScreen(navController) }
                composable(Screen.Login.route) { LoginScreen(navController) }

                composable(Screen.FoodHome.route) {
                    FoodHomeScreen(
                        navController = navController, 
                        appViewModel = appViewModel,
                        foodViewModel = foodHomeViewModel
                    )
                }

                composable(Screen.Cart.route) {
                    CartScreen(navController = navController, appViewModel = appViewModel)
                }

                composable(
                    route = Screen.FoodDetail.route,
                    arguments = listOf(navArgument("foodName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("foodName") ?: ""
                    FoodDetailScreen(
                        navController = navController,
                        foodName = name,
                        appViewModel = appViewModel, // FIXED: Pass appViewModel
                        foodViewModel = foodHomeViewModel
                    )
                }

                composable(Screen.EditAddress.route) {
                    EditAddressScreen(
                        navController = navController,
                        appViewModel = appViewModel,
                        viewModel = profileViewModel
                    )
                }

                composable(Screen.OrderHistory.route) {
                    OrderHistoryScreen(navController, appViewModel)
                }

                composable(Screen.Home.route) { HomeScreen(appViewModel) }
                composable(Screen.Profile.route) { ProfileScreen(navController, appViewModel, viewModel()) }
                composable(Screen.Calculator.route) { CalculatorScreen(calculatorViewModel, navController, appViewModel) }
                composable(Screen.History.route) { HistoryScreen(navController, calculatorViewModel, appViewModel) }
                composable(Screen.EditProfile.route) { EditProfileScreen(navController, appViewModel) }
                composable(Screen.HandCricket.route) { HandCricketScreen(navController) }
                composable(Screen.Search.route) { SearchScreen(navController = navController, appViewModel = appViewModel) }
                composable("order_success") { OrderSuccessScreen(navController = navController, appViewModel = appViewModel) }

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