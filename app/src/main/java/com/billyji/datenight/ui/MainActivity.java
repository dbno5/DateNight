package com.billyji.datenight.ui;

import android.Manifest;
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

import com.billyji.datenight.R;
import com.billyji.datenight.activities.NetflixActivity;
import com.billyji.datenight.data.FoodSelectionDataModel;
import com.billyji.datenight.network.LocationGetter;
import com.billyji.datenight.network.YelpDataGetter;

import java.lang.ref.WeakReference;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.max_distance)
    EditText m_maxDistance;
    @BindView(R.id.min_stars)
    EditText m_minStars;
    @BindView(R.id.max_price)
    EditText m_maxPrice;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 24;
    private static final int DEFAULT_DISTANCE = 5;
    private static final double DEFAULT_STARS = 3;
    private static final String DEFAULT_PRICE = "2";
    private static ProgressDialog m_progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Permissions
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
    protected void onPause()
    {
        super.onPause();
        if (m_progressDialog != null)
        {
            m_progressDialog.dismiss();
        }
    }

    @OnClick(R.id.find_food)
    void findFood()
    {
        Intent intent = new Intent(this, NetflixActivity.class);
        startActivity(intent);
//        setFoodSelectionDetails();
//        if(!checkConnection())
//        {
//            return;
//        }
//        LocationGetter.getLocation(this);
//        new DownloadMessage(this).execute();
    }

    private void setFoodSelectionDetails()
    {
        FoodSelectionDataModel.setMaxDistance(m_maxDistance.getText().length() == 0 ?
            DEFAULT_DISTANCE : Double.parseDouble(m_maxDistance.getText().toString()));

        FoodSelectionDataModel.setMinStars(m_minStars.getText().length() == 0 ?
            DEFAULT_STARS : Double.parseDouble(m_minStars.getText().toString()));

        FoodSelectionDataModel.setMaxPrice(m_maxPrice.getText().length() == 0 ?
            DEFAULT_PRICE : m_maxPrice.getText().toString());
    }

    private boolean checkConnection()
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
                Toast toast = Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            return isConnected;
        }
        return false;
    }

    private static class DownloadMessage extends AsyncTask<URL, Integer, String>
    {

        YelpDataGetter m_yelpDataGetter;
        private final WeakReference<MainActivity> activityReference;

        DownloadMessage(MainActivity context)
        {
            activityReference = new WeakReference<>(context);
        }

        protected String doInBackground(URL... urls)
        {
            String dataFromYelp = null;

            try
            {
                m_yelpDataGetter = new YelpDataGetter(LocationGetter.getLatitudeLast(), LocationGetter.getLongitudeLast(), activityReference.get());
                dataFromYelp = m_yelpDataGetter.getDataFromYelp();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                onCancelled();
            }

            return dataFromYelp;
        }

        @Override
        protected void onPreExecute()
        {
            m_progressDialog = new ProgressDialog(activityReference.get());
            m_progressDialog.setMessage(activityReference.get().getString(R.string.finding_food_notification));
            m_progressDialog.show();
        }

        @Override
        protected void onCancelled()
        {
            if (m_progressDialog.isShowing())
            {
                m_progressDialog.dismiss();
            }
        }

        protected void onPostExecute(String result)
        {
            if(result.equals(YelpDataGetter.NO_BUSINESSES_FOUND))
            {
                Toast.makeText(activityReference.get(), R.string.no_businesses_found, Toast.LENGTH_LONG).show();
                return;
            }

            if (!result.equals(YelpDataGetter.DATA_FETCHED))
            {
                Toast.makeText(activityReference.get(), R.string.no_data_received + result, Toast.LENGTH_LONG).show();
                return;
            }

            if (YelpDataGetter.isUsedDefaultLocation())
            {
                Toast.makeText(activityReference.get(), R.string.using_default_location, Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(activityReference.get(), FoodChoiceActivity.class);
            activityReference.get().startActivity(intent);
        }
    }
}
