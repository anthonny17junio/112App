package com.teltronic.app112.screens

import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.rethinkdb.RethinkDB
import com.rethinkdb.gen.ast.ReqlExpr
import com.rethinkdb.model.OptArgs
import com.rethinkdb.net.Cursor
import com.teltronic.app112.classes.GoogleApiPeopleHelper
import com.teltronic.app112.classes.services.ListenNewMessagesService
import com.teltronic.app112.classes.enums.NamesRethinkdb
import com.teltronic.app112.classes.services.ListenNewNoticesService
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.DatabaseRoomHelper
import com.teltronic.app112.database.room.chats.ChatEntityConverter
import com.teltronic.app112.database.room.configurations.ConfigurationsEntity
import com.teltronic.app112.database.room.userRethink.UserRethinkEntity
import com.teltronic.app112.database.room.messages.MessageEntityConverter
import kotlinx.coroutines.*

@Suppress("UNCHECKED_CAST", "SENSELESS_COMPARISON")
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

    private var userRethink: LiveData<UserRethinkEntity>
    private var configurationsRoom: LiveData<ConfigurationsEntity?>

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var cursorChangesTbChats: Cursor<*>
    private lateinit var cursorChangesTbMessages: Cursor<*>
    private val databaseApp = DatabaseApp.getInstance(activityParam.application)
    private val dataSourceChats = databaseApp.chatsDao
    private val dataSourceMessages = databaseApp.messagesDao

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

        val dataSourceUserRethink = DatabaseApp.getInstance(_activity.application).userRethinkDao
        val dataSourceConfigurations =
            DatabaseApp.getInstance(_activity.application).configurationsDao
        userRethink = dataSourceUserRethink.getLiveData()
        configurationsRoom = dataSourceConfigurations.getLiveData()
        configurationsObserver()
        userIdRethinkObserver()
    }

    //Observer que se dispara al existir un usuario en room
    private fun userIdRethinkObserver() {
        //Cuando existe un id logueado en la base de datos se
        //escucha de Rethinkdb los cambios a las tablas de los chats
        userRethink.observe(_activity, Observer { userRethinkInRoom ->
            if (userRethinkInRoom != null) {
                val idUser = userRethinkInRoom.id_rethink
                //Listener para escuchar los cambios de los chats
                uiScope.launch {
                    subscribeToChangesTbChatsIO(idUser)
                }
                //Listener para escuchar los cambios de los mensajes
                uiScope.launch {
                    subscribeToChangesTbMessagesIO(idUser)
                }
                //Inicia el servicio para notficar nuevos mensajes
                uiScope.launch {
                    val contextActivity = this@MainActivityViewModel._activity
                    Intent(contextActivity, ListenNewMessagesService::class.java).also { intent ->
                        intent.putExtra("idUser", idUser)
                        contextActivity.startService(intent)
                    }
                }
            }
        })
    }

    //Observer que se dispara al existir (o no) configuraciones en room
    private fun configurationsObserver() {
        //Cuando existe configuraciones en room (en el caso de no existir se deben escuchar todos los avisos)
        //escucha de Rethinkdb los cambios a las tablas de los avisos dependiendo de la distancia
        configurationsRoom.observe(_activity, Observer { configurations ->

            var distanceId: Int? = null
            var latNotices: Double? = null
            var longNotices: Double? = null

            if (configurations != null) {
                distanceId = configurations.distance_code_notices
                latNotices = configurations.lat_notices
                longNotices = configurations.long_notices
            }
            //Inicia el servicio para notficar avisos
            uiScope.launch {
                val contextActivity = this@MainActivityViewModel._activity
                Intent(contextActivity, ListenNewNoticesService::class.java).also { intent ->
                    intent.putExtra("distanceId", distanceId)
                    intent.putExtra("latNotices", latNotices)
                    intent.putExtra("longNotices", longNotices)
                    contextActivity.startService(intent)
                }
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private suspend fun subscribeToChangesTbChatsIO(idUser: String) {
        withContext(Dispatchers.IO) {
            val con = DatabaseRethink.getConnection()
            val r = RethinkDB.r
            val table = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)
                .getAll(idUser).optArg("index", "id_user")

            if (con != null) {
                //OptArgs.of("time_format", "raw")  me devuelve la fecha como jsonObject y no como offSetDatetime
                //cursorChangesTbChats = table.changes().run(con) as Cursor<*> //ANTES devuelve la fecha como OffSetDatetime
                cursorChangesTbChats =
                    table.changes().run(con, OptArgs.of("time_format", "raw")) as Cursor<*>
                for (change in cursorChangesTbChats) { //Esto se ejecutará cada vez que haya un cambio en la tabla "tb_chats"
                    if (job.isActive) {
                        val cambio = change as HashMap<String, HashMap<String, *>>

                        val newChat = ChatEntityConverter.fromHashMap(
                            cambio["new_val"],
                            _activity.baseContext
                        )
                        val oldChat = ChatEntityConverter.fromHashMap(
                            cambio["old_val"],
                            _activity.baseContext
                        )

                        if (oldChat == null && newChat != null) {
                            //INSERT
                            val chatRoom = dataSourceChats.get(newChat.id)
                            if (chatRoom == null) {
                                dataSourceChats.insert(newChat)
                            }
                        } else if (oldChat != null && newChat == null) {
                            //DELETE
                            dataSourceMessages.deleteAllByChat(oldChat.id)
                            dataSourceChats.delete(oldChat.id)
                        } else if (oldChat != null && newChat != null) {
                            //UPDATE
                            val chatRoom = dataSourceChats.get(newChat.id)
                            if (chatRoom == null) {
                                dataSourceChats.insert(newChat)
                            } else {
                                dataSourceChats.update(newChat)
                            }
                        }
                    } else {
                        cursorChangesTbChats.close()
                    }
                }
            }
        }
    }

    private suspend fun subscribeToChangesTbMessagesIO(idUser: String) {
        withContext(Dispatchers.IO) {
            val con = DatabaseRethink.getConnection()
            val r = RethinkDB.r
            val tableChats = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_CHATS.text)
                .getAll(idUser).optArg("index", "id_user")
            val tableMessages =
                r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)

            if (con != null) {
                cursorChangesTbMessages = tableMessages.changes().filter { change: ReqlExpr ->
                    (r.expr(tableChats.g("id").coerceTo("array")))
                        .contains(change.g("new_val").g("id_chat"))
                }.filter { change: ReqlExpr -> (change.g("new_val").g("id_user").ne(idUser)) }
                    .run(con, OptArgs.of("time_format", "raw")) as Cursor<*>
                for (change in cursorChangesTbMessages) { //Esto se ejecutará cada vez que haya un cambio en la tabla "tb_chats"
                    if (job.isActive) {
                        val cambio = change as HashMap<String, HashMap<String, *>>

                        val newMessage = MessageEntityConverter.fromHashMap(
                            cambio["new_val"]
                        )
                        val oldMessage = MessageEntityConverter.fromHashMap(
                            cambio["old_val"]
                        )

                        if (oldMessage == null && newMessage != null) {
                            //INSERT
                            val messageRoom = dataSourceMessages.get(newMessage.id)
                            if (messageRoom == null) {
                                dataSourceMessages.insert(newMessage)
                            }
                        } else if (oldMessage != null && newMessage == null) {
                            //DELETE
                            dataSourceMessages.delete(oldMessage.id)
                        } else if (oldMessage != null && newMessage != null) {
                            //UPDATE
                            val messageRoom = dataSourceMessages.get(newMessage.id)
                            if (messageRoom == null) {
                                dataSourceMessages.insert(newMessage)
                            } else {
                                dataSourceMessages.update(newMessage)
                            }
                        }
                    } else {
                        cursorChangesTbMessages.close()
                    }
                }
            }
        }
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

    //Después de esta función, en tb_user_rethink (ROOM)
    //se encontrará el id que existe en rethinkDB correspondiente al usuario
    //que se acaba de loguear con la cuenta de google
    fun syncDatabasesAfterGoogleAuth() {
        //Obtener el id de google (nunca será null)
        val account = GoogleSignIn.getLastSignedInAccount(_activity)
        requireNotNull(account)

        val viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        uiScope.launch {
            syncDatabasesAfterGoogleAuthIO()
        }
    }

    private suspend fun syncDatabasesAfterGoogleAuthIO() {
        withContext(Dispatchers.IO) {

            val con = DatabaseRethink.getConnection()
            if (con != null) {
                val dataSourceConfigurations =
                    DatabaseApp.getInstance(_activity.application).userRethinkDao
                val configurations = dataSourceConfigurations.get()

                val idUser = DatabaseRoomHelper.getOrInsertSynchronizedRethinkId(con, _activity)

                if (configurations != null) {
                    val idRoom = configurations.id_rethink
                    //Se asegura que estén sincronizados los id de usuario de google con room y con rethinkdb

                    if (idRoom != idUser) {
                        //Si el id que estaba en room no es igual con el id sincronizado, eliminar lo que había antes en tb_chats y en tb_messages
                        val databaseApp = DatabaseApp.getInstance(_activity.application)
                        val dataSourceMessages = databaseApp.messagesDao
                        val dataSourceChats = databaseApp.chatsDao

                        dataSourceMessages.deleteAll()
                        dataSourceChats.deleteAll()
                    }
                }
            }
        }
    }

}
