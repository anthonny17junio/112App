package com.teltronic.app112.screens.notices

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.database.room.notices.NoticeEntity

class NoticesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataSourceNotices = DatabaseApp.getInstance(application).noticesDao
     var notices: LiveData<List<NoticeEntity>> =dataSourceNotices.getAll()

}