package com.example.alexander.geoflyinspira.photoDetails;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.geoflyinspira.R;
import com.example.alexander.geoflyinspira.data.CoordenadaDbHelper;
import com.example.alexander.geoflyinspira.data.CoordenadaDetalles;
import com.example.alexander.geoflyinspira.maps.MapsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.alexander.geoflyinspira.R.id.tv_latitude;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoDetailsFragment extends Fragment {
    private final static String ARG_RECORD_ID = "photoId";
    private String recordID;

    private CollapsingToolbarLayout mCollapsingView;
    private ImageView imageRecord;
    private TextView mAltitude;
    private TextView mLatitude;
    private TextView mLongitude;
    private TextView mCaptureDate;
    private TextView mDescription;
    private TextView mViewOnMaps;
    private double myLongitude;
    private double myLatitude;

    private CoordenadaDbHelper coordenadaDbHelper;


    public PhotoDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param recordID Parameter 2.
     * @return A new instance of fragment PhotoDetailsFragment.
     */
    public static PhotoDetailsFragment newInstance(String recordID) {
        PhotoDetailsFragment fragment = new PhotoDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECORD_ID, recordID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recordID = getArguments().getString(ARG_RECORD_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo_details, container, false);
        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        imageRecord = (ImageView) getActivity().findViewById(R.id.iv_avatar);
        mAltitude = (TextView) root.findViewById(R.id.tv_altitude);
        mLatitude = (TextView) root.findViewById(tv_latitude);
        mLongitude = (TextView) root.findViewById(R.id.tv_longitude);
        mCaptureDate= (TextView) root.findViewById(R.id.tv_captureDate);
        mDescription = (TextView) root.findViewById(R.id.tv_description);

        mViewOnMaps = (TextView) root.findViewById(R.id.tv_view_on_maps);
        mViewOnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(getActivity(), MapsActivity.class);
            try {
                intent.putExtra( MapsActivity.LATITUDE_EXTRA, myLatitude);
                intent.putExtra( MapsActivity.LONGITUDE_EXTRA, myLongitude);
                startActivity(intent);
            }catch (Exception e){
                e.getMessage();
            }
        }
        });

        coordenadaDbHelper = new CoordenadaDbHelper(getActivity());
        loadRecord();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Acciones
    }

    private void loadRecord() {
        new GetRecordByIdTask().execute();
    }

    private void showRecord(CoordenadaDetalles coordenadaDetalles) {
        mCollapsingView.setTitle(coordenadaDetalles.getNombreArchivo());
        mAltitude.setText(Float.toString(coordenadaDetalles.getAltitud()));
        mLatitude.setText(Float.toString(coordenadaDetalles.getLatitud()));
        mLongitude.setText(Float.toString(coordenadaDetalles.getLongitud()));
        mDescription.setText(coordenadaDetalles.getDescripcion());
        long dateLong = coordenadaDetalles.getFecha();
        Date date =new Date(dateLong);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateStr = df2.format(date);
        mCaptureDate.setText(dateStr);

        byte[] blobImage = coordenadaDetalles.getArchivoImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(blobImage, 0, blobImage.length);
        imageRecord.setImageBitmap(bitmap);
        imageRecord.setScaleType(ImageView.ScaleType.CENTER_CROP);

        myLatitude = Double.parseDouble(Float.toString(coordenadaDetalles.getLatitud()));
        myLongitude = Double.parseDouble(Float.toString(coordenadaDetalles.getLongitud()));
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    private class GetRecordByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return coordenadaDbHelper.getRecordById(recordID);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showRecord(new CoordenadaDetalles(cursor));
            } else {
                showLoadError();
            }
        }
    }
}
