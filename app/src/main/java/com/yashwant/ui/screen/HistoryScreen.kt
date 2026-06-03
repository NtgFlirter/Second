package com.yashwant.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yashwant.ui.components.HistoryCard
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: CalculatorViewModel,
    appViewModel: AppViewModel    // ✅ Add AppViewModel here
) {

    // Observe global dark theme state
    val darkTheme by appViewModel.isDarkTheme.collectAsState()

    val bgColor = if (darkTheme) Color(0xFF121212) else Color.White
    val textColor = if (darkTheme) Color.White else Color.Black

    Scaffold(

        containerColor = bgColor,

        topBar = {

            TopAppBar(

                title = {
                    Text("History")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },

                actions = {

                    TextButton(
                        onClick = {
                            viewModel.clearHistory()
                        }
                    ) {

                        Text("Clear")
                    }
                }
            )
        }

    ) { padding ->

        if (viewModel.history.value.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No history yet",
                    fontSize = 18.sp
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(viewModel.history.value) { item ->

                    HistoryCard(

                        item = item,

                        onClick = {

                            viewModel.restoreHistory(item)

                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}