package com.github.k0dm.unscramble.creategame.domain

import com.github.k0dm.unscramble.game.presentation.GameUiState
import com.github.k0dm.unscramble.game.presentation.ShuffleWord

interface GameSession {

    fun isTheSameWord(word: String): Boolean
    fun calculateScore()
    fun attempt()
    fun finishGame()
    fun nextWord()
    fun isLastWord(): Boolean
    fun isGameOver(): Boolean
    fun map(uiMapper: UiMapper): GameUiState

    data class Base(
        private val words: List<String>,
        private val maxWords: Int,
        private val shuffleWord: ShuffleWord
    ) : GameSession {
        private var currentWord = words[0]
        private var currentIndex = 0
        private var attemts = 0
        private var score = 0
        private var isFinished = false

        override fun isTheSameWord(word: String): Boolean = word == currentWord

        override fun calculateScore() {
            score += if (attemts == 0) 20 else 10
        }

        override fun attempt() {
            attemts++
        }

        override fun finishGame() {
            isFinished = true
        }

        override fun nextWord() {
            attemts = 0
            currentWord = words[++currentIndex]
        }

        override fun isLastWord(): Boolean = currentIndex == words.lastIndex

        override fun isGameOver() = isFinished

        override fun map(uiMapper: UiMapper): GameUiState = uiMapper.map(
            shuffledWord = shuffleWord.shuffle(currentWord),
            counter = "${currentIndex + 1}/$maxWords",
            score = "$score"
        )

    }

    object Empty : GameSession {
        override fun isTheSameWord(word: String) = false
        override fun calculateScore() = Unit
        override fun attempt() = Unit
        override fun finishGame() = Unit
        override fun nextWord() = Unit
        override fun isLastWord() = false
        override fun isGameOver(): Boolean = false
        override fun map(uiMapper: UiMapper): GameUiState = uiMapper.map("", "", "")
    }
}

interface UiMapper {

    fun map(shuffledWord: String, counter: String, score: String): GameUiState

    object ToInitial : UiMapper {
        override fun map(shuffledWord: String, counter: String, score: String): GameUiState {
            return GameUiState.Initial(counter, score, shuffledWord)
        }
    }

    data class IsReadyToSubmit(private val text: String) : UiMapper {
        override fun map(shuffledWord: String, counter: String, score: String): GameUiState {
            return if (text.length == shuffledWord.length) {
                GameUiState.ReadyToSubmit
            } else {
                GameUiState.NotReadyToSubmit
            }
        }
    }

    object GameOver : UiMapper {
        override fun map(shuffledWord: String, counter: String, score: String): GameUiState {
            return GameUiState.GameOver(score)
        }
    }

    object Error : UiMapper {
        override fun map(shuffledWord: String, counter: String, score: String): GameUiState {
            return GameUiState.Error
        }
    }
}