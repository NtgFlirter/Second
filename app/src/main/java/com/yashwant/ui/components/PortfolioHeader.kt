package com.yashwant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yashwant.model.PortfolioProfile
import com.yashwant.ui.theme.AppColors
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.yashwant.R


val OutfitFontFamily = FontFamily(
    Font(resId = R.font.outfit_bold, weight = FontWeight.Bold)
)


@Composable
fun PortfolioHeader(profile: PortfolioProfile, colors: AppColors) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xE4B6A0E3), Color(0xFF8463C9))
                )
            )
            // Glass on top of gradient
            .background(colors.glassBg)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFBB86FC).copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF7C3AED), Color(0xFF4F46E5))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "YV",
                        fontSize = 32.sp,
                        fontFamily = OutfitFontFamily,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = profile.name,
                fontSize = 22.sp,
                fontFamily = OutfitFontFamily,
                color = colors.textPrimary
            )

            Text(
                text = profile.username,
                fontSize = 14.sp,
                color = colors.accent
            )

            Spacer(Modifier.height(8.dp))

            // Role badge
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFD1BDF5),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = profile.role,
                    fontSize = 12.sp,
                    fontFamily = OutfitFontFamily,
                    color = colors.textSecondary
                )
            }

            Spacer(Modifier.height(10.dp))

            // Bio
            Text(
                text = profile.bio,
                fontSize = 12.sp,
                color = colors.textSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📍 ${profile.location}", fontSize = 12.sp, color = colors.textSecondary)
                Text("·", color = colors.textMuted)
                Text("🎓 ${profile.education}", fontSize = 12.sp, color = colors.textSecondary)
            }
        }
    }
}
