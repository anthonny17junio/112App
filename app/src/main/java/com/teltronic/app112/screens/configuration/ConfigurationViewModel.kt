package com.teltronic.app112.screens.configuration

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teltronic.app112.R
import com.teltronic.app112.classes.Preferences

class ConfigurationViewModel : ViewModel() {
    private var _boolSave = MutableLiveData<Boolean>()
    val boolSave: LiveData<Boolean>
        get() = _boolSave

    private var _posSelectedLanguage = MutableLiveData<Int>()
    val posSelectedLanguage: LiveData<Int>
        get() = _posSelectedLanguage

    fun save() {
        _boolSave.value = true
    }

    private fun saveComplete() {
        _boolSave.value = false
    }

    init {
        _boolSave.value = false
        _posSelectedLanguage.value = -1
    }

    fun loadConfigurations(activity: FragmentActivity) {
        val resourcesActivity = activity.resources

        val keyLanguage = resourcesActivity.getString(R.string.KEY_LANGUAGE)
        val nameSettingsPreferences = resourcesActivity.getString(R.string.name_settings_preferences)
        val sharedPreferences =
            activity.getSharedPreferences(nameSettingsPreferences, Activity.MODE_PRIVATE)
        val language = sharedPreferences?.getString(keyLanguage, "")

        if (language != "") {
            val langCodes = resourcesActivity.getStringArray(R.array.languages_values)
            for ((index, langCode) in langCodes.withIndex()) {
                if (langCode == language) {
                    _posSelectedLanguage.value = index
                    break
                }
            }
        }
    }

    fun saveConfigurations(fragment: ConfigurationFragment) {
        val binding = fragment.binding
        val activity = fragment.activity
        val positionNewLanguage = binding.spinnerLanguage.selectedItemPosition
        val langCode =
            activity!!.resources.getStringArray(R.array.languages_values)[positionNewLanguage]

        Preferences.setLocate(langCode, activity as Context)
        saveComplete()
        activity.recreate()
        Toast.makeText(
            activity,
            activity.resources.getString(R.string.changes_saved),
            Toast.LENGTH_LONG
        ).show()
    }

}