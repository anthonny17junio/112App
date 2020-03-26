package com.teltronic.app112.screens.confirmMessage

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rethinkdb.net.Connection
import com.teltronic.app112.R
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.classes.enums.PermissionsApp
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.rethink.DatabaseRethinkHelper
import com.teltronic.app112.database.room.DatabaseRoomHelper
import kotlinx.coroutines.*

class ConfirmMessageViewModel(subcat: Subcategory, activity: FragmentActivity) :
    ViewModel() {

    private val _application = activity.application
    private val _activity = activity
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private var _progressbarStyle = MutableLiveData<Int>()
    val progressbarStyle: LiveData<Int>
        get() = _progressbarStyle

    private var _boolEnableInterface = MutableLiveData<Boolean>()
    val boolEnableInterface: LiveData<Boolean>
        get() = _boolEnableInterface

    private val _strErrorCreateChat = MutableLiveData<String>()
    val strErrorCreateChat: LiveData<String>
        get() = _strErrorCreateChat

    private val _subcategory = MutableLiveData<Subcategory>()
    val subcategory: LiveData<Subcategory>
        get() = _subcategory

    private val _strCategory = MutableLiveData<String>()
    val strCategory: LiveData<String>
        get() = _strCategory

    private val _boolNavigateToChat = MutableLiveData<Boolean>()
    val boolNavigateToChat: LiveData<Boolean>
        get() = _boolNavigateToChat

    init {
        enableInterface()
        _strErrorCreateChat.value = ""
        _subcategory.value = subcat
        val category = subcat.category
        if (category != null) {
            _strCategory.value = _application.getString(category.idTitle)
        } else {
            _strCategory.value = ""
        }

        _boolNavigateToChat.value = false
    }

    fun tryCreateNewChat() {
        uiScope.launch {
            tryCreateNewChatIO()
        }
    }

    private suspend fun tryCreateNewChatIO() {
        withContext(Dispatchers.IO) {
            disableInterface()
            //Tener conexión con rethinkDB
            val con = DatabaseRethink.getConnection()
            if (con != null) {
                //Tener una localización activa
                val locationEnabled = isLocationEnabled()
                if (locationEnabled) {
                    //Tener un id de usuario válido
                    val idUser = DatabaseRoomHelper.getOrInsertSynchronizedRethinkId(con, _activity)
                    if (idUser != null) {
                        //TODO: Crear el chat
                        navigateToChat()
                    } else {
                        _strErrorCreateChat.postValue(
                            _application.getString(R.string.error_getting_user)
                        )
                    }
                }
            } else {
                _strErrorCreateChat.postValue(
                    _application.getString(R.string.no_database_connection)
                )
            }

            enableInterface()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationEnabled = false

        if (!Phone.isLocationEnabled(_activity)) {
            _strErrorCreateChat.postValue(
                _application.getString(R.string.location_no_enabled)
            )
        } else if (Phone.existPermission(
                _activity,
                PermissionsApp.FINE_LOCATION_FROM_CONFIRM_MESSAGE_FRAGMENT
            )
        ) {
            locationEnabled = true
        } else {
            //Si no tienes permisos de localización los pides
            Phone.askPermission(
                _activity!!,
                PermissionsApp.FINE_LOCATION_FROM_CONFIRM_MESSAGE_FRAGMENT
            )
        }
        return locationEnabled
    }


    fun navigateToChat() {
        _boolNavigateToChat.postValue(true)
    }

    fun navigateToChatComplete() {
        _boolNavigateToChat.value = false
    }

    fun clearStrErrorCreateChat() {
        _strErrorCreateChat.value = ""
    }

    private fun enableInterface() {
        _boolEnableInterface.postValue(true)
        _progressbarStyle.postValue(View.INVISIBLE)
    }

    private fun disableInterface() {
        _boolEnableInterface.postValue(false)
        _progressbarStyle.postValue(View.VISIBLE)
    }
}