package com.example.alexander.geoflyinspira;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.alexander.geoflyinspira.photoList.PhotoListActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // ActionBar  methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_main, menu);
        // Getting the action bar item
        MenuItem menuItem = (MenuItem) findViewById(R.id.action_load_info);
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        onCreateInspiredPhotosActivity();
    }

    /********************************************************************************************************/
    /********************************************************************************************************/
    /********************************** Obteniendo las acciones del menu de opciones ************************/
    /********************************************************************************************************/
    /********************************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load_info:
                onCreateLoadCsvActivity();
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

    private void onCreateLoadCsvActivity(){
        Intent intent = new Intent(this, LoadCsvActivity.class);
        startActivity(intent);
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
}