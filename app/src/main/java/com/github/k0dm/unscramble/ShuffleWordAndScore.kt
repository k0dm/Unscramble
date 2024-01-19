package com.github.k0dm.unscramble

interface ShuffleWordAndScore {

    fun shuffledWord(): String

    fun checkIsTheSame(word: String): Boolean

    fun score(attempt: Int): Int

    object Empty : ShuffleWordAndScore {
        override fun shuffledWord() = ""
        override fun checkIsTheSame(word: String) = false
        override fun score(attempt: Int) = 0
    }
}