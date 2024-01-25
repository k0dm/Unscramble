package com.github.k0dm.unscramble.game.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.github.k0dm.unscramble.core.ProvideViewModel
import com.github.k0dm.unscramble.core.UnscrambleApp
import com.github.k0dm.unscramble.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var binding: FragmentGameBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding!!) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (requireActivity() as ProvideViewModel).viewModel(GameViewModel::class.java)
        Log.d("k0dm", "bundle is null ${savedInstanceState == null}")
        submitButton.setOnClickListener {
            val uiState = viewModel.submit(textInputEditText.text.toString())
            uiState.show(this)
        }
        skipButton.setOnClickListener {
            val uiState = viewModel.skip()
            uiState.show(this)
        }
        textInputEditText.doAfterTextChanged {
            val uiState = viewModel.update(textInputEditText.text.toString())
            uiState.show(this)
        }
        if (savedInstanceState == null) {
            val uiState = viewModel.init()
            uiState.show(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}