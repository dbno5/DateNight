package com.billyji.datenight.network

import android.content.Context
import com.billyji.datenight.R
import com.billyji.datenight.data.FoodSelectionDataModel
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class YelpDataGetter(private val latitudeToSearch: Double, private val longitudeToSearch: Double, private val apiKey: String) {
    private var yelpParams: MutableMap<String, String>? = null

    //Use the API
    val dataFromYelp: String
        get() {
            val apiFactory = YelpFusionApiFactory()

            try {
                val yelpFusionApi = apiFactory.createAPI(apiKey)

                setUpParams()
                val call = yelpFusionApi.getBusinessSearch(yelpParams)
                val response = call.execute()

                if (response.code() != 200) {
                    return "Loading page error-" + response.code()
                }

                m_listBusinesses = response.body().businesses
            } catch (e: IOException) {
                e.printStackTrace()
                return "We had an error:" + e.message
            }

            return if (m_listBusinesses.isEmpty()) {
                NO_BUSINESSES_FOUND
            } else DATA_FETCHED

        }

    private fun setUpParams() {
        yelpParams = HashMap()

        //Use default location if no location provided(San Diego, CA)
        if (latitudeToSearch == 0.0 && longitudeToSearch == 0.0) {
            isUsedDefaultLocation = true
            yelpParams!!["latitude"] = "32.7157"
            yelpParams!!["longitude"] = "-117.071869"
        } else {
            yelpParams!!["latitude"] = java.lang.Double.toString(latitudeToSearch)
            yelpParams!!["longitude"] = java.lang.Double.toString(longitudeToSearch)
        }

        //Convert miles to meters
        val radius = Integer.toString(1609 * FoodSelectionDataModel.maxDistance)

        //Set some additional parameters
        yelpParams!!["radius"] = radius //5 miles
        yelpParams!!["limit"] = "50"
        yelpParams!!["sort_by"] = "distance"
        yelpParams!!["term"] = "restaurants"
    }

    companion object {
        const val DATA_FETCHED = "Successfully got data"
        const val NO_BUSINESSES_FOUND = "No businesses found"

        var m_listBusinesses: List<Business> = ArrayList()
        var isUsedDefaultLocation: Boolean = false
            private set
    }
}
