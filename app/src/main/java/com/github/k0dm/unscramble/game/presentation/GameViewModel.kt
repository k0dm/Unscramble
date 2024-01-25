package com.github.k0dm.unscramble.game.presentation

import androidx.lifecycle.ViewModel
import com.github.k0dm.unscramble.creategame.domain.GameSession
import com.github.k0dm.unscramble.creategame.domain.GameInteractor
import com.github.k0dm.unscramble.creategame.domain.UiMapper
import com.github.k0dm.unscramble.creategame.presentation.CreateGameScreen
import com.github.k0dm.unscramble.creategame.presentation.LiveDataWrapper
import com.github.k0dm.unscramble.main.Screen

class GameViewModel(
    private val navigation: LiveDataWrapper.Update<Screen>,
    private val gameInteractor: GameInteractor.StartGame,
    private val toInitialUiMapper: UiMapper = UiMapper.ToInitial,
    private val toGameOverMapper: UiMapper = UiMapper.GameOver,
    private val toErrorMapper: UiMapper = UiMapper.Error
): ViewModel() {

    private var gameSession: GameSession = GameSession.Empty

    fun init(): UiState {
        gameSession = gameInteractor.gameSession()
        return gameSession.map(toInitialUiMapper)
    }

    fun update(text: String): UiState = gameSession.map(UiMapper.IsReadyToSubmit(text))

    fun submit(word: String): UiState {
        return if (gameSession.isTheSameWord(word)) {
            gameSession.calculateScore()
            if (gameSession.isLastWord()) {
                gameSession.finishGame()
                gameSession.map(toGameOverMapper)
            } else {
                gameSession.nextWord()
                val uiState = gameSession.map(toInitialUiMapper)
                uiState
            }
        } else {
            gameSession.attempt()
            gameSession.map(toErrorMapper)
        }
    }

    fun skip(): UiState {
        return if (gameSession.isLastWord()) {
            if (gameSession.isGameOver()) {
                navigation.update(CreateGameScreen)
                UiState.Empty
            } else {
                gameSession.finishGame()
                gameSession.map(toGameOverMapper)
            }
        } else {
            gameSession.nextWord()
            gameSession.map(toInitialUiMapper)
        }
    }
}

