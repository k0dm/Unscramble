package com.github.k0dm.unscramble.creategame.presentation

import com.github.k0dm.unscramble.core.UiObservable

interface CreateGameUiStateObservable : UiObservable<CreateGameUiState> {
    class Base : CreateGameUiStateObservable,
        UiObservable.Abstract<CreateGameUiState>(CreateGameUiState.Empty)
}