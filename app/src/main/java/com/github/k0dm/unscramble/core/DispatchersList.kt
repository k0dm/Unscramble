package com.github.k0dm.unscramble.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersList{

    fun ui(): CoroutineDispatcher

    fun background(): CoroutineDispatcher

    class Base: DispatchersList {
        override fun ui() = Dispatchers.Main

        override fun background() = Dispatchers.IO
    }
}