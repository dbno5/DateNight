package com.billyji.datenight;

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

public class YelpRunner {

	public static final String WE_FETCHED_DATA = "We fetched data...";
	private static final String CLIENT_ID = "gQy70Nnii0ZWnS-LfcwiPg";
	private static final String CLIENT_SECRET = "SVq93NKM4nKfU8NLEzcFkCottzAGEJ0c63i2tNcHOloqWgQX6jFVwy7vyV9hQlZB";

	private double latitudeToSearch;
	private double longitudeToSearch;

	public static List<Business> listBusinesses=new ArrayList<>();

	private boolean usedDefaultLocation=false;

	public YelpRunner(double latitudeToSearch, double longitudeToSearch) {

		this.latitudeToSearch = latitudeToSearch;
		this.longitudeToSearch = longitudeToSearch;
	}

	public String getDataFromYelp(){
		YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();

		try {
			YelpFusionApi yelpFusionApi = apiFactory.createAPI(CLIENT_ID, CLIENT_SECRET);
			Map<String, String> params = new HashMap<>();


			//Our default locations
			if (latitudeToSearch==0 & longitudeToSearch==0){
				usedDefaultLocation=true;
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Using default location - Terrigal Australia");

				//New York - Upper West Side
				//params.put("latitude", "40.7150");
				//params.put("longitude", "-73.9839");

				//Williamsburg - we are feeling hip
				//params.put("latitude", "40.7081");
				//params.put("longitude", "-73.9571");

				//Terrigal - Welcome to Australia
				params.put("latitude", "-33.4462");
				params.put("longitude", "151.4447");
			}
			else{
				params.put("latitude",Double.toString(latitudeToSearch));
				params.put("longitude",Double.toString(longitudeToSearch));

			}

			//Set some additional parameters
			params.put("radius", "8500"); //5 miles
			params.put("limit","25");
			params.put("sort_by","distance");
			params.put("term","restaurants");

			//Use the API
			Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
			Response<SearchResponse> response = call.execute();


			if (response.code()!=200){
				//We could no get data
				return "Loading page error-"+response.code();
			}

			//ArrayList<Business> businesses = response.body().getBusinesses();
			
			listBusinesses=response.body().getBusinesses();

			//System.out.println("Number of restaurants found:"+businesses.size());


		} catch (IOException e) {
			if (AppConstant.DEBUG) Log.e(this.getClass().getSimpleName()+">","We had an error:"+"Error:"+e.getMessage());
			e.printStackTrace();
			return "We had an error:"+e.getMessage();
		}

		return WE_FETCHED_DATA;
	}

	public static void main(String[] args) {
		//YelpRunner testYelp=new YelpRunner();
		//testYelp.getDataFromYelp();
		//System.exit(1);
	}

	public boolean isUsedDefaultLocation() {
		return usedDefaultLocation;
	}
}
