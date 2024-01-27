package com.github.k0dm.unscramble.creategame.domain

import com.github.k0dm.unscramble.creategame.presentation.CreateGameUiState
import com.github.k0dm.unscramble.creategame.presentation.CreateGameUiStateObservable

interface GameResult {

    fun isCreated(): Boolean

    fun showError(observable: CreateGameUiStateObservable) = Unit

    object Success : GameResult {
        override fun isCreated() = true
    }

    data class Error(private val message: String) : GameResult {
        override fun isCreated() = false

        override fun showError(observable: CreateGameUiStateObservable) {
            observable.update(CreateGameUiState.Error(message))
        }
    }
}