package com.github.k0dm.unscramble.creategame.presentation

import com.github.k0dm.unscramble.core.DispatchersList
import com.github.k0dm.unscramble.core.Init
import com.github.k0dm.unscramble.core.Representative
import com.github.k0dm.unscramble.core.RunAsync
import com.github.k0dm.unscramble.core.UiObserver
import com.github.k0dm.unscramble.creategame.domain.GameInteractor
import com.github.k0dm.unscramble.game.presentation.GameScreen
import com.github.k0dm.unscramble.main.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface CreateGameViewModel : Representative<CreateGameUiState>, Init {

    fun createGame(numberOfWords: Int)

    class Base(
        runAsync: RunAsync,
        private val navigation: Navigation.Navigate,
        private val interactor: GameInteractor.CreateGame,
        private val createGameUiStateObservable: CreateGameUiStateObservable = CreateGameUiStateObservable.Base(),
    ) : CreateGameViewModel, Representative.Abstract<CreateGameUiState>(runAsync) {

        override fun init() = createGameUiStateObservable.update(CreateGameUiState.Initial)

        override fun createGame(numberOfWords: Int) {
            createGameUiStateObservable.update(CreateGameUiState.Loading)

            handleAsync({interactor.createGame(numberOfWords)}){ result->
                if (result.isCreated()) {
                    navigation.update(GameScreen)
                } else {
                    result.showError(createGameUiStateObservable)
                }
            }
        }

        override fun startGettingUpdates(observer: UiObserver<CreateGameUiState>) {
            createGameUiStateObservable.updateObserver(observer)
        }

        override fun stopGettingUpdates(observer: UiObserver<CreateGameUiState>) {
            createGameUiStateObservable.updateObserver(observer)
        }
    }
}
