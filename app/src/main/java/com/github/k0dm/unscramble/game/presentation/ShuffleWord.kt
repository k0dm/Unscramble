package com.github.k0dm.unscramble.game.presentation

interface ShuffleWord {
    fun shuffle(text: String): String

    object Reversed : ShuffleWord {
        override fun shuffle(text: String) = text.reversed()
    }

    object Shuffled : ShuffleWord {
        override fun shuffle(text: String): String {
            val list = text.toCharArray()
            list.shuffle()
            return String(list)
        }
    }
}