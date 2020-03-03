package com.teltronic.app112.classes

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.teltronic.app112.R
import timber.log.Timber
import java.lang.ref.WeakReference
import java.net.URL

class DownloadImageTask(
    private val imgView: WeakReference<ImageView>,
    var resources: Resources,
    private var liveDataBitmap: WeakReference<MutableLiveData<Bitmap>> //Recibe un liveData para evitar descargar la imagen innecesariamente
) : AsyncTask<String, Void, Bitmap>() {
    //Debe ser weak reference porque puede pasar que al obtener la imagen el parámetro ya no exista
    override fun doInBackground(vararg urls: String): Bitmap {
        val liveDataBitmapValue = liveDataBitmap.get()?.value

        return if (liveDataBitmapValue == null) {
            val urlOfImage = urls[0]
            val picture: Bitmap
            picture = try {
                val inputStream = URL(urlOfImage).openStream()
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Throwable) {
                Timber.e("Error al descargar la imagen")
                BitmapFactory.decodeResource(resources, R.mipmap.empty_avatar)
            }

            picture
        } else {
            liveDataBitmapValue
        }
    }

    override fun onPostExecute(result: Bitmap) {
        imgView.get()?.setImageBitmap(result) //Seteo la imagen en el image view
        if (liveDataBitmap.get()?.value == null) {
            liveDataBitmap.get()?.value = result
        }
    }
}

