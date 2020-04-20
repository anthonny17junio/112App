package com.teltronic.app112.database.rethink.tb_messages

import com.rethinkdb.RethinkDB
import com.teltronic.app112.classes.enums.NamesRethinkdb

object MessagesRethink {
    private val r = RethinkDB.r
    private val table = r.db(NamesRethinkdb.DATABASE.text).table(NamesRethinkdb.TB_MESSAGES.text)
}