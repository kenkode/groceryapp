package com.softark.eddie.gasexpress.Singleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestSingleton {

    private Context context;
    private RequestSingleton requestSingleton;
    private RequestQueue requestQueue;

    public RequestSingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestSingleton getInstance() {
        if(requestSingleton == null) {
            requestSingleton = new RequestSingleton(context);
        }
        return requestSingleton;
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

}
