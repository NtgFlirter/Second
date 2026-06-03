package com.yashwant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun CalculatorButton(
    text: String,
    buttonColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null
) {

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(75.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(buttonColor, RoundedCornerShape(20.dp))
            .pointerInput(Unit) {

                var job: Job? = null

                awaitEachGesture {

                    val down = awaitFirstDown()

                    var longPressTriggered = false

                    job = scope.launch {

                        delay(300) // long press threshold

                        longPressTriggered = true

                        while (isActive) {
                            onLongPress?.invoke()
                            delay(80)
                        }
                    }

                    // WAIT FOR RELEASE
                    val up = waitForUpOrCancellation()

                    job?.cancel()

                    // if user just tapped (no long press)
                    if (!longPressTriggered && up != null) {
                        onClick()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = textColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}