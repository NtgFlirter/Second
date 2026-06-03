package com.yashwant.viewmodel

import androidx.lifecycle.ViewModel
import com.yashwant.model.PortfolioProfile
import com.yashwant.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _profile = MutableStateFlow(PortfolioProfile())
    val profile: StateFlow<PortfolioProfile> = _profile

    val skills = listOf(
        // Programming
        "Python", "Kotlin", "Java", "C", "C++", "JavaScript", "SQL" , "NOSQL",
        // AI/DS
        "Machine Learning", "NLP", "Pandas", "NumPy", "Scikit-learn",
        // Web & Bots
        "FastAPI", "MongoDB", "Telebot", "Pyrogram", "Telethon",
        // Tools
        "Jetpack Compose", "Firebase", "GitHub", "Unreal Engine"
    )

    val projects = listOf(
        Project(
            name = "Telegram Bots",
            description = "Hand Cricket, Story Mode & Management bots. 10K+ active users.",
            tech = "Python · Telebot · Pyrogram · MongoDB",
            gradientStart = 0xFF0E4D6B,
            gradientEnd = 0xFF061C28
        ),
        Project(
            name = "GreenLeaf AI",
            description = "Final year project — AI plant health diagnosis using ML.",
            tech = "Python · Streamlit · Scikit-learn · FastAPI",
            gradientStart = 0xFF1A5C2E,
            gradientEnd = 0xFF0A2614
        ),
        Project(
            name = "Anime Duel Website",
            description = "Official website for Anime Draft battle game.",
            tech = "Web · JavaScript · animeduel.in",
            gradientStart = 0xFF6B1A3A,
            gradientEnd = 0xFF2B0817
        ),
        Project(
            name = "Geldium AI Analytics",
            description = "AI-powered delinquency prediction — Tata iQ via Forage.",
            tech = "Python · GenAI · EDA · ML",
            gradientStart = 0xFF5B3A00,
            gradientEnd = 0xFF251800
        ),
        Project(
            name = "PokeClash Bot",
            description = "Telegram Pokémon battle bot with live matchmaking.",
            tech = "Python · Telebot · MongoDB",
            gradientStart = 0xFF2D0D6B,
            gradientEnd = 0xFF13062B
        ),
        Project(
            name = "Portfolio OS",
            description = "Retro BIOS-themed portfolio. YW-OS v2.0.",
            tech = "Next.js · TailwindCSS · Vercel",
            gradientStart = 0xFF003D4D,
            gradientEnd = 0xFF001A20
        )
    )

    val stats = listOf(
        "10K+" to "Bot Users",
        "6+" to "Projects",
        "50+" to "Repos",
        "2026" to "Graduate"
    )

    val experience = listOf(
        Triple("Android Developer", "Ubuy Technologies", "Current"),
        Triple("Bot Developer", "Freelance", "Jan 2023–Present"),
        Triple("GenAI Intern", "Tata iQ (Forage)", "May 2026"),
        Triple("Quant Research", "JP Morgan (Forage)", "Virtual")
    )
}