package com.teltronic.app112.screens.location

import android.app.Activity
import android.location.Location
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ClipData
import android.content.ClipboardManager

class LocationViewModel(activity: FragmentActivity?) : ViewModel() {

    private var _activity: FragmentActivity? = activity
    private var _boolCopy = MutableLiveData<Boolean>()
    val boolCopy: LiveData<Boolean>
        get() = _boolCopy

    private var _coordinates = MutableLiveData<LatLng>()
    val coordinates: LiveData<LatLng>
        get() = _coordinates

    private var _boolMakeCall = MutableLiveData<Boolean>()
    val boolMakeCall: LiveData<Boolean>
        get() = _boolMakeCall

    private var _boolNavigateToNewChat = MutableLiveData<Boolean>()
    val boolNavigateToNewChat: LiveData<Boolean>
        get() = _boolNavigateToNewChat

    init {
        _boolCopy.value = false
        _boolMakeCall.value = false
        _boolNavigateToNewChat.value = false

        //Obtener la ubicación actual
        getLastLocation()
    }

    private fun getLastLocation(): Location? {
        val mFusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(_activity as Activity)
        mFusedLocationClient.lastLocation.addOnCompleteListener(_activity as Activity) { task ->
            val location: Location? = task.result
            if (location != null) {
                _coordinates.value = LatLng(location.latitude, location.longitude)
            }
        }
        return null
    }

    fun copyLatLang() {
        val strCopy =
            "Lat: " + _coordinates.value?.latitude.toString() + " Long: " + _coordinates.value?.latitude.toString()
        val myClipboard = _activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        val dataCopy = ClipData.newPlainText("latLang", strCopy)
        myClipboard?.setPrimaryClip(dataCopy)
        copyComplete()
    }

    fun initCopy() {
        _boolCopy.value = true
    }

    private fun copyComplete() {
        _boolCopy.value = false
    }

    //Make call
    fun makeCall() {
        _boolMakeCall.value = true
    }

    fun makeBoolCallComplete() {
        _boolMakeCall.value = false
    }

    fun navigateToNewChat() {
        _boolNavigateToNewChat.value = true
    }

    fun navigateToNewChatComplete() {
        _boolNavigateToNewChat.value = false
    }
}