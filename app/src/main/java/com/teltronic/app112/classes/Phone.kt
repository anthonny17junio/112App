@file:Suppress("SpellCheckingInspection")

package com.teltronic.app112.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.teltronic.app112.R
import com.teltronic.app112.screens.mainScreen.MainFragmentDirections
import java.util.concurrent.Executors

object Phone {

    //Verifica si existe un determinado permiso
    fun existPermission(context: FragmentActivity?, perm: PermissionsApp): Boolean {
        val permission =
            ContextCompat.checkSelfPermission(
                context!!,
                perm.manifestName
            ) //Compruebo si tengo permisos

        return permission == PackageManager.PERMISSION_GRANTED
    }

    //Pide un determinado permiso
    fun askPermission(context: FragmentActivity, perm: PermissionsApp) {
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

    //Realiza una llamada
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
                Codes.CODE_PERMISSION_CALL_PHONE.code
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
    private lateinit var boolDataParam: MutableLiveData<Boolean>
    fun biometricAuth(
        activity: FragmentActivity, //activity (si está en un fragment)
        boolLiveDataParam: MutableLiveData<Boolean>
    ) {
        boolDataParam = boolLiveDataParam
        fun changeNavigateLiveDataToTrue() {
            boolDataParam.postValue(true)
        }

        val executor = Executors.newSingleThreadExecutor()
        val kgm = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (kgm.isKeyguardSecure) { //Si puedo autenticarme (aunque no sea con huella) lo cual setDeviceCredentialAllowed(true) lo permite
            val biometricPrompt = BiometricPrompt( //Inicia el proceso de autenticación biométrica
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
        } else { //Si no puedo autenticarme muestro un mensaje
            Toast.makeText(
                    activity,
                    activity.resources.getString(R.string.device_no_secure),
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    //Fun google authentication (cuando se inicia en la aplicación)
    fun googleAuth(activity: Activity, resultCode: Int) {

        val account = GoogleSignIn.getLastSignedInAccount(activity)
        if (account == null) {
            val clientId =
                "533065996558-m8n8cbivnpvfi8oap2c6s9ttpnga4pk5.apps.googleusercontent.com"

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(clientId)
//                .requestScopes(Scope("https://www.googleapis.com/auth/user.birthday.read"),Scope(Scopes.PROFILE))
                .requestEmail()
                .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
            val signInIntent = mGoogleSignInClient.signInIntent

            activity.startActivityForResult(signInIntent, resultCode)
        }
    }


}

