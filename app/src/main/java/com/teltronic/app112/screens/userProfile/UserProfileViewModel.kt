package com.teltronic.app112.screens.userProfile

import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.teltronic.app112.classes.DownloadImageTask
import com.teltronic.app112.classes.GoogleApiPeopleHelper
import com.teltronic.app112.classes.enums.IntCodes

import java.lang.ref.WeakReference

class UserProfileViewModel(fragment: Fragment) : ViewModel() {
    private var _strName = MutableLiveData<String>()
    val strName: LiveData<String>
        get() = _strName

    private var _strGender = MutableLiveData<String>()
    val strGender: LiveData<String>
        get() = _strGender

    private var _strBirthDay = MutableLiveData<String>()
    val strBirthDay: LiveData<String>
        get() = _strBirthDay

    private var _strBirthMonth = MutableLiveData<String>()
    val strBirthMonth: LiveData<String>
        get() = _strBirthMonth

    private var _strBirthYear = MutableLiveData<String>()
    val strBirthYear: LiveData<String>
        get() = _strBirthYear

    private var _strEmail = MutableLiveData<String>()
    val strEmail: LiveData<String>
        get() = _strEmail

    private var _profileImage = MutableLiveData<Bitmap>()
    val profileImage: LiveData<Bitmap>
        get() = _profileImage

    init {
        val activity = fragment.activity
        val googleAccount = GoogleSignIn.getLastSignedInAccount(activity)
        if (googleAccount != null) {
            _strName.value = googleAccount.displayName
            _strEmail.value = googleAccount.email
            _strGender.value = ""
            _strBirthDay.value = ""
            _strBirthMonth.value = ""
            _strBirthYear.value = ""

            DownloadImageTask(
                activity!!.resources,
                WeakReference(_profileImage)
            ).execute(googleAccount.photoUrl.toString())

            //Inicia el intent para descargar los demás datos (fecha de nacimiento y género)
            GoogleApiPeopleHelper.googleAuth(
                IntCodes.CODE_REQUEST_API_PEOPLE_GOOGLE_AUTH_FRAGMENT_PROFILE.code,
                fragment
            )
        }
    }

    fun setGender(newGender: String) {
        _strGender.postValue(newGender)
    }

    fun setBirthDay(birthDay: String) {
        _strBirthDay.postValue(birthDay)
    }

    fun setBirthMonth(birthMonth: String) {
        _strBirthMonth.postValue(birthMonth)
    }

    fun setBirthYear(birthYear: String) {
        _strBirthYear.postValue(birthYear)
    }

}