package com.teltronic.app112.screens.userProfile

import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.teltronic.app112.classes.DownloadImageTask
import java.lang.ref.WeakReference

class UserProfileViewModel(activityParam: FragmentActivity) : ViewModel() {
    private var _strName = MutableLiveData<String>()
    val strName: LiveData<String>
        get() = _strName

    private var _strEmail = MutableLiveData<String>()
    val strEmail: LiveData<String>
        get() = _strEmail

    private var _profileImage = MutableLiveData<Bitmap>()
    val profileImage: LiveData<Bitmap>
        get() = _profileImage

    init {
        val googleAccount = GoogleSignIn.getLastSignedInAccount(activityParam)
        if (googleAccount != null) {
            _strName.value = googleAccount.displayName
            _strEmail.value = googleAccount.email
            DownloadImageTask(
                activityParam.resources,
                WeakReference(_profileImage)
            ).execute(googleAccount.photoUrl.toString())
        }
    }

}