package com.softark.eddie.gasexpress.data;

import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.CartItem;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Eddie on 5/3/2017.
 */

public class AccessoryData {

    public ArrayList<Accessory> getAccessories() {
        ArrayList<Accessory> accessories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Accessory accessory = new Accessory();
            accessory.setName("Cylinder");
            accessory.setPrice(456.9);
            accessory.setId(UUID.randomUUID().toString());
            accessories.add(accessory);
        }
        return accessories;
    }

}
