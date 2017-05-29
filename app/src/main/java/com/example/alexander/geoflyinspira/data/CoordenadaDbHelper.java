package com.example.alexander.geoflyinspira.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SANDGURU on 5/18/2017.
 */

public class CoordenadaDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = CoordenadaDbHelper.class.getSimpleName();

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    public final static int DATABASE_VERSION = 1;

    public final static String DATABASE_NAME = "GEOFLYInspira.db";

    /**
     * Constructs a new instance of {@link CoordenadaDbHelper}.
     *
     * @param context of the app
     */
    public CoordenadaDbHelper(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION);}
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_ENTRIES = "";
        SQL_ENTRIES = getSQLENTRIES(SQL_ENTRIES);
        // Execute the SQL statement
        db.execSQL(SQL_ENTRIES);
    }

    /**
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // Creamos la estructura de la tabla.
    private String getSQLENTRIES (String sql){
        sql =  "CREATE TABLE " + coordenadaContract.COORDENADAEntry.TABLE_NAME + " ("
                +  coordenadaContract.COORDENADAEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_ALTITUD + " REAL NOT NULL, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_LATITUD + " REAL NOT NULL, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_LONGITUD + " REAL NOT NULL, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_DATETIME + " DATETIME , "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_ARCHIVO_IMG + " BLOB, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_EXTENSION + " TEXT, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_NOMBRE_ARCHIVO + " TEXT, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_DESC + " TEXT, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_CREATED_DATE + " DATETIME);";
        return sql;
    }

    public Cursor getAllRecords() {
        return getReadableDatabase()
                .query(
                        coordenadaContract.COORDENADAEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getRecordById(String recordID) {
        Cursor c = getReadableDatabase().query(
                coordenadaContract.COORDENADAEntry.TABLE_NAME,
                null,
                coordenadaContract.COORDENADAEntry.ID + " = ? ",
                new String[] {recordID},
                null,
                null,
                null);
        return c;
    }
}
