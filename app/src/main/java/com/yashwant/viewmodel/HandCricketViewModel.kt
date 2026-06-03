package com.yashwant.viewmodel

import androidx.lifecycle.ViewModel
import com.yashwant.model.HandCricketState
import kotlinx.coroutines.flow.MutableStateFlow

class HandCricketViewModel : ViewModel() {

    private val _state = MutableStateFlow(HandCricketState())
    val state = _state

    fun play(number: Int) {

        val current = _state.value

        // 🚨 BLOCK INPUT IF POPUP OR GAME OVER
        if (current.showEvent || current.gameOver) return

        val ai = (1..6).random()

        var playerScore = current.playerScore
        var aiScore = current.aiScore

        var isBatting = current.isPlayerBatting
        var firstDone = current.firstInningsCompleted
        var target = current.target

        var gameOver = false
        var eventMsg = ""
        var showEvent = false

        // ───── PLAYER BATTING ─────
        if (isBatting) {

            if (number == ai) {

                if (!firstDone) {

                    target = playerScore + 1
                    firstDone = true
                    isBatting = false

                    eventMsg = "1st Innings Over! Target: $target"
                    showEvent = true

                } else {

                    gameOver = true
                    isBatting = false

                    eventMsg =
                        if (playerScore > aiScore) "You Win 🎉"
                        else if (playerScore < aiScore) "AI Wins 🤖"
                        else "Match Tie"

                    showEvent = true
                }

            } else {
                playerScore += number
            }
        }

        // ───── AI BATTING ─────
        else {

            if (number == ai) {

                gameOver = true

                eventMsg =
                    if (playerScore > aiScore) "You Win 🎉"
                    else if (playerScore < aiScore) "AI Wins 🤖"
                    else "Match Tie"

                showEvent = true

            } else {

                aiScore += ai

                if (aiScore >= target && firstDone) {
                    gameOver = true
                    eventMsg = "AI Chased Target!"
                    showEvent = true
                }
            }
        }

        _state.value = current.copy(
            playerScore = playerScore,
            aiScore = aiScore,
            playerChoice = number,
            aiChoice = ai,
            isPlayerBatting = isBatting,
            firstInningsCompleted = firstDone,
            target = target,
            gameOver = gameOver,
            showEvent = showEvent,
            eventMessage = eventMsg
        )
    }

    fun hideEvent() {
        _state.value = _state.value.copy(
            showEvent = false,
            eventMessage = ""
        )
    }

    fun startSecondInnings() {
        _state.value = _state.value.copy(
            isPlayerBatting = false,
            showEvent = false,
            eventMessage = ""
        )
    }

    fun resetGame() {
        _state.value = HandCricketState()
    }
}