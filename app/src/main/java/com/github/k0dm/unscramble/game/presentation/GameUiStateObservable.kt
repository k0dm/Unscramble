package com.github.k0dm.unscramble.game.presentation

import com.github.k0dm.unscramble.core.UiObservable

interface GameUiStateObservable: UiObservable<GameUiState> {
    class Base: GameUiStateObservable, UiObservable.Abstract<GameUiState>(GameUiState.Empty)
}
