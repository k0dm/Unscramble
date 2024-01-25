package com.github.k0dm.unscramble.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.github.k0dm.unscramble.R
import com.github.k0dm.unscramble.core.ProvideViewModel
import com.github.k0dm.unscramble.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = viewModel(MainViewModel::class.java)
        viewModel.navigationLiveData().observe(this){screen->
            screen.show(R.id.rootLayout, supportFragmentManager)
        }
        if (savedInstanceState == null) {
            viewModel.init()
        }
    }

    override fun <T : ViewModel> viewModel(clazz: Class<out T>): T {
        return (application as ProvideViewModel).viewModel(clazz)
    }
}