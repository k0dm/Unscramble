package com.github.k0dm.unscramble.game.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.github.k0dm.unscramble.core.ProvideViewModel
import com.github.k0dm.unscramble.core.UiObserver
import com.github.k0dm.unscramble.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var binding: FragmentGameBinding? = null
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding!!) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as ProvideViewModel).viewModel(GameViewModel::class.java)

        submitButton.setOnClickListener {
            viewModel.submit(textInputEditText.text.toString())
        }
        skipButton.setOnClickListener {
            viewModel.skip()
        }
        textInputEditText.doAfterTextChanged {
            viewModel.update(textInputEditText.text.toString())
        }
        if (savedInstanceState == null) {
            viewModel.init()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : UiObserver<GameUiState> {
            override fun update(data: GameUiState) {
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