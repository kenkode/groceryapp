package com.softark.eddie.gasexpress.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eddie on 4/17/2017.
 */

public class History {

    public History() {
    }

    public ArrayList<HashMap<String, String>> getHistory() {
        ArrayList<HashMap<String, String>> history = new ArrayList<>();

        HashMap<String, String> h[] = new HashMap[6];

        for (int i = 0; i < h.length; i++) {
            h[i] = new HashMap<>();
            h[i].put("type", "K-GasData");
            h[i].put("price", "1000");
            h[i].put("time", "45");
            h[i].put("date", "12/2/17");
            history.add(h[i]);
        }

        return history;
    }

}
