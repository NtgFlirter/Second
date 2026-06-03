package com.yashwant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yashwant.ui.theme.AppColors

// Skill chip accent colors , cycles through for visual richness
private val chipColors = listOf(
    Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFF10B981),
    Color(0xFFF59E0B), Color(0xFFEF4444), Color(0xFF06B6D4)
)

@Composable
fun SkillsSection(skills: List<String>, colors: AppColors) {

    Column {
        SectionTitle("Skills", colors)
        Spacer(Modifier.height(12.dp))

        // Manual row wrapping - no FlowRow dependency needed
        val rows = skills.chunked(3)
        rows.forEachIndexed { rowIndex, rowSkills ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                rowSkills.forEachIndexed { colIndex, skill ->
                    val colorIdx = (rowIndex * 3 + colIndex) % chipColors.size
                    SkillChip(skill, chipColors[colorIdx], colors)
                }
            }
        }
    }
}

@Composable
fun SkillChip(skill: String, tint: Color = Color(0xFF3B82F6), colors: AppColors) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .clip(shape)
            .background(colors.glassBg)
            .background(tint.copy(alpha = 0.12f))
            .border(1.dp, tint.copy(alpha = 0.30f), shape)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(skill, color = tint, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}