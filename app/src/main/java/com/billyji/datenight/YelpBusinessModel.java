package com.billyji.datenight;

import android.location.Location;

import com.yelp.fusion.client.models.Business;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class YelpBusinessModel
{
    private List<Business> m_fiveRandomBusinesses;
    private List<Business> m_allBusinesses;
    private Location m_userLocation;

    YelpBusinessModel(List<Business> allBusinesses)
    {
        this.m_allBusinesses = allBusinesses;
        m_userLocation = new Location("User Location");

        m_userLocation.setLatitude(LocationGetter.getLatitudeLast());
        m_userLocation.setLongitude(LocationGetter.getLongitudeLast());
    }

    void getFiveBusinesses()
    {
        m_fiveRandomBusinesses = new ArrayList<>();
        Random r = new Random();
        //Counter variable used in case there aren't 5 restaurants in the area
        int count = 0;

        while (m_fiveRandomBusinesses.size() < 5 && m_allBusinesses.size() > 0
            && count < 50)
        {
            int randomBusiness = r.nextInt(m_allBusinesses.size());
            Business curBusiness = m_allBusinesses.get(randomBusiness);

            if (withinParameters(curBusiness) && !m_fiveRandomBusinesses.contains(curBusiness))
            {
                addBusiness(curBusiness);
            }

            count++;
        }
    }

    private boolean withinParameters(Business business)
    {
        return business.getRating() >= FoodSelectionDetails.getMinStars()
            && business.getPrice().length() <= Double.parseDouble(FoodSelectionDetails.getMaxPrice());
    }

    private void addBusiness(Business business)
    {
        m_fiveRandomBusinesses.add(business);
    }

    int getBusinessListSize()
    {
        return m_fiveRandomBusinesses.size();
    }

    List<Business> getBusinesses()
    {
        return m_fiveRandomBusinesses;
    }

    boolean removeBusiness(int position)
    {
        if (m_fiveRandomBusinesses.size() == 1 || position >= m_fiveRandomBusinesses.size())
        {
            return false;
        }

        m_fiveRandomBusinesses.remove(position);
        return true;
    }

    String getDistance(Business business)
    {
        Location restaurantLocation = new Location("Restaurant Location");

        restaurantLocation.setLatitude(business.getCoordinates().getLatitude());
        restaurantLocation.setLongitude(business.getCoordinates().getLongitude());

        float distance = m_userLocation.distanceTo(restaurantLocation);
        return processDistance(distance);
    }

    private String processDistance(float distance)
    {
        distance = distance / 1609;
        return String.format("%.1f", distance) + " mi";
    }
}
