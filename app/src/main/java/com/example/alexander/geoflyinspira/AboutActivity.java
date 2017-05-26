package com.example.alexander.geoflyinspira;

import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yenifer on 20/05/2017.
 */

public class AboutActivity extends AppCompatActivity implements OnClickListener {

    private TextView urlPagina;
    private ImageView urlFacebook;
    private ImageView urlTwitter;
    private ImageView urlInstagram;
    private String direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        urlPagina=(TextView)findViewById(R.id.editTextPagina);
        urlFacebook=(ImageView)findViewById(R.id.ImageViewFacebook);
        urlTwitter=(ImageView)findViewById(R.id.imageViewTwitter);
        urlInstagram=(ImageView)findViewById(R.id.imageViewInstagram);
        direccion="";
        urlPagina.setOnClickListener(this);
        urlFacebook.setOnClickListener(this);
        urlTwitter.setOnClickListener(this);
        urlInstagram.setOnClickListener(this);
    }

    public void onClick(View v){
      switch (v.getId()){
          case R.id.editTextPagina:
              direccion="http://geoflyinspira.co/";
              irAweb(direccion);
              break;
          case  R.id.ImageViewFacebook:
              direccion="https://www.facebook.com/Geoflyinspira/";
              irAweb(direccion);
              break;
          case  R.id.imageViewTwitter:
              direccion="https://twitter.com/GEOFLYINSPIRA";
              irAweb(direccion);
              break;
          case  R.id.imageViewInstagram:
              direccion="https://www.instagram.com/geoflyinspira/?hl=es";
              irAweb(direccion);
              break;
          default:
              break;
      }
    }

    public void irAweb(String d){
        Uri uri=Uri.parse(d);
        Intent intentNav=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intentNav);
    }
}
