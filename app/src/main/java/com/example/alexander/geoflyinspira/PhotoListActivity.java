package com.example.alexander.geoflyinspira;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}