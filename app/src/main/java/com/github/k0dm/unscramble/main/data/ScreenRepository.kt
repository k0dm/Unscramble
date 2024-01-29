package com.github.k0dm.unscramble.main.data

interface ScreenRepository {

    interface Read {
        fun shouldLoadNewGame(): Boolean
    }

    interface Save {
        fun saveGameCreated()
        fun saveGameFinished()
    }

    interface Mutable : Read, Save

    class Base(private val localStorage: LocalStorage) : Mutable {
        override fun shouldLoadNewGame() = localStorage.read(KEY, true)

        override fun saveGameCreated() = localStorage.save(KEY,false)


        override fun saveGameFinished() = localStorage.save(KEY, true)

        companion object {
            private const val KEY = "ShouldLoadNewGame"
        }
    }
}

