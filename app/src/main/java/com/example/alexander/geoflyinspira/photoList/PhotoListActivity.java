package com.example.alexander.geoflyinspira.photoList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}