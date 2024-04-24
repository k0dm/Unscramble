package com.github.k0dm.unscramble.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.k0dm.unscramble.R
import com.github.k0dm.unscramble.core.ProvideViewModel
import com.github.k0dm.unscramble.core.Representative
import com.github.k0dm.unscramble.core.UiObserver

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = viewModel(MainViewModel::class.java)

        if (savedInstanceState == null) {
            viewModel.init()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : UiObserver<Screen> {
            override fun update(data: Screen) {
                data.show(R.id.rootLayout, supportFragmentManager)
                viewModel.notifyObserved()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun <T : Representative<*>> viewModel(clazz: Class<out T>): T {
        return (application as ProvideViewModel).viewModel(clazz)
    }
}

