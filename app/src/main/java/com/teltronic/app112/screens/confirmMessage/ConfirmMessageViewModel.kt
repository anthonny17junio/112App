package com.teltronic.app112.screens.confirmMessage

import android.app.Activity
import android.location.Location
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rethinkdb.net.Connection
import com.teltronic.app112.R
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.classes.enums.ChatState
import com.teltronic.app112.classes.enums.PermissionsApp
import com.teltronic.app112.classes.enums.Subcategory
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.rethink.tb_chats.ChatsRethink
import com.teltronic.app112.database.room.DatabaseRoomHelper
import com.teltronic.app112.databinding.FragmentConfirmMessageBinding
import kotlinx.coroutines.*

class ConfirmMessageViewModel(
    subcat: Subcategory,
    activity: FragmentActivity,
    private val binding: FragmentConfirmMessageBinding
) :
    ViewModel() {

    private val _application = activity.application
    private val _activity = activity
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    lateinit var idUser: String

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

    private val _idChatToNavigate = MutableLiveData<String>()
    val idChatToNavigate: LiveData<String>
        get() = _idChatToNavigate

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

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

        _idChatToNavigate.value = null
    }

    fun tryCreateNewChat() {
        val mFusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(_activity as Activity)

        mFusedLocationClient.lastLocation.addOnCompleteListener(_activity as Activity) { task ->
            val location: Location? = task.result
            var lat = -1.0
            var long = -1.0

            //Aunque no pueda obtener la ubicación podría crear el nuevo chat
            if (location != null) {
                lat = location.latitude
                long = location.longitude
            }

            uiScope.launch {
                tryCreateNewChatIO(lat, long)
            }
        }
    }

    private suspend fun tryCreateNewChatIO(lat: Double, long: Double) {
        withContext(Dispatchers.IO) {
            disableInterface()
            //Tener conexión con rethinkDB
            val con = DatabaseRethink.getConnection()
            if (con != null) {
                //Tener una localización activa
                val locationEnabled = activateLocation()
                if (locationEnabled) {
                    //Tener un id de usuario válido
                    val idUserTmp =
                        DatabaseRoomHelper.getOrInsertSynchronizedRethinkId(con, _activity)
                    if (idUserTmp != null) {
                        idUser = idUserTmp
                        //Tener un id de subcategoría
                        val subcategory = _subcategory.value
                        if (subcategory != null) {
                            val idNewChat = createNewChat(subcategory, con, lat, long, idUser)

                            if (idNewChat != null) {
                                navigateToChat(idNewChat)
                            } else {
                                _strErrorCreateChat.postValue(
                                    _application.getString(R.string.error_creating_chat)
                                )
                            }
                        } else {
                            _strErrorCreateChat.postValue(
                                _application.getString(R.string.error_getting_subcategory)
                            )
                        }
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

    private fun createNewChat(
        subcategory: Subcategory,
        con: Connection,
        lat: Double,
        long: Double,
        idUser: String
    ): String? {
        val idSubcategory = subcategory.id
        val realTimeSelected = binding.chkRealTimeLocation.isChecked

        //Insert en rethink
        return ChatsRethink.insertNewChat(
            con,
            idUser,
            idSubcategory,
            lat,
            long,
            realTimeSelected,
            ChatState.IN_PROGRESS.id
        )
    }


    private fun activateLocation(): Boolean {
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
                _activity,
                PermissionsApp.FINE_LOCATION_FROM_CONFIRM_MESSAGE_FRAGMENT
            )
        }
        return locationEnabled
    }


    private fun navigateToChat(idChat: String) {
        _idChatToNavigate.postValue(idChat)
    }

    fun navigateToChatComplete() {
        _idChatToNavigate.value = null
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