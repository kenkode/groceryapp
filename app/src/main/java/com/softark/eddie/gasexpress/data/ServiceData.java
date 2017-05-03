package com.softark.eddie.gasexpress.data;

import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Eddie on 5/3/2017.
 */

public class ServiceData {

    public ArrayList<Service> getServices() {
        ArrayList<Service> accessories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Service service = new Service();
            service.setName("Gas System installations");
            service.setId(UUID.randomUUID().toString());
            accessories.add(service);
        }
        return accessories;
    }

}
