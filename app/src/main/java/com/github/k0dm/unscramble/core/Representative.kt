package com.github.k0dm.unscramble.core

interface Representative<T> {

    fun startGettingUpdates(observer: UiObserver<T>)

    fun stopGettingUpdates(observer: UiObserver<T> = UiObserver.Empty())
}