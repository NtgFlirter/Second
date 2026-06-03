package com.yashwant.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yashwant.ui.components.CalculatorButton
import com.yashwant.ui.theme.Blue
import com.yashwant.ui.theme.DarkButton
import com.yashwant.ui.theme.DarkText
import com.yashwant.ui.theme.LightButton
import com.yashwant.ui.theme.LightText
import com.yashwant.ui.theme.Orange
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    navController: NavController,
    appViewModel: AppViewModel
) {

    val darkTheme by appViewModel.isDarkTheme.collectAsState()

    val buttonColor = if (darkTheme) DarkButton else LightButton
    val textColor = if (darkTheme) DarkText else LightText
    val operatorColor = if (darkTheme) Orange else Blue



    val scrollState = rememberScrollState()

    val buttons = listOf(
        listOf("C", "%", "÷", "⌫"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )


    LaunchedEffect(viewModel.expression.value) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("history")
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = textColor
                    )
                }

                /*

                IconButton(
                    onClick = {
                        navController.navigate("game")   // ✅ HERE
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Star, // or any game icon
                        contentDescription = "Game",
                        tint = textColor
                    )
                }



                IconButton(
                    onClick = {
                        viewModel.toggleTheme()
                    }
                ) {

                    Icon(
                        imageVector =
                            if (darkTheme)
                                Icons.Default.Brightness7
                            else
                                Icons.Default.Brightness4,

                        contentDescription = null,

                        tint = textColor
                    )
                }

                 */
            }

            Spacer(modifier = Modifier.height(20.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = viewModel.expression.value,
                    fontSize = 28.sp,
                    color = textColor.copy(alpha = 0.6f),
                    lineHeight = 34.sp,
                    softWrap = true
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = textColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            fun calculateResultFontSize(text: String): androidx.compose.ui.unit.TextUnit {

                val length = text.length

                return when {

                    length <= 6 -> 52.sp
                    length <= 10 -> 44.sp
                    length <= 14 -> 36.sp
                    length <= 18 -> 30.sp
                    else -> 24.sp
                }
            }


            val resultFontSize = calculateResultFontSize(viewModel.result.value)

            Text(
                text = viewModel.result.value,
                fontSize = resultFontSize,
                fontWeight = FontWeight.Light,
                color = textColor,
                maxLines = 1,
                softWrap = false
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            buttons.forEach { row ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    row.forEach { btn ->

                        val isOperator = btn in listOf("÷", "×", "-", "+", "=")

                        CalculatorButton(
                            text = btn,
                            modifier = if (btn == "0")
                                Modifier.weight(2f)
                            else
                                Modifier.weight(1f),
                            buttonColor = if (isOperator) operatorColor else buttonColor,
                            textColor = if (isOperator) Color.White else textColor,

                            onClick = {
                                viewModel.onButtonClick(btn)
                            },

                            onLongPress = if (btn == "⌫") {
                                {
                                    viewModel.onButtonClick("⌫")
                                }
                            } else null
                        )
                    }
                }
            }
        }
    }
}
