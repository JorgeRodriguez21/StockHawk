package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by negri on 19/05/2016.
 */
public class TimeLineActivity extends Activity {

    private LineChartView linechartView;
    private String name;
    private VolleyService volley;
    protected RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        linechartView = (LineChartView)findViewById(R.id.lineChart);
       name = getIntent().getStringExtra("name");
        volley = VolleyService.getInstance(this);
        requestQueue = volley.getRequestQueue();
        searchGraphData(name);
    }

    public void searchGraphData(String name){
        String url = getString(R.string.url_part1).concat(name).concat(getString(R.string.url_part2));
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONObject object;
                JSONArray result;
                float [] values = new float[20];
                String [] months = new String[20];
                try {
                    result = jsonObject.getJSONObject("query").getJSONObject("results").getJSONArray("quote");
                    for (int i =0; i<result.length() && i<20;i++){
                        try {
                            object = result.getJSONObject(i);
                            float val = Float.valueOf(object.getString("Close"));

                            String date = object.getString("Date");
                            values [i] = round3(val,2);
                            months [i] = date;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    LineSet dataset = new LineSet(months, values);
                    linechartView.addData(dataset);
                    linechartView.setStep(10);
                    linechartView.setHorizontalScrollBarEnabled(true);
                    linechartView.setVerticalScrollBarEnabled(true);
                    linechartView.setXLabels(AxisController.LabelPosition.NONE);
                    linechartView.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    System.out.println(new String(volleyError.networkResponse.data,"UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addToQueue(request);
    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (requestQueue == null)
                requestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            requestQueue.add(request);
        }
    }

    public static float round3(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
