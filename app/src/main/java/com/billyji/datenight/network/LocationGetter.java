package com.billyji.datenight.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class LocationGetter
{
    private static LocationListener m_locationListener;
    private static LocationManager m_locationManager;
    private static double m_latitudeLast;
    private static double m_longitudeLast;

    public static void getLocation(Context context)
    {
        m_locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            m_latitudeLast = 0;
            m_longitudeLast = 0;
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setSpeedRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String bestProvider = m_locationManager.getBestProvider(criteria, true);

        if (m_locationManager.isProviderEnabled(bestProvider))
        {
            m_locationListener = new LocationListenerCustom();
            m_locationManager.requestSingleUpdate(bestProvider, m_locationListener, null);
            Location lastKnownLocation = m_locationManager.getLastKnownLocation(bestProvider);
            if (lastKnownLocation != null)
            {
                m_latitudeLast = lastKnownLocation.getLatitude();
                m_longitudeLast = lastKnownLocation.getLongitude();
            }
        }
    }

    private static void stopUpdateRequest()
    {
        m_locationManager.removeUpdates(m_locationListener);
    }

    public static double getLatitudeLast()
    {
        return m_latitudeLast;
    }

    public static double getLongitudeLast()
    {
        return m_longitudeLast;
    }

    private static class LocationListenerCustom implements LocationListener
    {
        /**
         * Called when the location has changed.
         * <p/>
         * <p> There are no restrictions on the use of the supplied Location object.
         *
         * @param location The new location, as a Location object.
         */
        @Override
        public void onLocationChanged(Location location)
        {
            stopUpdateRequest();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }

        /**
         * Called when the provider is enabled by the user.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */
        @Override
        public void onProviderEnabled(String provider)
        {
        }

        /**
         * Called when the provider is disabled by the user. If requestLocationUpdates
         * is called on an already disabled provider, this method is called
         * immediately.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */
        @Override
        public void onProviderDisabled(String provider)
        {
        }
    }
}

