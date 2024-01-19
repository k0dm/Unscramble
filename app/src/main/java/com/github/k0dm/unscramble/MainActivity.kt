 package com.github.k0dm.unscramble

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.github.k0dm.unscramble.databinding.ActivityMainBinding

 class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = GameViewModel(Repository.Base())
        binding.submitButton.setOnClickListener{
            val uiState = viewModel.submit(binding.textInputEditText.text.toString())
            uiState.show(binding)
        }
        binding.skipButton.setOnClickListener {
            val uiState = viewModel.skip()
            uiState.show(binding)
        }
        binding.textInputEditText.doAfterTextChanged {
            val uiState = viewModel.update(binding.textInputEditText.text.toString())
            uiState.show(binding)
        }
        val uiState = viewModel.init()
        uiState.show(binding)
    }
}