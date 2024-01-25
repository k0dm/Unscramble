package com.github.k0dm.unscramble.core

import androidx.lifecycle.ViewModel
import com.github.k0dm.unscramble.creategame.presentation.CreatedGameViewModel
import com.github.k0dm.unscramble.creategame.data.WordsRepository
import com.github.k0dm.unscramble.creategame.data.WordsService
import com.github.k0dm.unscramble.creategame.domain.GameInteractor
import com.github.k0dm.unscramble.creategame.presentation.LiveDataWrapper
import com.github.k0dm.unscramble.game.presentation.GameViewModel
import com.github.k0dm.unscramble.game.presentation.ShuffleWord
import com.github.k0dm.unscramble.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(clazz: Class<out T>): T

    class Factory : ProvideViewModel {

        private val viewModelMap = mutableMapOf<Class<out ViewModel>, ViewModel>()

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
        private val navigation = LiveDataWrapper.Navigation()

        override fun <T : ViewModel> viewModel(clazz: Class<out T>): T {
            return if (viewModelMap.contains(clazz)) {
                viewModelMap[clazz] as T
            } else {
                val viewModel = when (clazz) {
                    MainViewModel::class.java -> MainViewModel(navigation)

                    CreatedGameViewModel::class.java -> CreatedGameViewModel(
                        navigation, interactor
                    )

                    GameViewModel::class.java -> GameViewModel(
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