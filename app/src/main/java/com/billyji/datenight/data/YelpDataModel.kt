package com.billyji.datenight.data

import android.location.Location

import com.billyji.datenight.network.LocationGetter
import com.yelp.fusion.client.models.Business

import java.util.ArrayList
import java.util.Locale
import java.util.Random

class YelpDataModel(private val m_allBusinesses: List<Business>) {
    private var fiveRandomBusinesses: MutableList<Business>? = null
    private val userLocation: Location = Location("User Location")

    val businessListSize: Int
        get() = fiveRandomBusinesses!!.size

    val businesses: List<Business>?
        get() = fiveRandomBusinesses

    init {
        userLocation.latitude = LocationGetter.getLatitudeLast()
        userLocation.longitude = LocationGetter.getLongitudeLast()
    }

    fun getFiveBusinesses() {
        fiveRandomBusinesses = ArrayList()
        val r = Random()
        //Counter variable used in case there aren't 5 restaurants in the area
        var count = 0
        while (fiveRandomBusinesses!!.size < 5 && m_allBusinesses.isNotEmpty()
                && count < 50) {
            val randomBusiness = r.nextInt(m_allBusinesses.size)
            val curBusiness = m_allBusinesses[randomBusiness]

            if (withinParameters(curBusiness) && !fiveRandomBusinesses!!.contains(curBusiness)) {
                addBusiness(curBusiness)
            }

            count++
        }

    }

    private fun withinParameters(business: Business): Boolean {
        return business.price != null &&
                business.rating >= FoodSelectionDataModel.minStars &&
                business.price.length <= FoodSelectionDataModel.maxDollarSigns
    }

    private fun addBusiness(business: Business) {
        fiveRandomBusinesses!!.add(business)
    }

    fun removeBusiness(position: Int): Boolean {
        if (fiveRandomBusinesses!!.size == 1 || position >= fiveRandomBusinesses!!.size) {
            return false
        }

        fiveRandomBusinesses!!.removeAt(position)
        return true
    }

    fun getDistance(business: Business): String {
        val restaurantLocation = Location("Restaurant Location")

        restaurantLocation.latitude = business.coordinates.latitude
        restaurantLocation.longitude = business.coordinates.longitude

        val distance = userLocation.distanceTo(restaurantLocation)
        return processDistance(distance)
    }

    private fun processDistance(dist: Float): String {
        var distance = dist
        distance /= 1609
        return String.format(Locale.US, "%.1f", distance) + " mi"
    }
}
