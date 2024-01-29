package com.github.k0dm.unscramble.creategame.domain

import com.github.k0dm.unscramble.creategame.data.GameRepository
import com.github.k0dm.unscramble.creategame.data.WordsRepository
import com.github.k0dm.unscramble.game.presentation.ShuffleWord
import com.github.k0dm.unscramble.main.data.ScreenRepository
import kotlinx.coroutines.delay

interface GameInteractor {

    interface CreateGame {
        suspend fun createGame(numberOfWords: Int): GameResult
    }

    interface GameInteraction {
        fun gameSession(): GameSession
        fun finishGame()
        fun saveGame()
    }

    interface Mutable : CreateGame, GameInteraction

    class Base(
        private val wordsRepository: WordsRepository,
        private val screenRepository: ScreenRepository.Save,
        private val gameRepository: GameRepository,
        private val shuffleWord: ShuffleWord
    ) : Mutable {

        private var gameSession: GameSession = GameSession.Empty

        override suspend fun createGame(numberOfWords: Int): GameResult {
            return try {
                delay(1000)
                val words = wordsRepository.words(numberOfWords)
                gameSession = GameSession.Base(words, numberOfWords, shuffleWord)
                screenRepository.saveGameCreated()
                saveGame()
                GameResult.Success
            } catch (e: Exception) {
                GameResult.Error(e.message.toString())
            }
        }

        override fun gameSession(): GameSession {
            gameSession = gameRepository.restoreGame()
            return gameSession
        }

        override fun finishGame() {
            gameSession.finishGame()
            screenRepository.saveGameFinished()
        }

        override fun saveGame() {
            gameRepository.saveGameSession(gameSession)
        }
    }
}

