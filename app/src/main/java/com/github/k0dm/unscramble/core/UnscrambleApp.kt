package com.github.k0dm.unscramble.core

import android.app.Application

class UnscrambleApp: Application(), ProvideViewModel{

    private lateinit var factory: ProvideViewModel
    override fun onCreate() {
        super.onCreate()
        factory = ProvideViewModel.Factory(this)
    }
    override fun <T : Representative<*>> viewModel(clazz: Class<out T>): T  = factory.viewModel(clazz)
}