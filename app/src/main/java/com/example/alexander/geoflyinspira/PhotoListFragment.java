package com.example.alexander.geoflyinspira;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.alexander.geoflyinspira.data.CoordenadaDbHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoListFragment extends Fragment {

    ListView mPhotosList;
    PhotosCursorAdapter mPhotosAdapter;
    FloatingActionButton mAddButton;

    private CoordenadaDbHelper coordenadaDbHelper;

    public PhotoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PhotoListFragment.
     */
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
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mPhotosList.setAdapter(mPhotosAdapter);

        // Instancia de helper
        coordenadaDbHelper = new CoordenadaDbHelper(getActivity());

        loadPhotos();
        return root;
    }

    private void loadPhotos() {
        new PhotosLoadTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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