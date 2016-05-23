package com.sam_chordas.android.stockhawk.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by negri on 08/04/2016.
 */
public class VolleyService {
    private static VolleyService mVolleyS = null;

    private RequestQueue mRequestQueue;

    private VolleyService(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyService getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new VolleyService(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


}
