package com.github.k0dm.unscramble

interface Repository {

    fun wordWithScore(): WordWithScore

    class Base : Repository {

        private val listOfWords = listOf<WordWithScore>(
            WordWithScore(word = "animal", score = 20),
            WordWithScore(word = "auto", score = 20),
            WordWithScore(word = "anecdote", score = 40),
            WordWithScore(word = "hello", score = 20),
            WordWithScore(word = "dog", score = 10),
            WordWithScore(word = "forest", score = 20),
            WordWithScore(word = "keyboard", score = 30),
            WordWithScore(word = "laptop", score = 20),
            WordWithScore(word = "cat", score = 10),
            WordWithScore(word = "transparent", score = 50)
        )

        private var currentIndex = 0
        override fun wordWithScore(): WordWithScore {
            return if (currentIndex != listOfWords.lastIndex) {
                listOfWords[currentIndex++]
            } else {
                val word = listOfWords[currentIndex]
                currentIndex = 0
                word
            }
        }
    }
}

