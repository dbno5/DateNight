package com.billyji.datenight.network;

import android.content.Context;

import com.billyji.datenight.data.FoodSelectionDataModel;
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

public class YelpDataGetter
{
    public static final String DATA_FETCHED = "Successfully got data";
    public static final String NO_BUSINESSES_FOUND = "No businesses found";
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    public static List<Business> m_listBusinesses = new ArrayList<>();
    private Map<String, String> m_yelpParams;

    private final double m_latitudeToSearch;
    private final double m_longitudeToSearch;
    private static boolean m_usedDefaultLocation;

    public YelpDataGetter(double latitudeToSearch, double longitudeToSearch, Context context)
    {
        this.m_latitudeToSearch = latitudeToSearch;
        this.m_longitudeToSearch = longitudeToSearch;
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
            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(m_yelpParams);
            Response<SearchResponse> response = call.execute();

            if (response.code() != 200)
            {
                return "Loading page error-" + response.code();
            }

            m_listBusinesses = response.body().getBusinesses();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "We had an error:" + e.getMessage();
        }

        if(m_listBusinesses.size() == 0)
        {
            return NO_BUSINESSES_FOUND;
        }

        return DATA_FETCHED;
    }

    private void setUpParams()
    {
        m_yelpParams = new HashMap<>();

        //Use default location if no location provided(San Diego, CA)
        if (m_latitudeToSearch == 0 && m_longitudeToSearch == 0)
        {
            m_usedDefaultLocation = true;
            m_yelpParams.put("latitude", "32.7157");
            m_yelpParams.put("longitude", "-117.071869");
        }
        else
        {
            m_yelpParams.put("latitude", Double.toString(m_latitudeToSearch));
            m_yelpParams.put("longitude", Double.toString(m_longitudeToSearch));
        }

        //Convert miles to meters
        String radius = Integer.toString((1609 * FoodSelectionDataModel.getMaxDistance()));

        //Set some additional parameters
        m_yelpParams.put("radius", radius); //5 miles
        m_yelpParams.put("limit", "50");
        m_yelpParams.put("sort_by", "distance");
        m_yelpParams.put("term", "restaurants");
    }

    public static boolean isUsedDefaultLocation()
    {
        return m_usedDefaultLocation;
    }
}
