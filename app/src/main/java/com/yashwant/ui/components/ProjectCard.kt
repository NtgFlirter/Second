package com.yashwant.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yashwant.ui.theme.AppColors

@Composable
fun PortfolioCard(portfolio: String, portfolioUrl: String, colors: AppColors) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(shape)
            .background(colors.glassBg)
            .background(Color(0xFF7C3AED).copy(alpha = 0.14f)) // purple tint
            .border(1.dp, Color(0xFF7C3AED).copy(alpha = 0.35f), shape)
            .clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(portfolioUrl)))
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("🌐", fontSize = 28.sp)
                Column {
                    Text("YW-OS Portfolio", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(portfolio, color = colors.textSecondary, fontSize = 12.sp)
                }
            }
            Text("Explore ↗", color = colors.accent, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}