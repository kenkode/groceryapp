package com.softark.eddie.gasexpress.Retrofit;

import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.models.Location;
import com.softark.eddie.gasexpress.models.OrderHistory;
import com.softark.eddie.gasexpress.models.OrderItem;
import com.softark.eddie.gasexpress.models.RAccessory;
import com.softark.eddie.gasexpress.models.RBulkGas;
import com.softark.eddie.gasexpress.models.RGas;
import com.softark.eddie.gasexpress.models.RService;
import com.softark.eddie.gasexpress.models.User;
import com.softark.eddie.gasexpress.models.UserAuth;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET(Constants.GET_SIZES)
    Call<int[]> getSizes();

    @GET(Constants.GET_GASES)
    Call<List<RGas>> getGases(@Query("size") int size);

    @GET(Constants.GET_ACCESSORIES)
    Call<List<RAccessory>> getAccessories();

    @GET(Constants.GET_SERVICES)
    Call<List<RService>> getServices();

    @GET(Constants.GET_BULK)
    Call<Integer> getBulkPrice();

    @GET(Constants.GET_ORDERS)
    Call<List<OrderHistory>> getOrders(@Query("user") String user);

    @GET(Constants.GET_ORDER_ITEMS)
    Call<List<OrderItem>> getOrderItems(@Query("user") String user, @Query("id") String id);

    @GET(Constants.GET_MY_LOCATIONS)
    Call<List<Location>> getLocations(@Query("user") String user);

    @GET(Constants.ADD_LOCATION)
    Call<String> addLocation(@Query("location") String location, @Query("user") String user);

    @GET(Constants.DISABLE_LOCATION)
    Call<String> disableLocation(@Query("location") String location, @Query("user") String user);

    @GET(Constants.ADD_USER)
    Call<UserAuth> addUser(@Query("user") String user, @Query("location") String location);

    @GET(Constants.AUTH_USER)
    Call<UserAuth> authUser(@Query("phone") String phone);

    @GET(Constants.PLACE_ORDER)
    Call<ResponseBody> placeOrder(@Query("json") String json,
                                  @Query("user") String user,
                                  @Query("location") String location,
                                  @Query("payment") String payment);

}
