package com.teltronic.app112.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.teltronic.app112.R

object Phone {

    private const val CALL_REQUEST_CODE = 100

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

    fun makeActionRequestPermissionsResult( //Revisar onRequestPermissionsResult en MainActivity.kt
        activity: Activity,
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CALL_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makeCallIntent(activity)
                } else {
                    Toast.makeText(activity, R.string.txt_permission_call, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun makeCallIntent(activity: Activity) { //Se debe comprobar previamente si ya tienes permisos
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+34976465656"))
        activity.startActivity(intent)
    }

}

