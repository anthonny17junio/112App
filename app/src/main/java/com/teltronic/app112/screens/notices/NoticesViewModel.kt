package com.teltronic.app112.screens.notices

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.notices.NoticeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class NoticesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataSourceNotices = DatabaseApp.getInstance(application).noticesDao
    var notices: LiveData<List<NoticeEntity>> = dataSourceNotices.getAll()

    private val _noticeModal = MutableLiveData<NoticeEntity>()
    val noticeModal: LiveData<NoticeEntity>
        get() = _noticeModal

    suspend fun readNotice(idNotice: String) {
        withContext(Dispatchers.IO) {
            dataSourceNotices.updateRead(idNotice, true)
        }
    }

    suspend fun getModalNotice(idNotice: String) {
        withContext(Dispatchers.IO) {
            val notice = dataSourceNotices.get(idNotice)
            if (notice != null) {
                _noticeModal.postValue(notice)
            }
        }
    }

    fun clearNoticeModal() {
        _noticeModal.postValue(null)
    }

    fun getBitmapImage(idMessage: String): Bitmap? {
        val message = dataSourceNotices.get(idMessage)
        return try {
            val url = message?.photo
            val imageBitmap = BitmapFactory.decodeFile(url)
            imageBitmap
        } catch (e: Exception) {
            null
        }
    }
}