package com.example.alexander.geoflyinspira;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private Button btnLoad;
    private Button btnLoadImg;
    public Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_csv);
        coordenadaDbHelper = new CoordenadaDbHelper(this);

        //Instanciamos los objetos que usaremos para interactuar entre la lógica y la GUI
        btnLoad = (Button) findViewById(R.id.btn_loadFile);
        btnLoadImg = (Button) findViewById(R.id.btn_loadImg);
        txt_path_file = (TextView) findViewById(R.id.lbl_path_file);

        // Establecemos los eventos para cada unos de los objetos instanciados previamente
        btnLoad.setOnClickListener(this);
        btnLoadImg.setOnClickListener(this);
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == btnLoad.getId()){
            if (btnLoad.getText() == getResources().getString(R.string.btn_select_file)){
                showFileChooser();
            }else if (btnLoad.getText() == getResources().getString(R.string.btn_save)){
                loadCsv(this.uri);
            }
        }
        if (v.getId() == btnLoadImg.getId()) {
            if (btnLoadImg.getText() == getResources().getString(R.string.btn_select_file)) {
                showImageChooser();
            } else if (btnLoadImg.getText() == getResources().getString(R.string.btn_save)) {
                loadImg(this.uri);
            }
        }
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

        // Formateamos la fecha para la fecha de inserción del registro
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss", Locale.ENGLISH);
        // Creamos una instancia de la base de datos de tal forma que podamos escribir sobre ella.
        SQLiteDatabase db = coordenadaDbHelper.getWritableDatabase();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
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
            Toast.makeText(this, String.valueOf(i) +  " registros guardados exitosamente.", Toast.LENGTH_SHORT).show();
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
            startActivityForResult(Intent.createChooser(intent, "Seleccione un archivo CSV para cargar"), 0);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Por favor, instale un administrador de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImg(Uri uri){

    }

    private void showImageChooser() {
        // Creamos un intento para abrir la aplicación
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Que permita el abrir el archivo
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("img/*");  //png, jpg, bitmap files
        try {
            startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen para cargar"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Por favor, instale un administrador de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        //Variables para el archivo cargado
        String path = "";

        switch (requestCode){
            case 0:
                // Si se selecciona un archivo
                if(resultCode == Activity.RESULT_OK){
                    // ResultData obtenemos el archivo gargado validando que no sea null
                    if(resultData != null){
                        this.uri = resultData.getData();
                        // Establecemos la ruta del archivo
                        path = this.uri.toString();
                        // TODO 2 Obtener el nombre del archivo para la ruta.
                        txt_path_file.setText(path);
                        btnLoad.setText(R.string.btn_save);
                    }
                }else{ // Si no selecciona nada
                    Toast.makeText(this, "No selecciono ningún archivo", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                // Si se selecciona un archivo
                if(resultCode == Activity.RESULT_OK){
                    // ResultData obtenemos el archivo gargado validando que no sea null
                    if(resultData != null){
                        this.uri = resultData.getData();
                        // Establecemos la ruta del archivo
                        path = this.uri.toString();
                        // TODO 2 Obtener el nombre del archivo para la ruta.

                        btnLoadImg.setText(R.string.btn_save);
                    }
                }else {
                    Toast.makeText(this, "No selecciono ninguna imagen", Toast.LENGTH_SHORT).show();
                }
                break;
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

    /**
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