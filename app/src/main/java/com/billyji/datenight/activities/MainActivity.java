package com.billyji.datenight.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import com.billyji.datenight.FoodSelectionDetails;
import com.billyji.datenight.LocationGetter;

import android.Manifest;

import com.billyji.datenight.R;
import com.billyji.datenight.YelpRunner;

import java.lang.ref.WeakReference;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 24;
    private static ProgressDialog progressDialog;

    @BindView(R.id.max_distance)
    EditText m_maxDistance;
    @BindView(R.id.min_stars)
    EditText m_minStars;
    @BindView(R.id.max_price)
    EditText m_maxPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Permissions!
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION))
            {

                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.find_food)
    void findFood()
    {
        setFoodSelectionDetails();
        if (!checkConnection())
        {
            return;
        }
        LocationGetter.getLocation(this);
        new DownloadMessage(this).execute();
    }

    private void setFoodSelectionDetails()
    {
        FoodSelectionDetails.setMaxDistance(m_maxDistance.getText().length() == 0 ?
            5 : Double.parseDouble(m_maxDistance.getText().toString()));
        FoodSelectionDetails.setMinStars(m_minStars.getText().length() == 0 ?
            3 : Double.parseDouble(m_minStars.getText().toString()));
        FoodSelectionDetails.setMaxPrice(m_maxPrice.getText().length() == 0 ?
            "2" : m_maxPrice.getText().toString());
    }

    public boolean checkConnection()
    {
        ConnectivityManager cm =
            (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;

        if (cm != null)
        {
            activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

            if (!isConnected)
            {
                Toast toast = Toast.makeText(this, "There is no network connection.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return isConnected;
        }
        return false;
    }

    private static class DownloadMessage extends AsyncTask<URL, Integer, String>
    {

        YelpRunner yelpRunner;
        private WeakReference<MainActivity> activityReference;

        DownloadMessage(MainActivity context)
        {
            activityReference = new WeakReference<>(context);
        }

        protected String doInBackground(URL... urls)
        {
            String dataFromYelp = null;

            try
            {
                //Gets the restaurant data using coordinates.
                yelpRunner = new YelpRunner(LocationGetter.getLatitudeLast(), LocationGetter.getLongitudeLast(), activityReference.get());
                dataFromYelp = yelpRunner.getDataFromYelp();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return dataFromYelp;
        }

        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(activityReference.get());
            progressDialog.setMessage("Finding you some eats…");
            progressDialog.show();
        }

        protected void onPostExecute(String result)
        {
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            if(result.equals(YelpRunner.NO_BUSINESSES_FOUND))
            {
                Toast.makeText(activityReference.get(), "We could not find any businesses nearby you", Toast.LENGTH_LONG).show();
                return;
            }

            if (!result.equals(YelpRunner.DATA_FETCHED))
            {
                Toast.makeText(activityReference.get(), "We could not get data:" + result, Toast.LENGTH_LONG).show();
                return;
            }

            if (yelpRunner.isUsedDefaultLocation())
            {
                Toast.makeText(activityReference.get(), "Could not get location...default location was used...", Toast.LENGTH_LONG).show();
            }


            Intent intent = new Intent(activityReference.get(), FoodChoiceActivity.class);
            activityReference.get().startActivity(intent);
        }
    }
}
