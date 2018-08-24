package com.example.reham.places;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BlankFragment extends Fragment {
    double latitude = 0.00;
    double longtude = 0.00;

    public BlankFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        final EditText title = rootView.findViewById(R.id.fplace_title);
        final EditText detail = rootView.findViewById(R.id.fplace_detail);
        final Button map = rootView.findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                i.putExtra("new", "newlocation");
                startActivityForResult(i, 1);
            }
        });
        Button save = rootView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = title.getText().toString();
                String description = detail.getText().toString();
                if (!getArguments().getString("editactivity").equals("")) {
                    int id = getArguments().getInt("id");
                    ContentValues contentValues = new ContentValues();
                    if (!name.equals("")) contentValues.put(PlacesContract.Entry.placeName, name);
                    if (!description.equals(""))
                        contentValues.put(PlacesContract.Entry.placeDescription, description);
                    if (latitude != 0) contentValues.put(PlacesContract.Entry.latitude, latitude);
                    if (longtude != 0) contentValues.put(PlacesContract.Entry.longtude, longtude);
                    getContext().getContentResolver().update(PlacesContract.Entry.CONTENT_URI, contentValues, PlacesContract.Entry._ID + "=?", new String[]{String.valueOf(id)});
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PlacesContract.Entry.placeName, name);
                    contentValues.put(PlacesContract.Entry.placeDescription, description);
                    contentValues.put(PlacesContract.Entry.latitude, latitude);
                    contentValues.put(PlacesContract.Entry.longtude, longtude);
                    Uri uri = getContext().getContentResolver().insert(PlacesContract.Entry.CONTENT_URI, contentValues);
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
                    if (uri == null) {
                        Toast.makeText(getContext(), "Error inserting reminder", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                latitude = data.getDoubleExtra("latitude", 0);
                longtude = data.getDoubleExtra("longtude", 0);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "No location added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
