package com.example.alexander.geoflyinspira.photoList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.geoflyinspira.R;
import com.example.alexander.geoflyinspira.data.coordenadaContract;

/**
 * Created by ALEXANDER ESTEBAN on 5/28/2017.
 */

public class PhotosCursorAdapter extends CursorAdapter {

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     *                {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public PhotosCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return  inflater.inflate(R.layout.activity_list_item_photo, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Referencias UI.
        TextView nameText = (TextView) view.findViewById(R.id.tv_name);
        final ImageView avatarImage = (ImageView) view.findViewById(R.id.iv_avatar);

        // Get valores.
        String name = cursor.getString(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_NOMBRE_ARCHIVO));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(coordenadaContract.COORDENADAEntry.COL_COOR_ARCHIVO_IMG));

        // Setup.
        nameText.setText(name);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(null, bitmap);
        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(bitmap.getHeight());
        avatarImage.setImageDrawable(roundedDrawable);

    }
}