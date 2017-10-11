package com.billyji.datenight.network;

import android.content.Context;

import com.billyji.datenight.data.FoodSelectionDetails;
import com.billyji.datenight.R;
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
    public static final String DATA_FETCHED = "Successfully got data";
    public static final String NO_BUSINESSES_FOUND = "No businesses found";
    public static List<Business> listBusinesses = new ArrayList<>();
    private Map<String, String> params;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private double latitudeToSearch;
    private double longitudeToSearch;
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

            setUpParams();
            //Use the API
            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            Response<SearchResponse> response = call.execute();

            if (response.code() != 200)
            {
                return "Loading page error-" + response.code();
            }

            listBusinesses = response.body().getBusinesses();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "We had an error:" + e.getMessage();
        }

        if(listBusinesses.size() == 0)
        {
            return NO_BUSINESSES_FOUND;
        }

        return DATA_FETCHED;
    }

    private void setUpParams()
    {
        params = new HashMap<>();

        //Use default location if no location provided(San Diego, CA)
        if (latitudeToSearch == 0 && longitudeToSearch == 0)
        {
            usedDefaultLocation = true;
            params.put("latitude", "32.7157");
            params.put("longitude", "-117.071869");
        }
        else
        {
            params.put("latitude", Double.toString(latitudeToSearch));
            params.put("longitude", Double.toString(longitudeToSearch));
        }

        //Convert miles to meters
        String radius = Integer.toString((1609 * FoodSelectionDetails.getMaxDistance()));

        //Set some additional parameters
        params.put("radius", radius); //5 miles
        params.put("limit", "50");
        params.put("sort_by", "distance");
        params.put("term", "restaurants");
    }

    public boolean isUsedDefaultLocation()
    {
        return usedDefaultLocation;
    }
}
