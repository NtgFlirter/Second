package com.yashwant.model

data class HandCricketState(

    val playerScore: Int = 0,
    val aiScore: Int = 0,

    val playerChoice: Int? = null,
    val aiChoice: Int? = null,

    val isPlayerBatting: Boolean = true,

    val firstInningsCompleted: Boolean = false,

    val target: Int = 0,

    val gameOver: Boolean = false,

    val result: String = "",

    val eventMessage: String = "",
    val showEvent: Boolean = false
)