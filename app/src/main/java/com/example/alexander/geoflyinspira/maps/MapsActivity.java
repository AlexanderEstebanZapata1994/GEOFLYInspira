package com.example.alexander.geoflyinspira.maps;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.example.alexander.geoflyinspira.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public final static String LATITUDE_EXTRA = "LATITUDE_EXTRA";
    public final static String LONGITUDE_EXTRA = "LONGITUDE_EXTRA";
    public final static String BYTE_BITMAP_IMAGE = "BITMAP_IMAGE";

    private GoogleMap mMap;
    double lati = 0;
    double longitude = 0;
    byte[] bitmap_array = null;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lati =  getIntent().getDoubleExtra(LATITUDE_EXTRA, 0);
        longitude = getIntent().getDoubleExtra(LONGITUDE_EXTRA, 0);
        bitmap_array = getIntent().getByteArrayExtra(BYTE_BITMAP_IMAGE);
        bitmap = BitmapFactory.decodeByteArray(bitmap_array, 0, bitmap_array.length);

        //Redimensionamos la imagen para que no se vea tan grande en el mapa
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) 300) / width;
        float scaleHeight = ((float) 200) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(),10 );
            dialog.show();
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 18;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);

        //LatLng recordLocation = new LatLng(-63.049092, -60.958994); // Posición exacta del Kraken en google Maps
        LatLng recordLocation = new LatLng(lati, longitude);
        String mititulo = "Ubicación de la foto: latitud " + String.valueOf(lati) + " + y su longitud " + String.valueOf(longitude);
        mMap.addMarker(new MarkerOptions()
                .position(recordLocation)
                .title(mititulo)
                .snippet("Descripción: " +
                        "Aquí va la descripción")
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recordLocation, zoomLevel));
    }
}
