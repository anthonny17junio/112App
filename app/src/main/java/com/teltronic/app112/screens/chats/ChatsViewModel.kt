package com.teltronic.app112.screens.chats

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Cursor
import com.teltronic.app112.database.rethink.DatabaseRethink
import com.teltronic.app112.database.rethink.DatabaseRethinkHelper
import com.teltronic.app112.databinding.FragmentChatsBinding
import kotlinx.coroutines.*
import java.util.*


class ChatsViewModel(private val binding: FragmentChatsBinding, private val activity: Activity) :
    ViewModel() {
    private val _palabras = MutableLiveData<String>()
    val palabras: LiveData<String>
        get() = _palabras

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    init {
//        DatabaseRethinkHelper.removeTbPalabras(uiScope) //Esto elimina la tabla
//        DatabaseRethinkHelper.createTbPalabras(uiScope) //Esto crea por primera vez la tabla (comentar las dos líneas de abajo)
        subscribeChangesTbPalabrasUI() //Similar a un listener que escucha los cambios de la tabla "tb_palabras"
        getPalabrasUI()
    }


    private fun subscribeChangesTbPalabrasUI() {
        uiScope.launch {
            subscribeChangesTbPalabrasIO()
        }
    }

    private suspend fun subscribeChangesTbPalabrasIO() {
        withContext(Dispatchers.IO) {
            val con = DatabaseRethink.getConnection()
            val rethinkDB = RethinkDB.r
            val cursorChanges = rethinkDB.table("tb_palabras").changes().run(con) as Cursor<*>

            for (change in cursorChanges) { //Esto se ejecutará cada vez que haya un cambio en la tabla "tb_palabras"
                getPalabrasIO()
            }
        }
    }

    private fun getPalabrasUI() {
        uiScope.launch {
            getPalabrasIO()
        }
    }

    private suspend fun getPalabrasIO() {
        withContext(Dispatchers.IO) {
            val con = DatabaseRethink.getConnection()
            val rethinkDB = RethinkDB.r


            val cursor =
                rethinkDB.table("tb_palabras").orderBy().optArg("index", "time")
                    .run(con) as Cursor<HashMap<*, *>>
            val list = cursor.bufferedItems()

            var palabras = ""
            for (item in list) {
                val jsonObject = item as org.json.simple.JSONObject
                val palabra = jsonObject["palabra"] as String
                palabras += palabra + "\n"
            }
            _palabras.postValue(palabras)
        }
    }

    fun insertPalabraUI() {
        uiScope.launch {
            insertPalabraIO()
        }
    }

    private suspend fun insertPalabraIO() {
        withContext(Dispatchers.IO) {
            val con = DatabaseRethink.getConnection()
            val rethinkDB = RethinkDB.r
            val etPalabra = binding.etPalabra
            val palabra = etPalabra.text.toString()
            rethinkDB.table("tb_palabras")
                .insert(
                    rethinkDB.hashMap("palabra", palabra)
                        .with("time", rethinkDB.now())
                )
                .run(con) as Any

            activity.runOnUiThread {
                etPalabra.setText("")
            }
        }
    }

    fun removePalabras() {
        DatabaseRethinkHelper.removeAllDocumentsTbPalabras(uiScope)
    }

}
