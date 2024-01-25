package com.github.k0dm.unscramble.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.k0dm.unscramble.creategame.presentation.CreateGameScreen
import com.github.k0dm.unscramble.creategame.presentation.LiveDataWrapper
import com.github.k0dm.unscramble.creategame.presentation.ObserveNavigation

class MainViewModel(
    private val navigation: LiveDataWrapper.NavigationMutable
) : ViewModel(), ObserveNavigation {

    fun init() {
        // TODO: restore screen after death and progress
        navigation.update(CreateGameScreen)
    }

    override fun navigationLiveData() = navigation.navigationLiveData()
}