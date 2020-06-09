package com.teltronic.app112.screens.configuration

import android.app.Activity
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.teltronic.app112.R
import com.teltronic.app112.classes.Preferences

class ConfigurationViewModel(activity: FragmentActivity?) : ViewModel() {
    private var _activity: FragmentActivity? = activity
    private var _boolSave = MutableLiveData<Boolean>()
    val boolSave: LiveData<Boolean>
        get() = _boolSave

    private var _posSelectedLanguage = MutableLiveData<Int>()
    val posSelectedLanguage: LiveData<Int>
        get() = _posSelectedLanguage

    var _currentDistanceId = MutableLiveData<Int>()
    val currentDistanceId: LiveData<Int>
        get() = _currentDistanceId

    val _strMessageDistance = MutableLiveData<String>()
    val strMessageDistance: LiveData<String>
        get() = _strMessageDistance

    private var _coordinates = MutableLiveData<LatLng>()
    val coordinates: LiveData<LatLng>
        get() = _coordinates

    fun save() {
        _boolSave.value = true
    }

    private fun saveComplete() {
        _boolSave.value = false
    }

    init {
        _boolSave.value = false
        _posSelectedLanguage.value = -1
        _currentDistanceId.value= 6 // TODO borrar esto, debe traer de configuration dao
        getLastLocation()
    }

    private fun getLastLocation() {
        val mFusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(_activity as Activity)
        mFusedLocationClient.lastLocation.addOnCompleteListener(_activity as Activity) { task ->
            val location: Location? = task.result
            if (location != null) {
                _coordinates.value = LatLng(location.latitude, location.longitude)
            }
        }
    }


    fun loadConfigurations(activity: FragmentActivity) {
        val resourcesActivity = activity.resources

        val keyLanguage = resourcesActivity.getString(R.string.KEY_LANGUAGE)
        val nameSettingsPreferences =
            resourcesActivity.getString(R.string.name_settings_preferences)
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