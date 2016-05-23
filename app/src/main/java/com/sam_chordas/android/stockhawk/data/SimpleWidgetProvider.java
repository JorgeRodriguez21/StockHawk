package com.sam_chordas.android.stockhawk.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;


/**
 * Created by negri on 22/05/2016.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {

        public static String EXTRA_WORD=
                "com.commonsware.android.appwidget.lorem.WORD";

        @Override
        public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
        int[] appWidgetIds) {
            for (int i=0; i<appWidgetIds.length; i++) {
                Intent svcIntent=new Intent(ctxt, WidgetService.class);

                svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
                svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

                RemoteViews widget=new RemoteViews(ctxt.getPackageName(),
                        R.layout.widget_layout);

                widget.setRemoteAdapter(appWidgetIds[i], R.id.recycler_view,
                        svcIntent);

                Intent clickIntent=new Intent(ctxt, MyStocksActivity.class);
                PendingIntent clickPI=PendingIntent
                        .getActivity(ctxt, 0,
                                clickIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                widget.setPendingIntentTemplate(R.id.recycler_view, clickPI);

                appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
            }

            super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
        }
}
