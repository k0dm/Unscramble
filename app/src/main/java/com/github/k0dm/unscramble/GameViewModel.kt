package com.github.k0dm.unscramble

class GameViewModel(
    private val gameInteractor: Interactor,
    private val toInitialUiMapper: UiMapper = UiMapper.ToInitial,
    private val toGameOverMapper: UiMapper = UiMapper.GameOver,
    private val toErrorMapper: UiMapper = UiMapper.Error
) {

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
               init()
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

