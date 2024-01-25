package com.github.k0dm.unscramble.creategame.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.k0dm.unscramble.core.SingleLiveEvent
import com.github.k0dm.unscramble.main.Screen

interface LiveDataWrapper {

    interface Update<T> {
        fun update(data: T)
    }


    abstract class Abstract<T>() : Update<T> {
        protected val liveData = SingleLiveEvent<T>()

        override fun update(data: T) {
            liveData.value = data
        }
    }

    class CreatedGame : Abstract<CreateGameUiState>(), ObserveCreatedGameUiState {
        override fun createdGameUiStateLiveData() = liveData
    }

    interface NavigationMutable: Update<Screen>, ObserveNavigation
    class Navigation : Abstract<Screen>(), NavigationMutable  {
        override fun navigationLiveData() = liveData
    }
}

interface ObserveCreatedGameUiState {
    fun createdGameUiStateLiveData(): LiveData<CreateGameUiState>
}

interface ObserveNavigation {
    fun navigationLiveData(): LiveData<Screen>
}