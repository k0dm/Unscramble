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
import com.github.k0dm.unscramble.core.UiObserver
import com.github.k0dm.unscramble.databinding.FragmentCreateGameBinding

class CreateGameFragment : Fragment() {

    private var binding: FragmentCreateGameBinding? = null
    private lateinit var viewModel: CreateGameViewModel
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
        viewModel = (requireActivity() as ProvideViewModel).viewModel(CreateGameViewModel::class.java)

        startGameButton.setOnClickListener {
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

        if (savedInstanceState == null){
            viewModel.init()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : UiObserver<CreateGameUiState>{
            override fun update(data: CreateGameUiState) {
                data.show(binding!!)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}