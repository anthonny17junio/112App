package com.teltronic.app112.database.room.chatWithMessages

import androidx.lifecycle.LiveData
import androidx.room.Embedded
import androidx.room.Relation
import com.teltronic.app112.database.room.chats.ChatEntity
import com.teltronic.app112.database.room.messages.MessageEntity

data class ChatWithMessages(
    @Embedded var chat: ChatEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_chat"
    )
    //val messages: LiveData<List<MessageEntity>> //TODO:- DEJAR LA LÍNEA DE ABAJO O ESTA LÍNEA???
    var messages: List<MessageEntity>
)