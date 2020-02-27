package com.teltronic.app112.screens

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teltronic.app112.classes.Phone

class MainActivityViewModel : ViewModel() {
    //LIVE DATA
    //****************************************************
    //User profile
    private var _boolNavigateToUserProfile = MutableLiveData<Boolean>()
    val boolNavigateToUserProfile: LiveData<Boolean>
        get() = _boolNavigateToUserProfile
    //Medical info
    private var _boolNavigateToMedicalInfo = MutableLiveData<Boolean>()
    val boolNavigateToMedicalInfo: LiveData<Boolean>
        get() = _boolNavigateToMedicalInfo
    //Configuration
    private var _boolNavigateToConfiguration = MutableLiveData<Boolean>()
    val boolNavigateToConfiguration: LiveData<Boolean>
        get() = _boolNavigateToConfiguration
    //Legal notice
    private var _boolNavigateToLegalNotice = MutableLiveData<Boolean>()
    val boolNavigateToLegalNotice: LiveData<Boolean>
        get() = _boolNavigateToLegalNotice
    //About
    private var _boolNavigateToAbout = MutableLiveData<Boolean>()
    val boolNavigateToAbout: LiveData<Boolean>
        get() = _boolNavigateToAbout

    //INIT
    //****************************************************
    init {
        _boolNavigateToUserProfile.value = false
        _boolNavigateToMedicalInfo.value = false
        _boolNavigateToConfiguration.value = false
        _boolNavigateToLegalNotice.value = false
        _boolNavigateToAbout.value = false
    }

    //NAVIGATION
    //****************************************************
    //User profile
    fun navigateToUserProfile(activity: FragmentActivity) {
        Phone.biometricAuth(activity, _boolNavigateToUserProfile)
    }

    fun navigateToUserProfileComplete() {
        _boolNavigateToUserProfile.value = false
    }

    //    Medical info
    fun navigateToMedicalInfo(activity: FragmentActivity) {
        Phone.biometricAuth(activity, _boolNavigateToMedicalInfo)
    }

    fun navigateToMedicalInfoComplete() {
        _boolNavigateToMedicalInfo.value = false
    }

    //Configuration
    fun navigateToConfiguration() {
        _boolNavigateToConfiguration.value = true
    }

    fun navigateToConfigurationComplete() {
        _boolNavigateToConfiguration.value = false
    }

    //Legal notice
    fun navigateToLegalNotice() {
        _boolNavigateToLegalNotice.value = true
    }

    fun navigateToLegalNoticeComplete() {
        _boolNavigateToLegalNotice.value = false
    }

    //About
    fun navigateToAbout() {
        _boolNavigateToAbout.value = true
    }

    fun navigateToAboutComplete() {
        _boolNavigateToAbout.value = false
    }

}