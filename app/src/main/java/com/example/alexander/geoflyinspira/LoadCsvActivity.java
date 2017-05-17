package com.example.alexander.geoflyinspira;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Alexander Esteban on 5/15/2017.
 */

public class LoadCsvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SEPARATOR = ",";
    public static final String QUOTE="\"";
    public static final int FILE_SELECT_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_csv);

        Button btnLoad = (Button) findViewById(R.id.btn_loadFile);
        btnLoad.setOnClickListener(this);
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

    public void loadCsv (){
        String latitude = "";
        String longitude = "";
        String altitude = "";
        String datetime = "";
        BufferedReader br = null;
        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.records);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            while (null != line) {
                String[] fields = line.split(SEPARATOR);
                System.out.println(Arrays.toString(fields));

                fields = removeTrailingQuotes(fields);
                System.out.println(Arrays.toString(fields));

                line = br.readLine();
                latitude =  fields[0].toString();
                longitude = fields[1].toString();
                altitude = fields[2].toString();
                datetime = fields[3].toString();
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
}
