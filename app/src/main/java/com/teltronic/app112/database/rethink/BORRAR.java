package com.teltronic.app112.database.rethink;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;


import java.util.HashMap;

public class BORRAR { //BORRAR SI NO DA ERRORES AL TRAER DE LA BD
    public Cursor<HashMap> getPalabras(RethinkDB r, Connection c) {

        Cursor<HashMap> cur = r.table("tb_palabras").getField("palabra").run(c);
        String palabras = "";
        for (Object palabra : cur) {
            palabras += palabra + "\n\n";
        }
        return cur;
    }
}
