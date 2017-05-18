package com.example.alexander.geoflyinspira.data;

import android.provider.BaseColumns;

/**
 * Created by Alexander Esteban on 5/18/2017.
 */

public final class coordenadaContract {
    // Para prevenir que accidentalmente se instancie la clase, generamos un constructor vacio.
    private coordenadaContract(){}

    /*
    Cada registro de la siguiente tabla de constantes representa un solo registro.
    */
    public static final class COORDENADAEntry implements BaseColumns{
        // Nombre de la tabla
        public final static String TABLE_NAME = "COORDENADA";
        // Nombre de las columnas de la tabla

        /**
         * Id unico para el registro de la bd
         * tipo: INTEGER
         */
        public final static String ID = BaseColumns._ID;

        /**
         * Altitud
         * tipo: INTEGER
         */
        public final static String COL_COOR_ALTITUD = "altitud";

        /**
         * latitud
         * tipo: INTEGER
         */
        public final static String COL_COOR_LATITUD = "latitud";

        /**
         * longitud
         * tipo: INTEGER
         */
        public final static String COL_COOR_LONGITUD = "longitud";

        /**
         * fecha en la que fue creado el registro desde el Drone que viene del archivo datetime(utc)
         * tipo: INTEGER
         */
        public final static String COL_COOR_DATETIME = "date";

        /**
         * Binario de la imagen que se cargue después del archivo
         * tipo: BLOB
         */
        public final static String COL_COOR_ARCHIVO_IMG = "archivo";

        /**
         * extension de la imagen
         * tipo: TEXT
         */
        public final static String COL_COOR_EXTENSION = "extension";

        /**
         * Nombre del archivo (imagen)
         * tipo: TEXT
         */
        public final static String COL_COOR_NOMBRE_ARCHIVO = "nombre";

        /**
         * Descripción
         * tipo: TEXT
         */
        public final static String COL_COOR_DESC = "descripcion";

        /**
         * Fecha en la que se inserto el registro en la base de datos
         * tipo: DATETIME
         */
        public final static String COL_COOR_CREATED_DATE = "created_date";
    }
}
