package com.example.alexander.geoflyinspira;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

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
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                try {
                    startActivityForResult(intent, 1);
                } catch (android.content.ActivityNotFoundException ex) {    }
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
}
