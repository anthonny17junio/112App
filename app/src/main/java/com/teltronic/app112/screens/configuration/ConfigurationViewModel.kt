package com.teltronic.app112.screens.configuration

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
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
import com.teltronic.app112.classes.enums.DistanceValues
import com.teltronic.app112.classes.services.ListenNewNoticesService
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.configurations.ConfigurationsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfigurationViewModel(activity: FragmentActivity?) : ViewModel() {
    private var _activity: FragmentActivity? = activity
    private var _boolSave = MutableLiveData<Boolean>()
    val boolSave: LiveData<Boolean>
        get() = _boolSave

    private var _posSelectedLanguage = MutableLiveData<Int>()
    val posSelectedLanguage: LiveData<Int>
        get() = _posSelectedLanguage

    var currentDistanceIdLiveData = MutableLiveData<Int>()
    val currentDistanceId: LiveData<Int>
        get() = currentDistanceIdLiveData

    val strMessageDistanceLiveData = MutableLiveData<String>()
    val strMessageDistance: LiveData<String>
        get() = strMessageDistanceLiveData

    private var _coordinates = MutableLiveData<LatLng>()
    val coordinates: LiveData<LatLng>
        get() = _coordinates

    private val dataSourceConfigurations =
        DatabaseApp.getInstance(_activity!!.application).configurationsDao

    var configurations: LiveData<ConfigurationsEntity?> = dataSourceConfigurations.getLiveData()

    fun save() {
        _boolSave.value = true
    }

    fun saveComplete() {
        _boolSave.value = false
    }

    init {
        _boolSave.value = false
        _posSelectedLanguage.value = -1
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

    fun setConfigurations(confRoom: ConfigurationsEntity?) {
        if (confRoom == null)
            currentDistanceIdLiveData.value = DistanceValues.NO_LIMIT.id
        else {
            currentDistanceIdLiveData.value = confRoom.distance_code_notices
            _activity?.let { setLangPosition(it, confRoom.lang_code) }
        }
    }

    private fun setLangPosition(activity: FragmentActivity, language: String) {
        val resourcesActivity = activity.resources
        val langCodes = resourcesActivity.getStringArray(R.array.languages_values)
        for ((index, langCode) in langCodes.withIndex()) {
            if (langCode == language) {
                _posSelectedLanguage.value = index
                break
            }
        }
    }


    suspend fun getConfigurationsRoom() {
        withContext(Dispatchers.IO) {
            configurations = dataSourceConfigurations.getLiveData()
        }
    }

    suspend fun saveConfigurations(newLang: String, distanceIdCode: Int) {
        val coordinates = _coordinates.value
        if (coordinates != null) {
            val lat = coordinates.latitude
            val long = coordinates.longitude

            val configurationsEntity =
                ConfigurationsEntity(1, newLang, lat, long, distanceIdCode)

            withContext(Dispatchers.IO) {
                val confTemp = dataSourceConfigurations.get()
                if (confTemp == null)
                    dataSourceConfigurations.insert(configurationsEntity)
                else
                    dataSourceConfigurations.update(configurationsEntity)

                Preferences.setLocate(newLang, _activity as Context)
                _activity?.runOnUiThread {
                    _activity?.recreate()
                    Toast.makeText(_activity, R.string.changes_saved, Toast.LENGTH_LONG).show()
                    //Inicia el servicio para notficar avisos
                    _activity!!.stopService(Intent(_activity!!, ListenNewNoticesService::class.java))
                    _activity!!.startService(Intent(_activity!!, ListenNewNoticesService::class.java))
//                    if (_activity != null) { todo descomentar esto
//                        _activity!!.stopService(
//                            Intent(
//                                _activity!!,
//                                ListenNewNoticesService::class.java
//                            )
//                        )
//                        Intent(_activity!!, ListenNewNoticesService::class.java).also { intent ->
//                            intent.putExtra("distanceId", distanceIdCode)
//                            intent.putExtra("latNotices", lat)
//                            intent.putExtra("longNotices", long)
//                            _activity!!.startService(intent)
//                        }
//                    }
                }
            }
        }

    }

}