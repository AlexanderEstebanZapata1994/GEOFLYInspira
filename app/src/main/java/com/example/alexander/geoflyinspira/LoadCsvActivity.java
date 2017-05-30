package com.example.alexander.geoflyinspira;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
// TODO Revisar guardar la información con hilos y tareas sincronizadas
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;
import java.util.Date;
import com.example.alexander.geoflyinspira.data.CoordenadaDbHelper;
import com.example.alexander.geoflyinspira.data.coordenadaContract;
import com.example.alexander.geoflyinspira.photoList.PhotoListActivity;


/**
 * Created by Alexander Esteban on 5/15/2017.
 */

public class LoadCsvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SEPARATOR = ",";
    public static final int FILE_SELECTED_CODE = 1005;
    public static final int SELECTED_IMAGE = 1046;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private CoordenadaDbHelper coordenadaDbHelper;
    private TextView txt_path_file;
    private Button btnLoad;
    private Button btnLoadImg;
    private ListView lv_imgToLoad;
    private Uri uri;
    private Uri photoUri;
    // Listas de objetos para ser guardados luego
    private ArrayList<Bitmap> bitmapsList = new ArrayList<Bitmap>();
    private ArrayList<String> pathList= new ArrayList<String>();
    private ArrayList<String> photosNamesList= new ArrayList<String>();
    private ArrayList<String> photosExtList= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_csv);
        coordenadaDbHelper = new CoordenadaDbHelper(this);

        //Instanciamos los objetos que usaremos para interactuar entre la lógica y la GUI
        btnLoad = (Button) findViewById(R.id.btn_loadFile);
        btnLoadImg = (Button) findViewById(R.id.btn_loadImg);
        txt_path_file = (TextView) findViewById(R.id.lbl_path_file);
        lv_imgToLoad = (ListView) findViewById(R.id.lv_imgToLoad);

        // Establecemos los eventos para cada unos de los objetos instanciados previamente
        btnLoad.setOnClickListener(this);
        btnLoadImg.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= 23){
            verifyUserPermissions();
        }
    }

    // ActionBar  methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_load_csv, menu);
        // Getting the action bar item
        MenuItem menuItem = (MenuItem) findViewById(R.id.action_inspired_photos);
        return true;
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
          afterGrantPermissions(v);
    }

    private void afterGrantPermissions(View v){
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
                loadImg(this.bitmapsList, this.photosNamesList, this.photosExtList);
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
        //SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss", Locale.ENGLISH);
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
                        //values.put(coordenadaContract.COORDENADAEntry.COL_COOR_DATETIME, dateFormat.format(timestamp)); // Datetime
                        //values.put(coordenadaContract.COORDENADAEntry.COL_COOR_CREATED_DATE, dateFormat.format(new Date()));

                        // Insertamos los datos, si la inserción nos devuelve un -1 quiere decir que hubo un error en la inserción
                        // De lo contrario guardará la información de forma correcta.
                        newRowId = db.insert(coordenadaContract.COORDENADAEntry.TABLE_NAME, null, values);
                        if (newRowId == -1){ // Si hay un error en la inserción de algun dato guardaremos el registro para
                            Toast.makeText(this, "Error al guardar los registros.", Toast.LENGTH_SHORT).show();
                        }
                        i++;
                    }
                }
            }
            Toast.makeText(this, String.valueOf(i) +  " registros guardados exitosamente.", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            System.out.print(e.getMessage());
            Toast.makeText(this, "Error al momento de guardar los registros.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            System.out.print(e.getMessage());
            Toast.makeText(this, "Hubo un error al conectar con la base de datos.", Toast.LENGTH_SHORT).show();
        }finally {
            try {
                if (null != br){
                    br.close(); // Cerramos el buffer reader
                }
                if (null != db){
                    db.close();// Cerramos la conexión con la bd
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
            startActivityForResult(Intent.createChooser(intent, "Seleccione un archivo CSV para cargar"), FILE_SELECTED_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Por favor, instale un administrador de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImg(ArrayList<Bitmap> bitmapList, ArrayList<String> photoNames, ArrayList<String> photosExtensions  ){
        List idsForUpdating = new ArrayList();
        ByteArrayOutputStream stream = null;
        try {
            SQLiteDatabase dbHelper = coordenadaDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    // Obtenemos el id del primer registro que se cargo y aun no tiene archivo
                    coordenadaContract.COORDENADAEntry._ID,
            };
            // Filter results WHERE "archivo" = 'is null'
            String selection = coordenadaContract.COORDENADAEntry.COL_COOR_ARCHIVO_IMG  + " IS NULL ";
            String[] selectionArgs = { "" };

            Cursor cursor = dbHelper.query(
                    coordenadaContract.COORDENADAEntry.TABLE_NAME,      // The table to query
                    projection,                                         // The columns to return
                    selection,                                          // The columns for the WHERE clause
                    null,                                               // The values for the WHERE clause
                    null,                                               // don't group the rows
                    null,                                               // don't filter by row groups
                    null                                                // The sort order
            );

            idsForUpdating = new ArrayList<>();
            while(cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(coordenadaContract.COORDENADAEntry._ID));
                idsForUpdating.add(itemId);
            }
            cursor.close();
        }catch (Exception e){
            e.getMessage();
        }

        try {
            String fileName = "";
            String fileExt = "";
            Bitmap v_bitmap = null;
            byte[] blobBytes = new byte[0];
            String id = "";
            if (idsForUpdating.size() > 0){
                for (int i = 0; i < idsForUpdating.size(); i++ ){
                    stream = new ByteArrayOutputStream();
                    // Obtenemos los valores de la lista
                    fileName = photoNames.get(i).toString();
                    // Cuando las imagenes son cargadas desde el telefono memoria interna no guarda extensiones por lo que toca colocar una excepción para esta actividad
                    if (photosExtensions.size() > 1) {
                        fileExt = photosExtensions.get(i).toString();
                    }else {
                        fileExt = "";
                    }
                    v_bitmap = bitmapList.get(i); //Obtenemos el bitmap de acuerdo a la iteración actual
                    v_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    blobBytes = stream.toByteArray(); // Convertimos la imagen en un arreglo de bytes
                    id = idsForUpdating.get(i).toString(); // Guardamos el id el cual vamos a actualizar

                    // Ahora actualizamos los valores con base a los ids obtenidos previamente
                    SQLiteDatabase db = coordenadaDbHelper.getReadableDatabase();

                    // New value for one column
                    ContentValues values = new ContentValues();
                    values.put(coordenadaContract.COORDENADAEntry.COL_COOR_ARCHIVO_IMG, blobBytes);
                    values.put(coordenadaContract.COORDENADAEntry.COL_COOR_NOMBRE_ARCHIVO, fileName);
                    values.put(coordenadaContract.COORDENADAEntry.COL_COOR_EXTENSION, fileExt);
                    // Which row to update, based on the id
                    String selection = coordenadaContract.COORDENADAEntry.ID + " = ?";
                    String[] selectionArgs = { id };

                    int count = db.update(
                            coordenadaContract.COORDENADAEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);
                }
                Toast.makeText(this, "Las imágenes han sido guardadas correctamente.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No hay información nueva por actualizar.", Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException sql){
            sql.getMessage();
            Toast.makeText(this, "Ha ocurrido un error al guardar la información.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.getMessage();
        }
    }

    private void showImageChooser() {
        // Creamos un intento para abrir la aplicación
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        try {
            startActivityForResult(Intent.createChooser(intent, "Seleccione la imágen a Cargar"), SELECTED_IMAGE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Por favor, instale un administrador de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        //Variables para el archivo cargado
        String path = "";

        switch (requestCode){
            case FILE_SELECTED_CODE: // Cuando carga el archivo CSV
                // Si se selecciona un archivo
                if(resultCode == Activity.RESULT_OK){
                    // ResultData obtenemos el archivo gargado validando que no sea null
                    if(resultData != null){
                        this.uri = resultData.getData();
                        if (this.uri != null){
                            path = this.uri.toString();
                            if (path.toLowerCase().startsWith("file://")){
                                path = (new File(URI.create(path))).getAbsolutePath();
                            }
                        }
                        txt_path_file.setText(path);
                        btnLoad.setText(R.string.btn_save);
                    }
                }else{ // Si no selecciona nada
                    Toast.makeText(this, "No selecciono ningún archivo", Toast.LENGTH_SHORT).show();
                }
                break;
            case SELECTED_IMAGE:  // Cuando selecciona imagenes
                // Si se selecciona un archivo
                if(resultCode == Activity.RESULT_OK && resultData != null){
                    // ResultData obtenemos el archivo gargado validando que no sea null
                    try {
                        ClipData clipData = null;
                        int imageSelectedCount = 0;
                        if( resultData.getClipData() != null){
                            clipData = resultData.getClipData();
                            imageSelectedCount =  clipData.getItemCount();
                        }
                        // Onteniendo la imagen del parametro data
                        ClipData.Item item = null;

                        if (imageSelectedCount > 1){
                            bitmapsList = new ArrayList<Bitmap>();
                            pathList= new ArrayList<String>();
                            photosNamesList= new ArrayList<String>();
                            photosExtList= new ArrayList<String>();
                            for (int i = 0; i < imageSelectedCount; i++){
                                item = clipData.getItemAt(i);
                                photoUri = item.getUri();
                                InputStream inputStream;
                                inputStream = getContentResolver().openInputStream(photoUri);
                                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                                bitmapsList.add(i, bitmap); // Añadimos el bitmap a la lista para luego guardarla

                                // Obteniendo la ruta del archivo
                                String picturePath = photoUri.getPath();
                                pathList.add(i, picturePath);
                                //Con la variable picturePath podemos guardar la ruta donde quedará en la bd
                                //cursor.close();

                                // Obteniendo el nombre del archivo
                                int lengthPath = picturePath.length(); //Obtenemos la cantidad de letras de la ruta
                                char separator = '/';// Establcemos el divisor de la ruta
                                int positions = 0;// Guardamos la cantidad de segmentos que tenemos de la ruta es decir cuando encuentra un separador quiere decir que llevamos un segmento.
                                //Recorremos la ruta buscando los separadores para aunmentar la cantidad de segmentos
                                for (int j = 0; j < lengthPath; j++){
                                    char currentCharacter = picturePath.charAt(j);//
                                    if (currentCharacter == separator){
                                        positions++;
                                    }
                                }
                                String[] segments = picturePath.split("/");
                                String lastSegment = segments[positions]; // Obtenemos el nombre del archivo
                                photosNamesList.add(i, lastSegment);

                                // Obteniendo la extensión del archivo seleccionado
                                String ext = "";
                                char currentChar;
                                for (int j = 0; j < lastSegment.length(); j++){
                                    currentChar = lastSegment.charAt(j);
                                    if (currentChar != '.' ){
                                        continue;
                                    }else {
                                        // Guardamos despues del punto el resto de la extensión
                                        ext = ext.concat(lastSegment.substring(j));
                                        photosExtList.add(i, ext);
                                        break;
                                    }
                                }
                            }
                        }else { // Si solo selecciono 1 imagen hace esto
                            // Limpiamos las variables para cuando se añade de uno en uno
                            bitmapsList = new ArrayList<Bitmap>(0);
                            pathList= new ArrayList<String>(0);
                            photosNamesList= new ArrayList<String>(0);
                            photosExtList= new ArrayList<String>(0);

                            this.photoUri = resultData.getData();
                            InputStream inputStream;
                            inputStream = getContentResolver().openInputStream(this.photoUri);
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                            bitmapsList.add(0, bitmap); // Añadimos el bitmap a la lista para luego guardarla

                            // Obteniendo la ruta del archivo
                            String picturePath = photoUri.getPath();
                            pathList.add(0, picturePath);
                            //Con la variable picturePath podemos guardar la ruta donde quedará en la bd
                            //cursor.close();

                            // Obteniendo el nombre del archivo
                            int lengthPath = picturePath.length(); //Obtenemos la cantidad de letras de la ruta
                            char separator = '/';// Establcemos el divisor de la ruta
                            int positions = 0;// Guardamos la cantidad de segmentos que tenemos de la ruta es decir cuando encuentra un separador quiere decir que llevamos un segmento.
                            //Recorremos la ruta buscando los separadores para aunmentar la cantidad de segmentos
                            for (int j = 0; j < lengthPath; j++){
                                char currentCharacter = picturePath.charAt(j);//
                                if (currentCharacter == separator){
                                    positions++;
                                }
                            }
                            String[] segments = picturePath.split("/");
                            String lastSegment = segments[positions]; // Obtenemos el nombre del archivo
                            photosNamesList.add(0, lastSegment);

                            // Obteniendo la extensión del archivo seleccionado
                            String ext = "";
                            char currentChar;
                            for (int j = 0; j < lastSegment.length(); j++){
                                currentChar = lastSegment.charAt(j);
                                if (currentChar != '.' ){
                                    continue;
                                }else {
                                    // Guardamos despues del punto el resto de la extensión
                                    ext = ext + lastSegment.substring(j);
                                    break;
                                }
                            }
                            this.photosExtList.add(0, ext);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, photosNamesList);
                        lv_imgToLoad.setAdapter(adapter);
                        btnLoadImg.setText(R.string.btn_save);
                    }catch (Exception e){
                        e.getMessage();
                        Toast.makeText(this, "Error al cargar la información seleccionada", Toast.LENGTH_LONG).show();
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


    /********************************************************************************************************/
    /********************************************************************************************************/
    /********************************** Obteniendo las acciones del menu de opciones ************************/
    /********************************************************************************************************/
    /********************************************************************************************************/
    /****************************** Menu de opciones ACTION BAR ***************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_inspired_photos:
                onCreateInspiredPhotosActivity();
                return true;

            case R.id.action_about:
                onCreateAboutActivity();
                return true;

            case R.id.action_logout:
                onCreateLogoutHOME();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void onCreateInspiredPhotosActivity(){
        Intent intent = new Intent(this, PhotoListActivity.class);
        startActivity(intent);
    }

    private void onCreateAboutActivity(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void onCreateLogoutHOME(){
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        try {
            startActivityForResult(intent, 1);
        } catch (android.content.ActivityNotFoundException ex) {    }
    }

    /********************************************************************************************************/
    /********************************************************************************************************/
    /************************** Android's Runtime Permissions GRANTED BY THE USER****************************/
    /********************************************************************************************************/
    /********************************************************************************************************/

    private void verifyUserPermissions() {
        int canReadStoragePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (canReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
