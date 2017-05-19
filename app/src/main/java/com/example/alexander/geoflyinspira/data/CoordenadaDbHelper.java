package com.example.alexander.geoflyinspira.data;

import android.content.Context;
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
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
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
                // TODO cambiar la fecha que no permita valores null
                +  coordenadaContract.COORDENADAEntry.COL_COOR_DATETIME + " DATETIME , "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_ARCHIVO_IMG + " BLOB, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_EXTENSION + " TEXT, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_NOMBRE_ARCHIVO + " TEXT, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_DESC + " TEXT, "
                +  coordenadaContract.COORDENADAEntry.COL_COOR_CREATED_DATE + " DATETIME NOT NULL);";
        return sql;
    }
}