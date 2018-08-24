package com.example.reham.places;


import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity {
    @BindView(R.id.place_title)
    TextView title;
    @BindView(R.id.map)
    Button location;
    @BindView(R.id.place_detail)
    TextView detail;
    @BindView(R.id.edit)
    Button edit;
    @BindView(R.id.done)
    Button doneButton;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    FrameLayout frameLayout;
    @BindView(R.id.edit_scroll)
    ScrollView scrollView;
    private static String FRAGMENT_TAG = "EditFragment";
    BlankFragment blankFragment;
    int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        if (savedInstanceState != null && savedInstanceState.getInt("key") == 1) {
            frameLayout.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            blankFragment = (BlankFragment)
                    this.getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            key = 1;
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollView.setVisibility(View.VISIBLE);
                    doneButton.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.GONE);
                    key = 0;
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                    if (fragment != null)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    frameLayout.setVisibility(View.GONE);

                }
            });
        } else {
            key = 0;
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            doneButton.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }
        Intent i = getIntent();
        final String name = i.getStringExtra("title");
        String eDetail = i.getStringExtra("detail");
        final double latitude = i.getDoubleExtra("latitude", 0);
        final double longtude = i.getDoubleExtra("longtude", 0);
        final int id = i.getIntExtra("id", 0);
        title.setText(name);
        title.setTextSize(60);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        detail.setText(eDetail);
        detail.setTextSize(20);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                key = 1;
                blankFragment = new BlankFragment();
                FragmentManager manager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("editactivity", "From Activity");
                bundle.putInt("id", id);
                blankFragment.setArguments(bundle);
                manager.beginTransaction().add(R.id.container, blankFragment, FRAGMENT_TAG).commit();
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrollView.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        doneButton.setVisibility(View.GONE);
                        key = 0;
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        frameLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MapsActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longtude", longtude);
                i.putExtra("new", "");
                Log.i("latlng", "" + new LatLng(latitude, longtude));
                startActivity(i);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key", key);
    }
}

