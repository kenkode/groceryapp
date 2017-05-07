package com.softark.eddie.gasexpress.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.softark.eddie.gasexpress.PreviousPurchasesActivity;
import com.softark.eddie.gasexpress.data.OrderData;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Location;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Eddie on 5/7/2017.
 */

public class Checkout {

    private Context context;
    private static Location location;
    private static Map<String, String> user;
    private static String orderKey;
    private OrderData orderData;
    private GEPreference preference;

    public Checkout (Context context) {
        this.context = context;
        preference = new GEPreference(context);
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        Checkout.location = location;
    }

    public static Map<String, String> getUser() {
        return user;
    }

    public static void setUser(Map<String, String> user) {
        Checkout.user = user;
    }

    public void checkOut() {
        orderData = new OrderData(context);

        ArrayList<Gas> gases = (ArrayList<Gas>) Cart.getInstance().getCart().get(Cart.GASES);
        ArrayList<Service> services = (ArrayList<Service>) Cart.getInstance().getCart().get(Cart.SERVICES);
        ArrayList<BulkGas> bulkGases = (ArrayList<BulkGas>) Cart.getInstance().getCart().get(Cart.BULK_GAS);
        ArrayList<Accessory> accessories = (ArrayList<Accessory>) Cart.getInstance().getCart().get(Cart.ACCESSORIES);

        if(preference.getOrderKey() == null) {
            orderData.requestOrderKey();
        }else {
            Log.i("DETA", preference.getOrderKey());

            for (Gas gas : gases) {
                orderData.placeGasOrder(Cart.GASES, gas);
                Log.i("ORDER", gas.getName());
            }

            for (Service service : services) {
                orderData.placeServiceOrder(Cart.SERVICES, service);
                Log.i("ORDER", service.getName());
            }

            for (BulkGas gas : bulkGases) {
                orderData.placeBulkGasOrder(Cart.BULK_GAS, gas);
                Log.i("ORDER", gas.getId());
            }

            for (Accessory accessory : accessories) {
                orderData.placeOrder(Cart.ACCESSORIES, accessory);
                Log.i("ORDER", accessory.getName());
            }
//            orderData.requestOrderKey();

//            gases.clear();
//            services.clear();
//            bulkGases.clear();
//            accessories.clear();
//            Intent intent = new Intent(context, PreviousPurchasesActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
        }
    }

}
