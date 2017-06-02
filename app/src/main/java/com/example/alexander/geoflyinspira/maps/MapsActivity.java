package com.example.alexander.geoflyinspira.maps;

import android.app.Activity;
import android.app.Dialog;
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

    private GoogleMap mMap;
    double lati = 0;
    double longitude = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lati =  getIntent().getDoubleExtra(LATITUDE_EXTRA, 0);
        longitude = getIntent().getDoubleExtra(LONGITUDE_EXTRA, 0);
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

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        //LatLng recordLocation = new LatLng(-63.049092, -60.958994); // Posición exacta del Kraken en google Maps
        LatLng recordLocation = new LatLng(lati, longitude);
        String mititulo = "Ubicación de la foto: latitud " + String.valueOf(lati) + " + y su longitud " + String.valueOf(longitude);
        mMap.addMarker(new MarkerOptions().position(recordLocation).title(mititulo).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        float zoomLevel = 18;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recordLocation, zoomLevel));
    }
}
