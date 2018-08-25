package com.example.reham.places;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity {
    @BindView(R.id.place_title)
    TextView title;
    @BindView(R.id.map)
    Button location;
    @BindView(R.id.place_detail)
    TextView detail;
    @BindView(R.id.weather)
    TextView weather;
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
    String city;

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
        try {
            city = getcity(latitude, longtude);
        } catch (IOException e) {
            e.printStackTrace();
        }

        title.setText(name);
        title.setTextSize(60);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        detail.setText(eDetail);
        detail.setTextSize(20);
        WidgetDataOnSharedPreferences.saveWidgetDataOnSharedPreferences(this, id, name, String.valueOf(latitude), String.valueOf(longtude));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), PlaceWidget.class));
        PlaceWidget placeWidget = new PlaceWidget();
        placeWidget.onUpdate(this, appWidgetManager, ids);
        new Async().execute();
        PlaceWidget.getUpdatedRemoteViews(this);
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
                startActivity(i);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key", key);
    }

    public String getcity(double lat, double lng) throws IOException {
        String locality;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
        locality = addresses.get(0).getAddressLine(0);
        return locality;
    }

    class Async extends AsyncTask {
        JSONObject data;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=YOUR_API_KEY");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";

                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                data = new JSONObject(json.toString());

                if (data.getInt("cod") != 200) {
                    System.out.println("Cancelled");
                    return null;
                }


            } catch (Exception e) {

                System.out.println("Exception " + e.getMessage());
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (data != null) {
                try {
                    double max = data.getJSONObject("main").getDouble("temp_max");
                    double min = data.getJSONObject("main").getDouble("temp_min");
                    weather.setText(R.string.maxtemp + max + "\n" + R.string.mintemp + min);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}