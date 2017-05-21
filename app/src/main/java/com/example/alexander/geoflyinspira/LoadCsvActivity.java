package com.example.alexander.geoflyinspira;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;
import java.util.Date;
import com.example.alexander.geoflyinspira.data.CoordenadaDbHelper;
import com.example.alexander.geoflyinspira.data.coordenadaContract;

import org.w3c.dom.Text;

/**
 * Created by Alexander Esteban on 5/15/2017.
 */

public class LoadCsvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SEPARATOR = ",";
    public static final int FILE_SELECT_CODE = 0;
    private CoordenadaDbHelper coordenadaDbHelper;
    private TextView txt_path_file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_csv);
        coordenadaDbHelper = new CoordenadaDbHelper(this);

        Button btnLoad = (Button) findViewById(R.id.btn_loadFile);
        btnLoad.setOnClickListener(this);
        // Instanciamos el txt donde mostraremos la ruta del archivo selecionado por el usuario
        txt_path_file = (TextView) findViewById(R.id.lbl_path_file);
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        showFileChooser();
    }

    private void loadCsv (Uri uri){
        String isHeader = "";
        String isPhoto = "";
        String isVideo = "";
        Date timestamp = null;
        float latitude = 0;
        float longitude = 0;
        float altitude = 0;
        long newRowId = 0;
        BufferedReader br = null;

        //Variables para el archivo cargado
        String path = "";
        String type = "";
        String nameFile = "";

        // Formateamos la fecha para la fecha de inserción del registro
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss", Locale.ENGLISH);
        // Creamos una instancia de la base de datos de tal forma que podamos escribir sobre ella.
        SQLiteDatabase db = coordenadaDbHelper.getWritableDatabase();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            // Establecemos la ruta del archivo
            path = uri.toString();
            // TODO 2 Obtener el nombre del archivo para la ruta.
            txt_path_file.setText(path);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            int i = 0;
            while (null != line) {
                String[] fields = line.split(SEPARATOR);
                line = br.readLine();
                // Creamos un nuevo mapa de valores donde las columnas son las llaves
                ContentValues values = new ContentValues();
                // Validamos que si la primer fila tiene los encabezado entonces siga con la siguiente fila
                isHeader = fields[0].toString();

                if (!isHeader.equals("latitude")){
                    latitude = Float.parseFloat(fields[0].toString());
                    longitude = Float.parseFloat(fields[1].toString());
                    altitude = Float.parseFloat(fields[2].toString());
                    isPhoto = fields[15].toString();
                    isVideo = fields[16].toString();

                    if ( (isPhoto.equals("1")) || (isVideo.equals("1")) ) {
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_LATITUD, latitude );
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_LONGITUD, longitude );
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_ALTITUD, altitude );
                        // TODO  1 pendiente por guardar fecha del archivo ERROR: Unparsable java.text.ParseException: Unparseable date
                        timestamp  = getCastingStrToDate(fields[11].toString (), "dd-MM-yyyy");
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_DATETIME, dateFormat.format(timestamp)); // Datetime
                        values.put(coordenadaContract.COORDENADAEntry.COL_COOR_CREATED_DATE, dateFormat.format(new Date()));

                        // Insertamos los datos, si la inserción nos devuelve un -1 quiere decir que hubo un error en la inserción
                        // De lo contrario guardará la información de forma correcta.
                        newRowId = db.insert(coordenadaContract.COORDENADAEntry.TABLE_NAME, null, values);
                        i++;
                    }
                }
            }
            Toast.makeText(this, String.valueOf(i)+  " registros guardados.", Toast.LENGTH_SHORT).show();
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

    private void showFileChooser() {
        // Creamos un intento para abrir la aplicación
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // Que permita el abrir el archivo
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");      //csv only files
        try {
            startActivityForResult(Intent.createChooser(intent, "Seleccione un archivo para cargar"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Por favor, instale un administrador de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        Uri currentUri = null;
        // Si la respuesta del usuario es CORRECTA
        if(resultCode == Activity.RESULT_OK){
            // ResultData obtenemos el archivo gargado validando que no sea null
            if(resultData != null){
                currentUri = resultData.getData();
                loadCsv(currentUri);
            }
        }
    }

    /**
     * @param date = Fecha en formato String para que sea convertida
     * @param format = Formato especificado para el cual sera convertida la fecha,
     *               si no se especifica el formato por defecto sera MM d, yyyy HH:mm:ss
     * @return convertedDate = Sera la fecha de salida ya formateada.
     * */
    protected Date getCastingStrToDate(String date, String format){
        Date convertedDate = new Date();
        SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.ENGLISH);

        try {
            convertedDate = inputFormat.parse(date);
        }catch (Exception e){
            e.getStackTrace();
        }
        return convertedDate;
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
