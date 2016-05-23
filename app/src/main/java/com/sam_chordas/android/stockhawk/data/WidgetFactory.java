package com.sam_chordas.android.stockhawk.data;

import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by negri on 22/05/2016.
 */
public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory , LoaderManager.LoaderCallbacks<Cursor> {

    public static  String[] items={"lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer",
            "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae",
            "arcu", "aliquet", "mollis",
            "etiam", "vel", "erat",
            "placerat", "ante",
            "porttitor", "sodales",
            "pellentesque", "augue",
            "purus"};
    public static String[] prices={"0", "0", "0",
            "0", "0", "0",
            "0", "0", "0",
            "0", "0", "0",
            "0", "0", "0",
            "0", "0", "0",
            "0", "0",
            "0", "0",
            "0", "0",
            "0"};
    private Context ctxt=null;
    private int appWidgetId;
    private Cursor mCursor;


    public WidgetFactory(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(items.length);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.list_item_quote);

        row.setTextViewText(R.id.stock_symbol, items[position]);
        row.setTextViewText(R.id.bid_price, prices[position]);

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(SimpleWidgetProvider.EXTRA_WORD, items[position]);
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.stock_symbol, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {

        if(mCursor!=null) {
            items = new String[mCursor.getCount()];
            int i = 0;
            if (mCursor.moveToFirst()) {
                do {
                    String data = mCursor.getString(mCursor.getColumnIndex("data"));
                    items[i] = data;
                    i++;
                    System.out.println("Esta en el cursor");
                } while (mCursor.moveToNext());
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(ctxt, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        onDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
