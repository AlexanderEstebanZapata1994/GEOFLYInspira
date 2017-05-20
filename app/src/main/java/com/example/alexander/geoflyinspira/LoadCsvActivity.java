package com.example.alexander.geoflyinspira;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateTimePatternGenerator;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.geoflyinspira.data.CoordenadaDbHelper;
import com.example.alexander.geoflyinspira.data.coordenadaContract;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;
import java.util.*;
import java.util.Date;

/**
 * Created by Alexander Esteban on 5/15/2017.
 */

public class LoadCsvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SEPARATOR = ",";
    public static final String QUOTE="\"";
    public static final int FILE_SELECT_CODE = 0;
    private CoordenadaDbHelper coordenadaDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_csv);

        Button btnLoad = (Button) findViewById(R.id.btn_loadFile);
        btnLoad.setOnClickListener(this);
        coordenadaDbHelper = new CoordenadaDbHelper(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        loadCsv();
    }

    private void loadCsv (){
        String isHeader = "";
        String isPhoto = "";
        String isVideo = "";
        float latitude = 0;
        float longitude = 0;
        float altitude = 0;
        Date timestamp = null;

        BufferedReader br = null;

        // Creamos una instancia de la base de datos de tal forma que podamos escribir sobre ella.
        SQLiteDatabase db = coordenadaDbHelper.getWritableDatabase();
        // Formateamos la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.records);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            while (null != line) {
                String[] fields = line.split(SEPARATOR);
                line = br.readLine();
                // Creamos un nuevo mapa de valores donde las columnas son las llaves
                ContentValues values = new ContentValues();
                // Validamos que si la primer fila tiene los encabezado entonces siga con la siguiente fila
                isHeader = fields[0].toString();
                latitude = Float.parseFloat(fields[0].toString());
                longitude = Float.parseFloat(fields[0].toString());
                altitude = Float.parseFloat(fields[0].toString());
                isPhoto = fields[15].toString();
                isVideo = fields[16].toString();

                if (!isHeader.equals("latitude")){
                    if ( (isPhoto.equals("1")) || (isVideo.equals("1")) ) {
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_LATITUD, latitude );
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_LONGITUD, longitude );
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_ALTITUD, altitude );
                        // TODO pendiente por guardar fecha del archivo ERROR: Unparsable java.text.ParseException: Unparseable date
                        //Date parsedDate = dateFormat.parse(fields[11].toString ());
                        SimpleDateFormat print = new SimpleDateFormat("MM d, yyyy HH:mm:ss");
                        //values.put(coordenadaContract.COORDENADAEntry.COL_COOR_DATETIME, print.format(parsedDate) ); // Datetime

                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_CREATED_DATE, dateFormat.format(new Date())); // CREATED DATE
                        // Insertamos los datos, si la inserción nos devuelve un -1 quiere decir que hubo un error en la inserción
                        // De lo contrario guardará la información de forma correcta.
                        long newRowId = db.insert(coordenadaContract.COORDENADAEntry.TABLE_NAME, null, values);
                    }
                }
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }finally {
            try {
                if (null != br){
                    br.close();
                }
            }catch (IOException e){
                System.out.print(e.getMessage());
            }
        }
    }

    private static String[] removeTrailingQuotes(String[] fields) {
        String result[] = new String[fields.length];

        for (int i=0;i<result.length;i++){
            result[i] = fields[i].replaceAll("^"+QUOTE, "").replaceAll(QUOTE+"$", "");
        }
        return result;
    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/csv");      //all files
        //intent.setType("text/xml");   //XML file only
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    // Este metodo es para visualizar la información de la base de datos creada y
    // poder verificar que efectivamente fue creada y tenemos conexión con la bd

    private void displayDatabaseInfo(){
        coordenadaDbHelper = new CoordenadaDbHelper(this);

        SQLiteDatabase database =coordenadaDbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + coordenadaContract.COORDENADAEntry.TABLE_NAME, null);
        try{
            TextView txt = (TextView) findViewById(R.id.lbl_path_file);
            txt.setText("ROWS " + cursor.getCount());
        }finally {
            cursor.close();
        }
    }
    /*
    Since getWritableDatabase() and getReadableDatabase() are expensive to call when the database is closed,
    you should leave your database connection open for as long as you possibly need to access it.
    Typically, it is optimal to close the database in the onDestroy() of the calling Activity.
    https://developer.android.com/training/basics/data-storage/databases.html
    */
    @Override
    protected void onDestroy(){
        coordenadaDbHelper.close();
        super.onDestroy();
    }
}
