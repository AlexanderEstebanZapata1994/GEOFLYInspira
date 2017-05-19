package com.example.alexander.geoflyinspira;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateTimePatternGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
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
import java.util.Arrays;
import java.util.*;

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
        String latitude = "";
        String longitude = "";
        String altitude = "";
        String datetime = "";

        // Declaramos las variables para almacenar las fecha
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        BufferedReader br = null;

        // Creamos una instancia de la base de datos de tal forma que podamos escribir sobre ella.
        SQLiteDatabase db = coordenadaDbHelper.getWritableDatabase();
        Calendar date = new GregorianCalendar();

        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.records);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            while (null != line) {
                String[] fields = line.split(SEPARATOR);
                System.out.println(Arrays.toString(fields));

                fields = removeTrailingQuotes(fields);
                System.out.println(Arrays.toString(fields));

                // Almacenamos los valores de la fecha actual antes de guardarla
                year = date.get(Calendar.YEAR);
                month = date.get(Calendar.MONTH);
                day = date.get(Calendar.DAY_OF_MONTH);
                hour = date.get(Calendar.HOUR);
                minute = date.get(Calendar.MINUTE);
                second = date.get(Calendar.SECOND);
                line = br.readLine();
                // Creamos un nuevo mapa de valores donde las columnas son las llaves
                ContentValues values = new ContentValues();
                values.put(coordenadaContract.COORDENADAEntry.COL_COOR_LATITUD, Float.parseFloat(fields[0].toString ())  ); //  Latitude
                values.put(coordenadaContract.COORDENADAEntry.COL_COOR_LONGITUD, Float.parseFloat(fields[1].toString()) ); // Longitude
                values.put(coordenadaContract.COORDENADAEntry.COL_COOR_ALTITUD, Float.parseFloat(fields[2].toString ())  ); // Altitude
                //TODO Falta guardar de  forma correcta la fecha realizando un casting de forma correcta
                //values.put(coordenadaContract.COORDENADAEntry.COL_COOR_DATETIME, Float.parseFloat(fields[3].toString()) ); // Datetime
                values.put(coordenadaContract.COORDENADAEntry.COL_COOR_CREATED_DATE, + day + "/" + (month+1) + "/" + year + " " + hour + ":" + minute + ":" + second); // CREATED DATE
                // Insertando la información si la inserción nos devuelve un -1 quiere decir que hubo un error en la inserción
                // De lo contrario guardará la información de forma correcta.
                long newRowId = db.insert(coordenadaContract.COORDENADAEntry.TABLE_NAME, null, values);
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }finally {
            try {
                if (null != br){
                    br.close();
                    displayDatabaseInfo();
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
        CoordenadaDbHelper coordenadaDbHelper = new CoordenadaDbHelper(this);

        SQLiteDatabase database =coordenadaDbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + coordenadaContract.COORDENADAEntry.TABLE_NAME, null);
        try{
            TextView txt = (TextView) findViewById(R.id.lbl_path_file);
            txt.setText("ROWS " + cursor.getCount());
        }finally {
            cursor.close();
        }
    }
}
