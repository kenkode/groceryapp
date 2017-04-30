package com.softark.eddie.gasexpress.Singleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Eddie on 4/30/2017.
 */

public class RequestSingleton {

    private static Context context;
    private static RequestSingleton requestSingleton;
    private RequestQueue requestQueue;

    public RequestSingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestSingleton getInstance(Context context) {
        if(requestSingleton == null) {
            requestSingleton = new RequestSingleton(context);
        }
        return requestSingleton;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

}
