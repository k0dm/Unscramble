package com.github.k0dm.unscramble.main

import com.github.k0dm.unscramble.core.Init
import com.github.k0dm.unscramble.core.Representative
import com.github.k0dm.unscramble.core.UiObserver
import com.github.k0dm.unscramble.creategame.presentation.CreateGameScreen
import com.github.k0dm.unscramble.game.presentation.GameScreen
import com.github.k0dm.unscramble.main.data.ScreenRepository

interface MainViewModel : Representative<Screen>, Init {

    fun notifyObserved()

    class Base(
        private val navigation: Navigation.Mutable,
        private val screenRepository: ScreenRepository.Read
    ) : MainViewModel {

        override fun init() {
            if(screenRepository.shouldLoadNewGame()){
                navigation.update(CreateGameScreen)
            }else{
                navigation.update(GameScreen)
            }
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

