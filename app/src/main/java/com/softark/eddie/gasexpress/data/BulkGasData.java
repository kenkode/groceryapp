package com.softark.eddie.gasexpress.data;

import com.softark.eddie.gasexpress.models.BulkGas;

import java.util.ArrayList;

/**
 * Created by Eddie on 5/3/2017.
 */

public class BulkGasData {

    public ArrayList<BulkGas> getBulkGases() {
        ArrayList<BulkGas> bulkGas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            BulkGas gas = new BulkGas();
            gas.setMetric("Tons");
            gas.setSize(8);
            bulkGas.add(gas);
        }
        return bulkGas;
    }

}
