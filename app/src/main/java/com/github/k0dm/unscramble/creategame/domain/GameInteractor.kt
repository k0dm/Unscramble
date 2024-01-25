package com.github.k0dm.unscramble.creategame.domain

import com.github.k0dm.unscramble.creategame.data.WordsRepository
import com.github.k0dm.unscramble.game.presentation.ShuffleWord
import kotlinx.coroutines.delay

interface GameInteractor {

    interface CreateGame {
        suspend fun createGame(numberOfWords: Int): GameResult
    }

    interface StartGame {
        fun gameSession(): GameSession
    }

    interface Mutable : CreateGame, StartGame

    class Base(
        private val wordsRepository: WordsRepository,
        private val shuffleWord: ShuffleWord
    ) : Mutable {

        private var gameSession: GameSession = GameSession.Empty
        override suspend fun createGame(numberOfWords: Int): GameResult {
            return try {
                delay(1000)
                val words = wordsRepository.words(numberOfWords)
                gameSession = GameSession.Base(words, numberOfWords, shuffleWord)
                GameResult.Success
            } catch (e: Exception) {
                GameResult.Error(e.message.toString())
            }
        }

        override fun gameSession() = gameSession
    }
}

