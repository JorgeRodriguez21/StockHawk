package com.sam_chordas.android.stockhawk.data;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by negri on 22/05/2016.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetFactory(this.getApplicationContext(),
                intent));
    }
}