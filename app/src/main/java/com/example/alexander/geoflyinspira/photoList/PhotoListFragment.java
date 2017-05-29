package com.example.alexander.geoflyinspira.photoList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.alexander.geoflyinspira.R;
import com.example.alexander.geoflyinspira.data.CoordenadaDbHelper;
import com.example.alexander.geoflyinspira.data.coordenadaContract;
import com.example.alexander.geoflyinspira.photoDetails.PhotoDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoListFragment extends Fragment {
    public static final int REQUEST_SHOW_RECORD = 2;
    public ListView mPhotosList;
    public PhotosCursorAdapter mPhotosAdapter;

    private CoordenadaDbHelper coordenadaDbHelper;

    public PhotoListFragment() {
        // Required empty public constructor
    }

    public static PhotoListFragment newInstance() {
        return new PhotoListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo_list, container, false);

        // Referencias UI
        mPhotosList = (ListView) root.findViewById(R.id.photo_list);
        mPhotosAdapter = new PhotosCursorAdapter(getActivity(), null);

        // Setup
        mPhotosList.setAdapter(mPhotosAdapter);

        mPhotosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mPhotosAdapter.getItem(i);
                String currentId = currentItem.getString(
                        currentItem.getColumnIndex(coordenadaContract.COORDENADAEntry._ID));

                showDetailScreen(currentId);
            }
        });

        // Instancia de helper
        coordenadaDbHelper = new CoordenadaDbHelper(getActivity());

        loadPhotos();

        return root;
    }

    private void showDetailScreen(String recordID) {
        Intent intent = new Intent(getActivity(), PhotoDetailsActivity.class);
        intent.putExtra( PhotoListActivity.EXTRA_PHOTO_ID, recordID);
        startActivityForResult(intent, REQUEST_SHOW_RECORD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_SHOW_RECORD:
                    loadPhotos();
                    break;
            }
        }
    }
    private void loadPhotos() {
        new PhotosLoadTask().execute();
    }

    class PhotosLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return coordenadaDbHelper.getAllRecords();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mPhotosAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }

}