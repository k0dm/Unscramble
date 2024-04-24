package com.github.k0dm.unscramble.creategame.presentation

import android.view.View
import androidx.core.content.ContextCompat
import com.github.k0dm.unscramble.R
import com.github.k0dm.unscramble.databinding.FragmentCreateGameBinding

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

    object Empty: CreateGameUiState {
        override fun show(binding: FragmentCreateGameBinding) = Unit
    }
}