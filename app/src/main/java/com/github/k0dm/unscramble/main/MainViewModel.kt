package com.github.k0dm.unscramble.main

import com.github.k0dm.unscramble.core.Init
import com.github.k0dm.unscramble.core.Representative
import com.github.k0dm.unscramble.core.UiObserver
import com.github.k0dm.unscramble.creategame.presentation.CreateGameScreen

interface MainViewModel : Representative<Screen>, Init {

    fun notifyObserved()

    class Base(private val navigation: Navigation) : MainViewModel {

        override fun init() {
            // TODO: restore screen after death and progress
            navigation.update(CreateGameScreen)
        }

        override fun startGettingUpdates(observer: UiObserver<Screen>) {
            navigation.updateObserver(observer)
        }

        override fun stopGettingUpdates(observer: UiObserver<Screen>) {
            navigation.updateObserver(observer)
        }

        override fun notifyObserved() = navigation.clear()
    }
}

