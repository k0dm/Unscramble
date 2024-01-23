package com.github.k0dm.unscramble

interface Interactor {

    fun gameSession(): GameSession

    class Base(
        private val repository: Repository,
        private val maxWords: Int,
        private val shuffleWord: ShuffleWord
    ) : Interactor {
        override fun gameSession(): GameSession {
            val words = repository.words(maxWords)
            return GameSession.Base(words,maxWords, shuffleWord)
        }
    }
}