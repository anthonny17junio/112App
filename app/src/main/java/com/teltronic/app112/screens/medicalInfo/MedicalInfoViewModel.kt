package com.teltronic.app112.screens.medicalInfo

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.medicalInfo.MedicalInfoDao
import com.teltronic.app112.database.room.medicalInfo.MedicalInfoEntity
import com.teltronic.app112.databinding.FragmentMedicalInfoBinding
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MedicalInfoViewModel(
    application: Application,
    private val binding: FragmentMedicalInfoBinding
) : AndroidViewModel(application) {
    //Permite administrar las co-rutinas, nos permite cancelar todas las co-rutinas cuando este view model se destruya
    //y así evitar errores cuando la co-rutina desee retornar algún valor y ya no exista el view model
    private var viewModelJob = Job()

    private var _progressbarStyle = MutableLiveData<Int>()
    val progressbarStyle: LiveData<Int>
        get() = _progressbarStyle

    private var _boolEnableInterface = MutableLiveData<Boolean>()
    val boolEnableInterface: LiveData<Boolean>
        get() = _boolEnableInterface

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    private val dataSource = DatabaseApp.getInstance(application).medicalInfoDao

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    init {
        enableInterface()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //Scope determina en que hilo se ejecutará la co-rutina y con que job
    //Estamos ejecutando en Dispatchers.Main, esto significa que la co-rutina se ejecutará en el scope UI en el hilo principal
    //Esto tiene sentido ya que para muchas co-rutinas que empezaron por un view model tienen como resultado una actualización del UI después de algún procesamiento
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var medicalInfo: LiveData<MedicalInfoEntity?> =
        dataSource.get() //Al ser un liveData no se necesita volver a pedirlo (ya que siempre se mantiene actualizado)

    fun saveMedicalInfo() {
        //Ejecutamos una co-rutina en el hilo principal
        uiScope.launch {
            //TimeUnit.MILLISECONDS.sleep(3000L) //Si se pone código aquí SI se bloquea UI (porque estamos en Dispatchers.Main)
            val strAllergies = binding.etAllergies.text.toString()
            val strDiseases = binding.etDisease.text.toString()
            val strDisabilities = binding.etDisability.text.toString()
            val strMedications = binding.etMedicine.text.toString()
            val medicalInfoInsert =
                MedicalInfoEntity(
                    1,
                    strAllergies,
                    strDiseases,
                    strDisabilities,
                    strMedications
                )
            insertToDatabase(medicalInfoInsert)
            //medicalInfo = getMedicalInfoFromDatabase() //Esto no hace falta debido a que es un live data
        }
    }

    private suspend fun insertToDatabase(medicalInfoInsert: MedicalInfoEntity) {
        //Ejecutamos una co-rutina en el hilo IO (Input Output)
        withContext(Dispatchers.IO) {
            disableInterface() //Deshabilito la interfaz

            TimeUnit.MILLISECONDS.sleep(3000L) //Si se pone código aquí NO se bloquea UI (porque estamos en Dispatchers.IO)
            if (medicalInfo.value == null) {
                //Si no existe medicalInfo se inserta
                dataSource.insert(medicalInfoInsert)
            } else {
                //Si existe se actualiza
                dataSource.update(medicalInfoInsert)
            }
            enableInterface() //Habilito la interfaz
            _showSnackbarEvent.postValue(true)
        }
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