package com.github.k0dm.unscramble

interface ShuffleWord {
    fun shuffle(text: String): String

    object Reversed : ShuffleWord {
        override fun shuffle(text: String) = text.reversed()
    }

    object Shuffled : ShuffleWord {
        override fun shuffle(text: String) = text.toCharArray().shuffle().toString()
    }
}