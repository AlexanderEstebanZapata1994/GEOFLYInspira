package com.example.alexander.geoflyinspira.photoDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.alexander.geoflyinspira.photoList.PhotoListActivity;
import com.example.alexander.geoflyinspira.R;

public class PhotoDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String id = getIntent().getStringExtra(PhotoListActivity.EXTRA_PHOTO_ID);

        PhotoDetailsFragment fragment = (PhotoDetailsFragment)
                getFragmentManager().findFragmentById(R.id.photo_detail_container);
        if (fragment == null) {
            fragment = PhotoDetailsFragment.newInstance(id);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.photo_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}