package com.example.alexander.geoflyinspira.data;

import android.database.Cursor;
/**
 * Created by Alexander Esteban on 5/28/2017.
 */
/**
 * Entidad "CoordenadaDetalles"
 */
public class CoordenadaDetalles {
    private int ID;
    private float altitud;
    private float latitud;
    private float longitud;
    private byte[] archivoImg;
    private String extension;
    private String nombreArchivo;
    private String descripcion;
    private long fecha;

    public CoordenadaDetalles(int ID, float altitud, float latitud, float longitud, byte[] archivoImg,
                              String extension, String nombreArchivo, String descripcion, long fecha) {
        this.ID = ID;
        this.altitud = altitud;
        this.latitud = latitud;
        this.longitud = longitud;
        this.archivoImg = archivoImg;
        this.extension = extension;
        this.nombreArchivo = nombreArchivo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public int getID() {
        return ID;
    }

    public float getAltitud() {
        return altitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public byte[] getArchivoImg() {
        return archivoImg;
    }

    public String getExtension() {
        return extension;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public long getFecha() {
        return fecha;
    }

    public CoordenadaDetalles(Cursor cursor) {
        ID = cursor.getInt(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry._ID));
        altitud = cursor.getFloat(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_ALTITUD));
        latitud = cursor.getFloat(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_LATITUD));
        longitud = cursor.getFloat(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_LONGITUD));
        archivoImg = cursor.getBlob(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_ARCHIVO_IMG));
        extension = cursor.getString(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_EXTENSION));
        nombreArchivo = cursor.getString(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_NOMBRE_ARCHIVO));
        descripcion = cursor.getString(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_DESC));
        fecha = cursor.getLong(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_DATETIME)) ;
    }
}
