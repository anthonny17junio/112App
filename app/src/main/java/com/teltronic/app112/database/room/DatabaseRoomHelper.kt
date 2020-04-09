package com.teltronic.app112.database.room

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.rethinkdb.net.Connection
import com.teltronic.app112.database.rethink.tb_users.UsersRethink
import com.teltronic.app112.database.room.configurations.ConfigurationsDao
import com.teltronic.app112.database.room.configurations.ConfigurationsEntity

object DatabaseRoomHelper {

    //Obtiene id_rethink que se encuentre en Room en tb_configurations
    //pero se asegura que este sea el correcto en Rethink, en tb_users y en googleSignIn
    //si es nulo, lo inserta tanto en rethink como en room y devuelve el id creado
    fun getOrInsertSynchronizedRethinkId(con: Connection, activity: Activity): String? {
        val idSynchronizedUser: String?

        var googleIDLocal: String? = null

        //Inicializo googleIDLocal (puede ser null si nunca se ha iniciado sesión con google en la aplicación)
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        if (account != null) {
            googleIDLocal = account.id
        }

        //Inicializo el id de user en Room (puede ser null si nunca se ha iniciado sesión con google en la aplicación)
        val dataSource = DatabaseApp.getInstance(activity.application).configurationsDao
        val configurations = dataSource.get()
        //Verifico que este idUserRoom exista en RETHINKDB
        val idUserRoom = UsersRethink.getIdUserById(con, configurations.id_rethink)
        //Si no existe ningún usuario en rethinkDB que corresponda con el id que está en room elimino el usuario de ROOM
        if (idUserRoom == null) {
            dataSource.deleteAll()
        }

        /* EN ESTE PUNTO HAY 4 POSIBLES CASOS:
        CASO 1: en room NO existe un id_rethink en tb_configurations y NO hay ninguna cuenta de google en el dispositivo asociada
        CASO 2: en room NO existe un id_rethink en tb_configurations y SI hay una cuenta de google en el dispositivo asociada
        CASO 3: en room SI existe un id_rethink en tb_configurations y NO hay ninguna cuenta de google en el dispositivo asociada
        CASO 4: en room SI existe un id_rethink en tb_configurations y SI hay una cuenta de google en el dispositivo asociada
        */

        if (idUserRoom == null) {
            idSynchronizedUser = if (googleIDLocal == null) {
                //CASO 1
                caso1(con, dataSource)
            } else {
                //CASO 2
                caso2(con, dataSource, googleIDLocal)
            }
        } else {
            idSynchronizedUser = if (googleIDLocal == null) {
                //CASO 3
                caso3(con, idUserRoom, dataSource)

            } else {
                //CASO 4
                caso4(con, idUserRoom, googleIDLocal, dataSource)
            }
        }
        return idSynchronizedUser
    }

    //CASO 1: en room NO existe un id_rethink en tb_configurations y NO hay ninguna cuenta de google en el dispositivo asociada
    //*************************************************************************************************************************
    //En este caso creo un nuevo registro en rethinkdb y lo guardo en room
    private fun caso1(con: Connection, dataSource: ConfigurationsDao): String {
        val idRethink = UsersRethink.insertNewUserWithoutGoogleId(con)
        val configurationsEntity = ConfigurationsEntity(1, idRethink)
        dataSource.insert(configurationsEntity)
        return idRethink
    }

    //CASO 2: en room NO existe un id_rethink en tb_configurations y SI hay una cuenta de google en el dispositivo asociada
    //*********************************************************************************************************************
    //En este  caso compruebo si existe un registro en rethinkdb con dicho id de google
    //si no existe lo creo y lo guardo en room
    //si existe solo lo guardo en room
    private fun caso2(
        con: Connection,
        dataSource: ConfigurationsDao,
        googleIDLocal: String
    ): String {
        var idRethink = UsersRethink.getIdUserByGoogleId(con, googleIDLocal)
        if (idRethink == null) {
            idRethink = UsersRethink.insertNewUserWithGoogleId(con, googleIDLocal)
        }
        val configurationsEntity = ConfigurationsEntity(1, idRethink)
        dataSource.insert(configurationsEntity)
        return idRethink
    }

    //CASO 3: en room SI existe un id_rethink en tb_configurations y NO hay ninguna cuenta de google en el dispositivo asociada
    //*************************************************************************************************************************
    //En este caso compruebo si existe alguna cuenta de google en rethinkdb asociada a dicho id
    //Si existe elimino el id_rethink de room y llamo a caso1 (este caso no debería pasar)
    //Si no existe no hago nada (significa que es una cuenta temporal)
    private fun caso3(con: Connection, idUserRoom: String, dataSource: ConfigurationsDao): String {
        val googleId = UsersRethink.getGoogleIdUserById(con, idUserRoom)
        return if (googleId != null) {
            dataSource.deleteAll()
            caso1(con, dataSource)
        } else {
            UsersRethink.getIdUserById(con, idUserRoom)!!
        }
    }

    //CASO 4: en room SI existe un id_rethink en tb_configurations y SI hay una cuenta de google en el dispositivo asociada
    //*********************************************************************************************************************
    //En este caso verifico si coinciden estos id en rethinkdb
    //Si coinciden no hago nada
    //Si no coinciden elimino el usuario de room y llamo a caso2
    private fun caso4(
        con: Connection,
        idUserRoom: String,
        googleIDLocal: String,
        dataSource: ConfigurationsDao
    ): String {
        val googleIdRethink = UsersRethink.getGoogleIdUserById(con, idUserRoom)
        return if (googleIdRethink == googleIDLocal) {
            UsersRethink.getIdUserById(con, idUserRoom)!!
        } else {
            dataSource.deleteAll()
            caso2(con, dataSource, googleIDLocal)
        }
    }
}