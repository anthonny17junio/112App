package com.teltronic.app112.classes.enums

import com.teltronic.app112.R

enum class ChatState(var idTitle: Int, var idIcon: Int, var idColor: Int) {
    IN_PROGRESS(R.string.chat_state_in_progress, R.drawable.ic_calendar_time, R.color.colorGreen),
    PROCESSED(R.string.chat_state_in_processed, R.drawable.ic_done_white, R.color.black_overlay)
}
