package com.billyji.datenight;

import android.content.Context;
import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class YelpRunner
{

    public static final String WE_FETCHED_DATA = "We fetched data...";
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    private double latitudeToSearch;
    private double longitudeToSearch;

    static List<Business> listBusinesses = new ArrayList<>();

    private boolean usedDefaultLocation = false;

    public YelpRunner(double latitudeToSearch, double longitudeToSearch, Context context)
    {

        this.latitudeToSearch = latitudeToSearch;
        this.longitudeToSearch = longitudeToSearch;
        CLIENT_ID = context.getString(R.string.client_id);
        CLIENT_SECRET = context.getString(R.string.client_secret);
    }

    public String getDataFromYelp()
    {
        YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();

        try
        {
            YelpFusionApi yelpFusionApi = apiFactory.createAPI(CLIENT_ID, CLIENT_SECRET);
            Map<String, String> params = new HashMap<>();

            //Our default locations
            if (latitudeToSearch == 0 && longitudeToSearch == 0)
            {
                Log.e(Double.toString(latitudeToSearch), Double.toString(longitudeToSearch));
                usedDefaultLocation = true;
                params.put("latitude", "32.7157");
                params.put("longitude", "-117.071869");
            }
            else
            {
                params.put("latitude", Double.toString(latitudeToSearch));
                params.put("longitude", Double.toString(longitudeToSearch));

            }

            //Set some additional parameters
            params.put("radius", "8500"); //5 miles
            params.put("limit", "50");
            params.put("sort_by", "distance");
            params.put("term", "restaurants");

            //Use the API
            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            Response<SearchResponse> response = call.execute();

            if (response.code() != 200)
            {
                //We could not get data
                return "Loading page error-" + response.code();
            }

            listBusinesses = response.body().getBusinesses();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "We had an error:" + e.getMessage();
        }

        return WE_FETCHED_DATA;
    }

    public boolean isUsedDefaultLocation()
    {
        return usedDefaultLocation;
    }
}
