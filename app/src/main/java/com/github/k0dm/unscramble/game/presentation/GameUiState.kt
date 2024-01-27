package com.github.k0dm.unscramble.game.presentation

import android.view.View
import androidx.core.content.ContextCompat
import com.github.k0dm.unscramble.R
import com.github.k0dm.unscramble.databinding.FragmentGameBinding

interface GameUiState {

    fun show(binding: FragmentGameBinding)

    object Empty : GameUiState{
        override fun show(binding: FragmentGameBinding) = Unit
    }

    data class Initial(
        private val counter: String,
        private val score: String,
        private val shuffledWord: String,
    ) : GameUiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            mainLayout.show()
            counterTextView.text = counter
            shuffledWordTextView.text = shuffledWord
            textInputLayout.error = ""
            textInputLayout.isErrorEnabled = false
            textInputEditText.text = null

            submitButton.show()
            submitButton.isEnabled = false
            submitButton.setBackgroundColor(ContextCompat.getColor(
                submitButton.context,
                R.color.grey
            ))
            skipButton.setText(R.string.skip)
            skipButton.setTextColor(
                ContextCompat.getColor(
                    skipButton.context,
                    R.color.primary_color
                )
            )
            scoreTextView.text = score
        }
    }

    object NotReadyToSubmit : GameUiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            submitButton.isEnabled = false
            submitButton.setBackgroundColor(ContextCompat.getColor(
                submitButton.context,
                R.color.grey
            ))
            textInputLayout.isHintEnabled = false
        }
    }

    object ReadyToSubmit : GameUiState {
        override fun show(binding: FragmentGameBinding)= with(binding) {
            submitButton.enable()
            submitButton.setBackgroundColor(ContextCompat.getColor(
                submitButton.context,
                R.color.primary_color
            ))
        }
    }

    object Error : GameUiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            textInputLayout.error = textInputLayout.context.getString(R.string.wrong_answer)
            textInputLayout.isErrorEnabled = true
        }
    }

    data class GameOver(private val score: String) : GameUiState {
        override fun show(binding: FragmentGameBinding) = with(binding) {
            mainLayout.visibility = View.GONE
            textInputLayout.error = ""
            textInputLayout.isErrorEnabled = false
            submitButton.hide()
            skipButton.setText(R.string.restart_game)
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