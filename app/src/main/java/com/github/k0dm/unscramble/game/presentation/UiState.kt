package com.github.k0dm.unscramble.game.presentation

import android.view.View
import androidx.core.content.ContextCompat
import com.github.k0dm.unscramble.R
import com.github.k0dm.unscramble.databinding.FragmentGameBinding

interface UiState {

    fun show(binding: FragmentGameBinding)

    object Empty : UiState{
        override fun show(binding: FragmentGameBinding) = Unit
    }

    data class Initial(
        private val counter: String,
        private val score: String,
        private val shuffledWord: String,
    ) : UiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            mainLayout.visibility = View.VISIBLE
            counterTextView.text = counter
            shuffledWordTextView.text = shuffledWord
            textInputLayout.error = ""
            textInputLayout.isErrorEnabled = false
            textInputEditText.text = null

            submitButton.visibility = View.VISIBLE
            submitButton.isEnabled = false
            submitButton.setBackgroundColor(ContextCompat.getColor(
                submitButton.context,
                R.color.grey
            ))
            skipButton.text = "Skip"
            skipButton.setTextColor(
                ContextCompat.getColor(
                    skipButton.context,
                    R.color.primary_color
                )
            )
            scoreTextView.text = score
        }
    }

    object NotReadyToSubmit : UiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            submitButton.isEnabled = false
            submitButton.setBackgroundColor(ContextCompat.getColor(
                submitButton.context,
                R.color.grey
            ))
            textInputLayout.isHintEnabled = false
        }
    }

    object ReadyToSubmit : UiState {
        override fun show(binding: FragmentGameBinding)= with(binding) {
            submitButton.isEnabled = true
            submitButton.setBackgroundColor(ContextCompat.getColor(
                submitButton.context,
                R.color.primary_color
            ))
        }
    }

    object Error : UiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            textInputLayout.error = "Wrong answer"
            textInputLayout.isErrorEnabled = true
        }
    }

    data class GameOver(private val score: String) : UiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            mainLayout.visibility = View.GONE
            textInputLayout.error = ""
            textInputLayout.isErrorEnabled = false
            submitButton.visibility = View.GONE
            skipButton.text = "Restart game"
            skipButton.setTextColor(
                ContextCompat.getColor(
                    skipButton.context,
                    R.color.red
                )
            )
            scoreTextView.text = score
        }
    }
}