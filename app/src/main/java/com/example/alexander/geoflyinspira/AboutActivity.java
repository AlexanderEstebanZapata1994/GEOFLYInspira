package com.example.alexander.geoflyinspira;

import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by yenifer on 20/05/2017.
 */

public class AboutActivity extends AppCompatActivity implements OnClickListener {

    private TextView urlPagina;
    private String direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        urlPagina=(TextView)findViewById(R.id.editTextPagina);
        direccion="";
        urlPagina.setOnClickListener(this);
    }

    public void onClick(View v){
      switch (v.getId()){
          case R.id.editTextPagina:
              direccion="http://geoflyinspira.co/";
              irAweb(direccion);
      }
    }

    public void irAweb(String d){
        Uri uri=Uri.parse(d);
        Intent intentNav=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intentNav);
    }
}
