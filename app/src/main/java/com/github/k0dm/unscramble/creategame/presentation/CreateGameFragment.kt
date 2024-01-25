package com.github.k0dm.unscramble.creategame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.github.k0dm.unscramble.R
import com.github.k0dm.unscramble.core.ProvideViewModel
import com.github.k0dm.unscramble.databinding.FragmentCreateGameBinding

class CreateGameFragment : Fragment() {

    private var binding: FragmentCreateGameBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateGameBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding!!) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel =
            (requireActivity() as ProvideViewModel).viewModel(CreatedGameViewModel::class.java)

        startGameButton.setOnClickListener{
            viewModel.createGame(textInputEditText.text.toString().toInt())
        }

        textInputEditText.doAfterTextChanged {
            val number = it.toString().let { str ->
                if (str.isBlank()) 0 else str.toInt()
            }
            if (number in 2..20) {
                slider.value = number.toFloat()
                startGameButton.isEnabled = true
                textInputLayout.isErrorEnabled = false
                startGameButton.setBackgroundColor(
                    ContextCompat.getColor(
                        startGameButton.context,
                        R.color.primary_color
                    )
                )
            } else {
                startGameButton.isEnabled = false
                textInputLayout.error = "Min 2 Max 20"
                textInputLayout.isErrorEnabled = true
                startGameButton.setBackgroundColor(
                    ContextCompat.getColor(
                        startGameButton.context,
                        R.color.grey
                    )
                )
            }
        }

        slider.addOnChangeListener { _, value, _ ->
            textInputEditText.setText(value.toInt().toString())
        }

        viewModel.createdGameUiStateLiveData().observe(viewLifecycleOwner){uiState->
            uiState.show(this)
        }

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}