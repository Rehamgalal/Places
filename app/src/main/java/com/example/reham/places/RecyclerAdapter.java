package com.example.reham.places;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ToDoViewHolder> {
    private Context mContext;
    private List<Place> Locations;
    int id;

    public RecyclerAdapter(Context context, Cursor mCursor) {
        mContext = context;
        Locations = new ArrayList<>();
        mCursor.moveToFirst();
        while (mCursor.moveToNext()) {
            int index1 = mCursor.getColumnIndex(PlacesContract.Entry.placeName);
            int index2 = mCursor.getColumnIndex(PlacesContract.Entry.placeDescription);
            int index3 = mCursor.getColumnIndex(PlacesContract.Entry.latitude);
            int index4 = mCursor.getColumnIndex(PlacesContract.Entry.longtude);
            int index5 = mCursor.getColumnIndex(PlacesContract.Entry._ID);
            String name = mCursor.getString(index1);
            String detail = mCursor.getString(index2);
            double latitude = mCursor.getDouble(index3);
            double longtude = mCursor.getDouble(index4);
            id = mCursor.getInt(index5);
            Place place = new Place(name, detail, latitude, longtude);
            Locations.add(place);
        }
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater;
        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        ToDoViewHolder viewHolder = new ToDoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        String name = Locations.get(position).getPlaceName();
        holder.placeName.setTextSize(45);
        holder.placeName.setTypeface(Typeface.DEFAULT_BOLD);
        holder.placeName.setText(name);
    }

    @Override
    public int getItemCount() {
        return Locations.size();
    }

    public void swapCursor(List<Place> events) {
        Locations = events;
        notifyDataSetChanged();

    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.event_name)
        TextView placeName;


        public ToDoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(mContext, EditActivity.class);
                    intent.putExtra("title", Locations.get(position).getPlaceName());
                    intent.putExtra("detail", Locations.get(position).getPlaceDescription());
                    intent.putExtra("latitude", Locations.get(position).getLatitude());
                    intent.putExtra("longtude", Locations.get(position).getLongtude());
                    intent.putExtra("id", id);
                    mContext.startActivity(intent);
                }
            });
        }


    }
}