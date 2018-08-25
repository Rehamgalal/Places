package com.example.reham.places;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class PlaceWidget extends AppWidgetProvider {

    private static final String TAG = PlaceWidget.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int mWidgetsNumber = appWidgetIds.length;
        for (int i = 0; i < mWidgetsNumber; i++) {
            int appWidgetId = appWidgetIds[i];
            appWidgetManager.updateAppWidget(appWidgetId, getUpdatedRemoteViews(context));
        }

    }

    public static RemoteViews getUpdatedRemoteViews(Context context) {
        String[] wigetData = WidgetDataOnSharedPreferences.getWidgetDataFromSharedPreferences(context);
        String id = wigetData[0];
        String place = wigetData[1];
        String l1 = wigetData[2];
        String l2 = wigetData[3];
        double latitude = Double.parseDouble(l1);
        double longtude = Double.parseDouble(l2);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.place_widget);
        views.setTextViewText(R.id.appwidget_text, place);
        Intent intent = new Intent(context, MapsActivity.class);
        int Id = Integer.parseInt(id);
        intent.putExtra("id", Id);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longtude", longtude);
        intent.putExtra("new", "");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        return views;
    }

}

