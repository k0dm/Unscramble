package com.github.k0dm.unscramble

data class WordWithScore(private val word: String, private val score: Int) : ShuffleWordAndScore {

    override fun shuffledWord() = word.reversed()

    override fun checkIsTheSame(word: String) = this.word == word

    override fun score(attempt: Int): Int {
        return  if (attempt == 0) {
            score
        } else {
            var currentScope = score
            repeat(attempt) {
                currentScope /= 2
            }
            currentScope
        }
    }
}