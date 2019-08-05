package ru.skillbranch.devintensive.viewmodels

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository


class ProfileViewModel: ViewModel() {
    private val repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val defaultDrawable = MutableLiveData<Drawable>()

    init {
        Log.d("M_ProfileViewModel", "init")
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()



        makeDefaultDrawable(profileData.value as Profile)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ProfileViewModel", "on_cleared")
    }

    fun getProfileData():LiveData<Profile>  = profileData

    fun getTheme():LiveData<Int> = appTheme

    fun getDefaultDrawable():LiveData<Drawable> = defaultDrawable

    fun saveProfileData(profile:Profile) {
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
        makeDefaultDrawable(profileData.value as Profile)
    }

    private fun makeDefaultDrawable(profile: Profile) {
        val initials = profile.initials
        if (initials.isNullOrEmpty()) return
        else {
            if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
                defaultDrawable.value = repository.getInitialsDrawable(profile, App.applicationContext().getColor(R.color.color_accent_night))
            } else {
                defaultDrawable.value = repository.getInitialsDrawable(profile,  App.applicationContext().getColor(R.color.color_accent))
            }

        }

    }


}