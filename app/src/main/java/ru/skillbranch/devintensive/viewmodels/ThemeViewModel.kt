package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ThemeViewModel: ViewModel() {
    private val repository: PreferencesRepository = PreferencesRepository
    private val appTheme = MutableLiveData<Int>()

    init {
        Log.d("M_ThemeViewModel", "init")
        appTheme.value = repository.getAppTheme()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ThemeViewModel", "on_cleared")
    }

    fun getTheme(): LiveData<Int> = appTheme

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

}