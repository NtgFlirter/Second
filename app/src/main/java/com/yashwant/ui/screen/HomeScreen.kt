package com.yashwant.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yashwant.ui.components.*
import com.yashwant.ui.theme.appColors
import com.yashwant.viewmodel.AppViewModel
import com.yashwant.viewmodel.HomeViewModel

// ─────────────────────────────────────────────────────────────
//  HomeScreen
//  • Observes isDarkTheme from AppViewModel → drives all colors
//  • Data comes from HomeViewModel
//  • LazyColumn with stable keys → NO SCROLL CRASH
// ─────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    appViewModel: AppViewModel,                       // injected from Activity/NavGraph
    homeViewModel: HomeViewModel = viewModel()
) {
    val isDark by appViewModel.isDarkTheme.collectAsState()
    val colors = appColors(isDark)                    // derives all colors from theme

    val profile  by homeViewModel.profile.collectAsState()
    val stats    = homeViewModel.stats
    val skills   = homeViewModel.skills
    val projects = homeViewModel.projects

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp,
            top = 16.dp, bottom = 40.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item(key = "header") {
            PortfolioHeader(profile, colors)
        }

        item(key = "contact") {
            ContactCards(profile, colors)
        }

        item(key = "github") {
            GithubCard(profile.github, profile.githubUrl, colors)
        }

        item(key = "portfolio") {
            PortfolioCard(profile.portfolio, profile.portfolioUrl, colors)
        }

        item(key = "stats") {
            SocialStatsRow(stats, colors)
        }

        item(key = "skills") {
            SkillsSection(skills, colors)
        }

        item(key = "projects") {
            FeaturedProjectsSection(projects, colors)
        }

        item(key = "bottom") {
            Spacer(Modifier.height(16.dp))
        }
    }
}