package com.github.k0dm.unscramble.creategame.presentation

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.k0dm.unscramble.R
import com.github.k0dm.unscramble.core.DispatchersList
import com.github.k0dm.unscramble.creategame.domain.GameInteractor
import com.github.k0dm.unscramble.databinding.FragmentCreateGameBinding
import com.github.k0dm.unscramble.game.presentation.GameScreen
import com.github.k0dm.unscramble.game.presentation.UiState
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatedGameViewModel(
    private val navigation: LiveDataWrapper.Navigation,
    private val interactor: GameInteractor.CreateGame,
    private val uiStateLiveDataWrapper: LiveDataWrapper.Update<CreateGameUiState> = LiveDataWrapper.CreatedGame(),
    private val dispatcherList: DispatchersList = DispatchersList.Base()
) : ViewModel(),  ObserveCreatedGameUiState {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun init() = uiStateLiveDataWrapper.update(CreateGameUiState.Initial)

    fun createGame(numberOfWords: Int) {
        uiStateLiveDataWrapper.update(CreateGameUiState.Loading)
        viewModelScope.launch(dispatcherList.background()) {
            val result = interactor.createGame(numberOfWords)
            withContext(dispatcherList.ui()) {
                if (result.isCreated()) {
                    navigation.update(GameScreen)
                } else {
                    result.showError(uiStateLiveDataWrapper)
                }
            }
        }
    }

    override fun createdGameUiStateLiveData() =
        (uiStateLiveDataWrapper as ObserveCreatedGameUiState).createdGameUiStateLiveData()
}

interface CreateGameUiState {

    fun show(binding: FragmentCreateGameBinding)

    object Initial : CreateGameUiState {
        override fun show(binding: FragmentCreateGameBinding) = with(binding) {
            mainLayout.visibility = View.VISIBLE
            startGameButton.visibility = View.VISIBLE
            startGameButton.setBackgroundColor(
                ContextCompat.getColor(
                    startGameButton.context,
                    R.color.primary_color
                )
            )
            errorTextView.visibility = View.GONE
            progressBarr.visibility = View.GONE
            loadingTextView.visibility = View.GONE
            slider.visibility = View.VISIBLE
            retryButton.visibility = View.GONE
        }
    }

    object Loading : CreateGameUiState {
        override fun show(binding: FragmentCreateGameBinding) = with(binding) {
            mainLayout.visibility = View.GONE
            startGameButton.visibility = View.GONE
            errorTextView.visibility = View.GONE
            progressBarr.visibility = View.VISIBLE
            loadingTextView.visibility = View.VISIBLE
            slider.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : CreateGameUiState {
        override fun show(binding: FragmentCreateGameBinding) = with(binding) {
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
            progressBarr.visibility = View.GONE
            loadingTextView.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
        }
    }
}