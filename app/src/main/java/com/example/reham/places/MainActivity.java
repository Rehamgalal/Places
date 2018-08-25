package com.example.reham.places;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.container_layout)
    FrameLayout frameLayout;
    @BindView(R.id.scroll_main)
    ScrollView scrollView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_event)
    FloatingActionButton addButton;
    @BindView(R.id.done)
    Button doneButton;
    @BindView(R.id.adView)
    AdView mAdView;
    private static String FRAGMENT_TAG = "Fragment";
    RecyclerAdapter adapter;
    BlankFragment blankFragment;
    int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null && savedInstanceState.getInt("key") == 1) {
            frameLayout.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            blankFragment = (BlankFragment)
                    this.getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            key = 1;
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollView.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
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
            frameLayout.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //MobileAds.initialize(this, AdRequest.DEVICE_ID_EMULATOR);
        getLoaderManager().initLoader(1, null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        /*AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this, R.string.deleting, Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                final int position = viewHolder.getAdapterPosition();
                String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.event_name)).getText().toString();
                getContentResolver().delete(PlacesContract.Entry.CONTENT_URI, PlacesContract.Entry.placeName + "=?", new String[]{title});
                adapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
                key = 1;
                blankFragment = new BlankFragment();
                final FragmentManager manager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("editactivity", "");
                blankFragment.setArguments(bundle);
                manager.beginTransaction().add(R.id.container_layout, blankFragment, FRAGMENT_TAG).commit();
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrollView.setVisibility(View.VISIBLE);
                        addButton.setVisibility(View.VISIBLE);
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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, PlacesContract.Entry.CONTENT_URI, null, null, null, PlacesContract.Entry._ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter = new RecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key", key);
    }
}

