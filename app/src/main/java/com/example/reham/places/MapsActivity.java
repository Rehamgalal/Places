package com.example.reham.places;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @BindView(R.id.done)
    Button done;
    private GoogleMap mMap;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = getIntent();
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;

        if (intent.getStringExtra("new").equals("newlocation")) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    if (markerOptions != null) {
                        mMap.clear();


                        markerOptions.position(latLng);
                        mMap.addMarker(markerOptions);
                        done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                double l1 = latLng.latitude;
                                double l2 = latLng.longitude;
                                intent.putExtra("latitude", l1);
                                intent.putExtra("longtude", l2);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

                    }
                }
            });
        } else {


            double l1 = intent.getDoubleExtra("latitude", 0);
            double l2 = intent.getDoubleExtra("longtude", 0);
            String coordl1 = Double.toString(l1);
            String coordl2 = Double.toString(l2);
            l1 = Double.parseDouble(coordl1);
            l2 = Double.parseDouble(coordl2);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(l1, l2));
            mMap.addMarker(markerOptions);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

}


