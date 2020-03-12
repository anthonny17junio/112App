package com.teltronic.app112.screens

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.teltronic.app112.classes.GoogleApiPeopleHelper

class MainActivityViewModel(activityParam: MainActivity) : ViewModel() {
    private var _activity: MainActivity = activityParam

    //User profile
    private var _boolTryNavigateToUserProfile = MutableLiveData<Boolean>()
    val boolTryNavigateToUserProfile: LiveData<Boolean>
        get() = _boolTryNavigateToUserProfile

    private var _boolBiometricAuthToUserProfile = MutableLiveData<Boolean>()
    val boolBiometricAuthToUserProfile: LiveData<Boolean>
        get() = _boolBiometricAuthToUserProfile

    private var _boolNavigateToUserProfile = MutableLiveData<Boolean>()
    val boolNavigateToUserProfile: LiveData<Boolean>
        get() = _boolNavigateToUserProfile

    private var _profileImage = MutableLiveData<Bitmap>()
    val profileImage: LiveData<Bitmap>
        get() = _profileImage

    //Medical info
    private var _boolTryNavigateToMedicalInfo = MutableLiveData<Boolean>()
    val boolTryNavigateToMedicalInfo: LiveData<Boolean>
        get() = _boolTryNavigateToMedicalInfo

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
    //Google session
    private var _boolGoogleAuthenticated =
        MutableLiveData<Boolean>() //Indica si está autenticado o no con una cuenta de google
    val boolGoogleAuthenticated: LiveData<Boolean>
        get() = _boolGoogleAuthenticated

    private var _shouldAskGoogleAuth =
        MutableLiveData<Boolean>() //Indica si ya ha pedido autenticación o no (para evitar que pida cada ver que se gire la pantalla
    val shouldAskGoogleAuth: LiveData<Boolean>
        get() = _shouldAskGoogleAuth


    init {
        //Esto se inicia cuando se presiona el botón para ir a user profile
        _boolBiometricAuthToUserProfile.value = false
        //Esto se inicia cuando se cumplen todos los requisitos para ir a user profile
        _boolNavigateToUserProfile.value = false

        _boolTryNavigateToMedicalInfo.value = false
        _boolNavigateToMedicalInfo.value = false
        _boolNavigateToConfiguration.value = false
        _boolNavigateToLegalNotice.value = false
        _boolNavigateToAbout.value = false
        val googleAccount = GoogleSignIn.getLastSignedInAccount(_activity)
        _boolGoogleAuthenticated.value = googleAccount != null
        _shouldAskGoogleAuth.value = true

        GoogleApiPeopleHelper.initGoogleApiClient(_activity)
    }

    fun googleAuthAsked() {
        _shouldAskGoogleAuth.value = false
    }

    //****************************************************
    //NAVIGATION
    //****************************************************
    //User profile
    //*************
    //Cuando se da click por primera vez para ir a la pantalla de user profile
    fun tryNavigateToUserProfile() {
        _boolTryNavigateToUserProfile.value = true
    }

    //Cuando se termina de intentar ir a user profile (puede que se haya ido o no)
    fun navigateToUserProfileTried() {
        _boolTryNavigateToUserProfile.value = false
    }

    //Cuando se va a user profile (cuando se tiene todos los permisos)
    fun navigateToUserProfile() {
        _boolNavigateToUserProfile.value = true
    }

    //Cuando se ha ido a user profile
    fun navigateToUserProfileComplete() {
        _boolNavigateToUserProfile.value = false
    }

    //Se obtiene el bool para saber si está autenticado biométricamente (para modificarlo en Phone.biometricAuth())
    fun getLiveDataBiometricAuthToUserProfile(): MutableLiveData<Boolean> {
        return _boolBiometricAuthToUserProfile
    }

    //Se reinicia el bool de biometric auth para que lo vuelva a pedir cada vez
    fun resetBiometricUserProfileAuth() {
        _boolBiometricAuthToUserProfile.value = false
    }

    fun getLiveDataProfileImageBitmap(): MutableLiveData<Bitmap> {
        return _profileImage
    }

    //Medical info
    //*************
    fun tryNavigateToMedicalInfo() {
        _boolTryNavigateToMedicalInfo.value = true
    }

    fun navigateToMedicalInfoTried() {
        _boolTryNavigateToMedicalInfo.value = false
    }

    fun navigateToMedicalInfoComplete() {
        _boolNavigateToMedicalInfo.value = false
    }

    fun getLiveDataNavigateToMedicalInfo(): MutableLiveData<Boolean> {
        return _boolNavigateToMedicalInfo
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

    //****************************************************
    //FIN NAVIGATION
    //****************************************************

    //Google authentication
    fun authenticationWithGoogleComplete() {
        _boolGoogleAuthenticated.value = true
    }

}
