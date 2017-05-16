package com.example.alexander.geoflyinspira;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // ActionBar  methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
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
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
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
}
