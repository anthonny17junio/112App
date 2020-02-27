package com.teltronic.app112.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.teltronic.app112.R
import com.teltronic.app112.screens.mainScreen.MainFragmentDirections
import java.util.concurrent.Executors

object Phone {


    fun existPermission(context: FragmentActivity?, perm:PermissionsApp): Boolean {
        val permission =
            ContextCompat.checkSelfPermission(
                context!!,
                perm.manifestName
            ) //Compruebo si tengo permisos

        return permission == PackageManager.PERMISSION_GRANTED
    }


    fun askPermission(context: FragmentActivity, perm:PermissionsApp) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(perm.manifestName),
            perm.code
        )
    }

    //Revisar onRequestPermissionsResult en MainActivity.kt
    fun makeActionRequestPermissionsResult(
        activity: Activity, requestCode: Int, grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsApp.CALL_PHONE.code -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makeCallIntent(activity)
                } else {
                    Toast.makeText(activity, R.string.txt_permission_call, Toast.LENGTH_LONG).show()
                }
            }
            PermissionsApp.FINE_LOCATION.code -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //Si acepto los permisos navego a "location screen"
                    val actionNavigate =
                        MainFragmentDirections.actionMainFragmentToLocationFragment()
                    //Ojo IMPORTANTE debo enviar myNavHostFragment debido a que en activity_main.xml es el navController
                    activity.findNavController(R.id.myNavHostFragment).navigate(actionNavigate)
                } else {
                    Toast.makeText(activity, R.string.txt_permission_location, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun makeCall(context: FragmentActivity) {
        val permission =
            ContextCompat.checkSelfPermission(
                context,
                PermissionsApp.CALL_PHONE.manifestName
            ) //Compruebo si tengo permisos

        if (permission != PackageManager.PERMISSION_GRANTED) { //Si no los tengo los pido
            ActivityCompat.requestPermissions(
                context,
                arrayOf(PermissionsApp.CALL_PHONE.manifestName),
                PermissionsApp.CALL_PHONE.code
            )
        } else { //Si los tengo realizo la llamada
            makeCallIntent(context)
        }
    }

    @SuppressLint("MissingPermission")
    private fun makeCallIntent(activity: Activity) { //Se debe comprobar previamente si ya tienes permisos
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+34661314793"))
//        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+34976465656")) //Teltronic
        activity.startActivity(intent)
    }

    //Comprueba si está habilitada la localización GPS del móvil
    fun isLocationEnabled(activity: FragmentActivity?): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /*Esta variable (boolNavigateLiveData) trabaja conjuntamente con biometricAuth
    si en el override onAuthenticationSucceeded se pone boolNavigateLiveDataParam.value = true
    solo se ejecuta una vez dicho override y siempre irá a la misma pantalla
    */
    private lateinit var boolNavigateLiveData: MutableLiveData<Boolean>

    fun biometricAuth(
        activity: FragmentActivity, //activity (si está en un fragment)
        boolNavigateLiveDataParam: MutableLiveData<Boolean>
    ) {
        boolNavigateLiveData = boolNavigateLiveDataParam
        fun changeNavigateLiveDataToTrue() {
            boolNavigateLiveData.value = true
        }

        val executor = Executors.newSingleThreadExecutor()
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    //Si el error es diferente a que el usuario ha presionado fuera de la pantalla
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                        Toast.makeText(activity, errString.toString(), Toast.LENGTH_LONG)
                            .show() //Muestra el error
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    changeNavigateLiveDataToTrue()
                    super.onAuthenticationSucceeded(result)
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.resources.getString(R.string.title_biometric))
            .setDeviceCredentialAllowed(true) //Pide el patrón
            .build()

        biometricPrompt.authenticate(promptInfo)


    }

}

