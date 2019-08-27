package ru.skillbranch.devintensive.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun initViews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initToolbar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
