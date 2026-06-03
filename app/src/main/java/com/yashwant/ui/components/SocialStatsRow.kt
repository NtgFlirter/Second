package com.yashwant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yashwant.ui.theme.AppColors

// Each stat card has a unique accent tint for visual variety
private val statTints = listOf(
    Color(0xFF3B82F6), // blue  - Bot Users
    Color(0xFF8B5CF6), // purple - Projects
    Color(0xFF10B981), // green  - Repos
    Color(0xFFF59E0B)  // amber  - Graduate
)

@Composable
fun SocialStatsRow(stats: List<Pair<String, String>>, colors: AppColors) {

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (stats.isNotEmpty()) StatCard(stats[0].first, stats[0].second, statTints[0], colors, Modifier.weight(1f))
            if (stats.size > 1) StatCard(stats[1].first, stats[1].second, statTints[1], colors, Modifier.weight(1f))
        }

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (stats.size > 2) StatCard(stats[2].first, stats[2].second, statTints[2], colors, Modifier.weight(1f))
            if (stats.size > 3) StatCard(stats[3].first, stats[3].second, statTints[3], colors, Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(
    value: String,
    title: String,
    tint: Color,
    colors: AppColors,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .height(80.dp)
            .clip(shape)
            .background(colors.glassBg)
            .background(tint.copy(alpha = 0.12f))
            .border(1.dp, tint.copy(alpha = 0.30f), shape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = tint, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(title, color = colors.textSecondary, fontSize = 12.sp)
        }
    }
}