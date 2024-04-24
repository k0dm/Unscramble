package com.github.k0dm.unscramble.game.presentation

import com.github.k0dm.unscramble.core.Init
import com.github.k0dm.unscramble.core.Representative
import com.github.k0dm.unscramble.core.UiObserver
import com.github.k0dm.unscramble.creategame.domain.GameInteractor
import com.github.k0dm.unscramble.creategame.domain.GameSession
import com.github.k0dm.unscramble.creategame.domain.UiMapper
import com.github.k0dm.unscramble.creategame.presentation.CreateGameScreen
import com.github.k0dm.unscramble.main.Navigation

interface GameViewModel: Representative<GameUiState>, Init {

    fun update(text: String)

    fun submit(word: String)

    fun skip()

    class Base(
        private val navigation: Navigation.Navigate,
        private val gameInteractor: GameInteractor.GameInteraction,
        private val gameUiStateObservable: GameUiStateObservable = GameUiStateObservable.Base(),
        private val toInitialUiMapper: UiMapper = UiMapper.ToInitial,
        private val toGameOverMapper: UiMapper = UiMapper.GameOver,
        private val toErrorMapper: UiMapper = UiMapper.Error
    ) : GameViewModel {

        private var gameSession: GameSession = GameSession.Empty

        override fun init() {
            gameSession = gameInteractor.gameSession()
            gameUiStateObservable.update(gameSession.map(toInitialUiMapper))
        }

        override fun update(text: String) {
            gameUiStateObservable.update(gameSession.map(UiMapper.IsReadyToSubmit(text)))
        }

        override fun submit(word: String) {
            val uiState = if (gameSession.isTheSameWord(word)) {
                gameSession.calculateScore()
                if (gameSession.isLastWord()) {
                    gameSession.finishGame()
                    gameSession.map(toGameOverMapper)
                } else {
                    gameSession.nextWord()
                    gameSession.map(toInitialUiMapper)
                }
            } else {
                gameSession.attempt()
                gameSession.map(toErrorMapper)
            }
            gameUiStateObservable.update(uiState)
        }

        override fun skip() {
            val uiState = if (gameSession.isLastWord()) {
                if (gameSession.isGameOver()) {
                    navigation.update(CreateGameScreen)
                    GameUiState.Empty
                } else {
                    gameInteractor.finishGame()
                    gameSession.finishGame()
                    gameSession.map(toGameOverMapper)
                }
            } else {
                gameSession.nextWord()
                gameSession.map(toInitialUiMapper)
            }
            gameUiStateObservable.update(uiState)
        }

        override fun startGettingUpdates(observer: UiObserver<GameUiState>) {
            gameUiStateObservable.updateObserver(observer)
        }

        override fun stopGettingUpdates(observer: UiObserver<GameUiState>) {
            gameUiStateObservable.updateObserver(observer)
            gameInteractor.saveGame()
        }
    }
}