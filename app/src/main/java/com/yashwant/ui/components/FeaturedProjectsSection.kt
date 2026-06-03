package com.yashwant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.yashwant.model.Project
import com.yashwant.ui.theme.AppColors

@Composable
fun FeaturedProjectsSection(projects: List<Project>, colors: AppColors) {
    Column {
        SectionTitle("Featured Projects", colors)
        Spacer(Modifier.height(12.dp))
        projects.forEach { project ->
            ProjectCard(project, colors)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun ProjectCard(project: Project, colors: AppColors) {
    val shape = RoundedCornerShape(18.dp)
    val tintColor = Color(project.gradientStart)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.glassBg)
            .background(tintColor.copy(alpha = 0.18f))   // colored glass tint
            .border(1.dp, tintColor.copy(alpha = 0.35f), shape)
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(project.name, color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("↗", color = tintColor, fontSize = 16.sp)
            }

            Spacer(Modifier.height(6.dp))

            Text(project.description, color = colors.textSecondary, fontSize = 13.sp, fontFamily = OutfitFontFamily)

            Spacer(Modifier.height(10.dp))

            // Tech pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(tintColor.copy(alpha = 0.15f))
                    .border(0.5.dp, tintColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(project.tech, color = colors.textSecondary, fontSize = 11.sp, fontFamily = OutfitFontFamily)
            }
        }
    }
}