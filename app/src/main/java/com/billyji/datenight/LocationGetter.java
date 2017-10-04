package com.billyji.datenight;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class LocationGetter
{

    private static LocationListener locationListener;
    private static LocationManager locationManager;


    private static final int MAX_NUMBER_OF_ADDRESS = 5;

    private static final String PERMISSION_MESSAGE = "Location permission needs to be granted to use this app. You can go into Device " +
        "settings->App->This app-> and Permissions";

    private static double latitudeLast;
    private static double longitudeLast;


    public static void getLocation(Context context)
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            latitudeLast = 0;
            longitudeLast = 0;
            Toast.makeText(context, PERMISSION_MESSAGE, Toast.LENGTH_SHORT).show();
            return;
        }

        //We want to try to use network.
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setSpeedRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (locationManager.isProviderEnabled(bestProvider))
        {
            locationListener = new LocationListenerCustom(context);
            locationManager.requestSingleUpdate(bestProvider, locationListener, null);
            Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
            if (lastKnownLocation != null)
            {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();

                List<Address> listOfAddresses = AppUtility.getAddressesFromGeoCoder(context, latitude, longitude, MAX_NUMBER_OF_ADDRESS);

                if (!listOfAddresses.isEmpty())
                {
                    //We just want to get the first latitude and longitude
                    latitudeLast = latitude;
                    longitudeLast = longitude;
                }
            }
        }
    }


    private static void stopUpdateRequest()
    {
        locationManager.removeUpdates(locationListener);
    }


    private static class LocationListenerCustom implements LocationListener
    {

        final Context context;

        LocationListenerCustom(Context context)
        {
            this.context = context;
        }

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

    public static double getLatitudeLast()
    {
        return latitudeLast;
    }

    public static double getLongitudeLast()
    {
        return longitudeLast;
    }

}

