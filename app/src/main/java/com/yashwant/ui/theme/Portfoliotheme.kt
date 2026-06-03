package com.yashwant.ui.theme

import androidx.compose.ui.graphics.Color

object PortfolioDarkColors {
    val background       = Color(0xFF080E1E)
    val surface          = Color(0xFF0D1428)
    val glassBg          = Color(0x1AFFFFFF)   // white 10%
    val glassBorder      = Color(0x33FFFFFF)   // white 20%
    val glassBlur        = Color(0x0DFFFFFF)   // white 5%
    val textPrimary      = Color(0xFFFFFFFF)
    val textSecondary    = Color(0xFF94A3B8)
    val textMuted        = Color(0xFF64748B)
    val accent           = Color(0xFFBB86FC)   // purple
    val accentBlue       = Color(0xFF60A5FA)
    val accentGreen      = Color(0xFF34D399)
    val cardBg           = Color(0x1AFFFFFF)
    val divider          = Color(0x1AFFFFFF)
}

object PortfolioLightColors {
    val background       = Color(0xFFF0F4FF)
    val surface          = Color(0xFFFFFFFF)
    val glassBg          = Color(0x99FFFFFF)   // white 60%
    val glassBorder      = Color(0xCCFFFFFF)   // white 80%
    val glassBlur        = Color(0x66FFFFFF)   // white 40%
    val textPrimary      = Color(0xFF0F172A)
    val textSecondary    = Color(0xFF475569)
    val textMuted        = Color(0xFF94A3B8)
    val accent           = Color(0xFF7C3AED)
    val accentBlue       = Color(0xFF2563EB)
    val accentGreen      = Color(0xFF059669)
    val cardBg           = Color(0xB3FFFFFF)
    val divider          = Color(0x33000000)
}

data class AppColors(
    val background: Color,
    val surface: Color,
    val glassBg: Color,
    val glassBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val accent: Color,
    val accentBlue: Color,
    val accentGreen: Color,
    val cardBg: Color,
    val divider: Color
)

fun appColors(isDark: Boolean): AppColors = if (isDark) {
    AppColors(
        background    = PortfolioDarkColors.background,
        surface       = PortfolioDarkColors.surface,
        glassBg       = PortfolioDarkColors.glassBg,
        glassBorder   = PortfolioDarkColors.glassBorder,
        textPrimary   = PortfolioDarkColors.textPrimary,
        textSecondary = PortfolioDarkColors.textSecondary,
        textMuted     = PortfolioDarkColors.textMuted,
        accent        = PortfolioDarkColors.accent,
        accentBlue    = PortfolioDarkColors.accentBlue,
        accentGreen   = PortfolioDarkColors.accentGreen,
        cardBg        = PortfolioDarkColors.cardBg,
        divider       = PortfolioDarkColors.divider
    )
} else {
    AppColors(
        background    = PortfolioLightColors.background,
        surface       = PortfolioLightColors.surface,
        glassBg       = PortfolioLightColors.glassBg,
        glassBorder   = PortfolioLightColors.glassBorder,
        textPrimary   = PortfolioLightColors.textPrimary,
        textSecondary = PortfolioLightColors.textSecondary,
        textMuted     = PortfolioLightColors.textMuted,
        accent        = PortfolioLightColors.accent,
        accentBlue    = PortfolioLightColors.accentBlue,
        accentGreen   = PortfolioLightColors.accentGreen,
        cardBg        = PortfolioLightColors.cardBg,
        divider       = PortfolioLightColors.divider
    )
}
