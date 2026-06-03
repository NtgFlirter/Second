package com.yashwant.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yashwant.ui.theme.AppColors

@Composable
fun SectionTitle(title: String, colors: AppColors) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = colors.textPrimary
    )
}