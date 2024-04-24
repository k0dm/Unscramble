package com.github.k0dm.unscramble.game.presentation

interface ShuffleWord {
    fun shuffle(text: String): String

    class Reversed : ShuffleWord {
        override fun shuffle(text: String) = text.reversed()
    }

    class Shuffled : ShuffleWord {
        override fun shuffle(text: String): String {
            val list = text.toCharArray()
            list.shuffle()
            return String(list)
        }
    }
}