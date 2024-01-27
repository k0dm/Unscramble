package com.github.k0dm.unscramble.core

import com.github.k0dm.unscramble.creategame.data.WordsRepository
import com.github.k0dm.unscramble.creategame.data.WordsService
import com.github.k0dm.unscramble.creategame.domain.GameInteractor
import com.github.k0dm.unscramble.creategame.presentation.CreateGameViewModel
import com.github.k0dm.unscramble.game.presentation.GameViewModel
import com.github.k0dm.unscramble.game.presentation.ShuffleWord
import com.github.k0dm.unscramble.main.MainViewModel
import com.github.k0dm.unscramble.main.Navigation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ProvideViewModel {

    fun <T : Representative<*>> viewModel(clazz: Class<out T>): T

    class Factory : ProvideViewModel {

        private val viewModelMap = mutableMapOf<Class<out Representative<*>>, Representative<*>>()

        private val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://random-word-api.herokuapp.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val interactor =
            GameInteractor.Base(
                WordsRepository.Base(retrofit.create(WordsService::class.java)),
                ShuffleWord.Shuffled
            )
        private val navigation = Navigation.Base()

        override fun <T : Representative<*>> viewModel(clazz: Class<out T>): T {
            return if (viewModelMap.contains(clazz)) {
                viewModelMap[clazz] as T
            } else {
                val viewModel = when (clazz) {
                    MainViewModel::class.java -> MainViewModel.Base(navigation)

                    CreateGameViewModel::class.java -> CreateGameViewModel.Base(
                        navigation, interactor
                    )

                    GameViewModel::class.java -> GameViewModel.Base(
                       navigation, interactor
                    )

                    else -> throw IllegalStateException("No such viewModel with class:$clazz")
                }
                viewModelMap[clazz] = viewModel
                viewModel as T
            }
        }
    }
}