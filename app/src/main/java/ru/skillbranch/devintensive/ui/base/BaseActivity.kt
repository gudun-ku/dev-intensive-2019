package ru.skillbranch.devintensive.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel
import ru.skillbranch.devintensive.viewmodels.ThemeViewModel

open class BaseActivity: AppCompatActivity()  {

    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setTheme(R.style.AppTheme)
    }

    private fun initViewModel() {
        themeViewModel = ViewModelProviders.of(this).get(ThemeViewModel::class.java)
        themeViewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }
}