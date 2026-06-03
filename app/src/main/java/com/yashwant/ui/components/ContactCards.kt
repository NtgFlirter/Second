package com.yashwant.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.yashwant.model.PortfolioProfile
import com.yashwant.ui.theme.AppColors

@Composable
fun ContactCards(profile: PortfolioProfile, colors: AppColors) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // EMAIL CARD
        Box(
            modifier = Modifier
                .weight(1f)
                .height(95.dp)
                .clip(shape)
                .background(colors.glassBg)
                .background(Color(0xFF1E88E5).copy(alpha = 0.12f))
                .border(1.dp, Color(0xFF1E88E5).copy(alpha = 0.2f), shape)
                .clickable {
                    context.startActivity(
                        Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${profile.email}")
                        }
                    )
                }
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {

                Row() {
                    Text("📧", fontSize = 18.sp)
                    Spacer(Modifier.height(4.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Email",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
                Text(profile.email, color = colors.textSecondary, fontSize = 12.sp, maxLines = 1)
            }
        }

        // LINKEDIN CARD
        Box(
            modifier = Modifier
                .weight(1f)
                .height(95.dp)
                .clip(shape)
                .background(colors.glassBg)
                .background(Color(0xFF0A66C2).copy(alpha = 0.20f))
                .border(1.dp, Color(0xFF0A66C2).copy(alpha = 0.4f), shape)
                .clickable {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(profile.linkedinUrl))
                    )
                }
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                Row() {
                    Text(
                        "in",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A66C2)
                    )
                    Spacer(Modifier.height(4.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "LinkedIn",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
                Text("Connect ↗", color = Color(0xFF0A66C2), fontSize = 10.sp)
            }
        }
    }
}
