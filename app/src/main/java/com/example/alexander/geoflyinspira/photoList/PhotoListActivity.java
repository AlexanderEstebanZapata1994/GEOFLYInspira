package com.example.alexander.geoflyinspira.photoList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alexander.geoflyinspira.AboutActivity;
import com.example.alexander.geoflyinspira.LoadCsvActivity;
import com.example.alexander.geoflyinspira.R;

public class PhotoListActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO_ID = "extra_photo_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PhotoListFragment photoFragment = (PhotoListFragment) getFragmentManager().findFragmentById(R.id.photo_container);

        if (photoFragment == null) {
            photoFragment = PhotoListFragment.newInstance();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.photo_container, photoFragment)
                    .commit();
        }
    }

    /********************************************************************************************************/
    /********************************************************************************************************/
    /********************************** Obteniendo las acciones del menu de opciones ************************/
    /********************************************************************************************************/
    /********************************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    private void onCreateAboutActivity(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void onCreateLogoutHOME(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        try {
            startActivityForResult(intent, 1);
        } catch (android.content.ActivityNotFoundException ex) {    }
    }
}