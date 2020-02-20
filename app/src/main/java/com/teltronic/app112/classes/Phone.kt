package com.teltronic.app112.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.teltronic.app112.R
import com.teltronic.app112.screens.mainScreen.MainFragmentDirections

object Phone {

    private const val CALL_REQUEST_CODE = 100
    private const val LOCATION_REQUEST_CODE = 200

    fun existLocationPermission(context: FragmentActivity?): Boolean {
        val permission =
            ContextCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) //Compruebo si tengo permisos

        return permission == PackageManager.PERMISSION_GRANTED
    }


    fun askLocationPermissionLocation(context: FragmentActivity) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    //Revisar onRequestPermissionsResult en MainActivity.kt
    fun makeActionRequestPermissionsResult(
        activity: Activity, requestCode: Int, grantResults: IntArray
    ) {
        when (requestCode) {
            CALL_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makeCallIntent(activity)
                } else {
                    Toast.makeText(activity, R.string.txt_permission_call, Toast.LENGTH_LONG).show()
                }
            }
            LOCATION_REQUEST_CODE -> {
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
                android.Manifest.permission.CALL_PHONE
            ) //Compruebo si tengo permisos

        if (permission != PackageManager.PERMISSION_GRANTED) { //Si no los tengo los pido
            ActivityCompat.requestPermissions(
                context,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                CALL_REQUEST_CODE
            )
        } else { //Si los tengo realizo la llamada
            makeCallIntent(context)
        }
    }

    @SuppressLint("MissingPermission")
    private fun makeCallIntent(activity: Activity) { //Se debe comprobar previamente si ya tienes permisos
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+34976465656"))
        activity.startActivity(intent)
    }

    //Comprueba si está habilitada la localización GPS del móvil
    fun isLocationEnabled(activity: FragmentActivity?): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}

