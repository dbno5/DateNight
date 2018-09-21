package com.billyji.datenight.ui

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import com.billyji.datenight.R
import com.billyji.datenight.data.FoodSelectionDataModel
import com.billyji.datenight.network.LocationGetter
import com.billyji.datenight.network.YelpDataGetter
import com.billyji.datenight.ui.ViewIndicator.CirclePageIndicator

import java.lang.ref.WeakReference
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an ArrayAdapter using the string array and a default spinner layout
        var adapter = ArrayAdapter.createFromResource(this,
                R.array.distance_options, R.layout.spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        // Apply the adapter to the spinner
        distance_spinner.setAdapter(adapter)
        distance_spinner.selectedIndex = 2

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.price_options, R.layout.spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        // Apply the adapter to the spinner
        price_spinner.setAdapter(adapter)
        price_spinner.selectedIndex = 1
        price_spinner.setTextColor(ContextCompat.getColor(this, R.color.dollar_sign_color))

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.stars_options, R.layout.spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        // Apply the adapter to the spinner
        stars_spinner.setAdapter(adapter)
        stars_spinner.selectedIndex = 2
        stars_spinner.setTextColor(ContextCompat.getColor(this, R.color.stars_color))

//        val listener: View.OnTouchListener = OnSwipeTouchListener(applicationContext)
        yelp_options_flipper.displayedChild = 2
        val circlePageIndicator: CirclePageIndicator = findViewById(R.id.circlePageIndicator)
        circlePageIndicator.setViewFlipper(yelp_options_flipper)

        entire_screen.setOnTouchListener(OnSwipeTouchListener(applicationContext, yelp_options_flipper, circlePageIndicator))

        //Permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        }

        find_food.setOnClickListener {
            setFoodSelectionDetails()
            if (!checkConnection()) { Unit }
            LocationGetter.getLocation(this)
            DownloadMessage(this).execute()
        }
        find_food_default.setOnClickListener {
            setFoodSelectionDetails()
            if (!checkConnection()) { Unit }
            LocationGetter.getLocation(this)
            DownloadMessage(this).execute()
        }
    }

    override fun onPause() {
        super.onPause()
        if (m_progressDialog != null) {
            m_progressDialog!!.dismiss()
        }
    }

    private fun setFoodSelectionDetails() {
        FoodSelectionDataModel.setMaxDistance(distance_spinner.selectedIndex.toString().toInt())
        FoodSelectionDataModel.setMinStars(stars_spinner.selectedIndex.toString().toDouble())
        FoodSelectionDataModel.setMaxPrice(price_spinner.selectedIndex.toString())
    }

    private fun checkConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?

        if (cm != null) {
            activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

            if (!isConnected) {
                val toast = Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
            return isConnected
        }
        return false
    }

    private class DownloadMessage internal constructor(context: MainActivity) : AsyncTask<URL, Int, String>() {
        internal var m_yelpDataGetter: YelpDataGetter? = null
        private val activityReference: WeakReference<MainActivity>

        init {
            activityReference = WeakReference(context)
        }

        override fun doInBackground(vararg urls: URL): String? {
            var dataFromYelp: String? = null

            try {
                m_yelpDataGetter = YelpDataGetter(LocationGetter.getLatitudeLast(), LocationGetter.getLongitudeLast(), activityReference.get())
                dataFromYelp = m_yelpDataGetter!!.dataFromYelp
            } catch (e: Exception) {
                e.printStackTrace()
                onCancelled()
            }

            return dataFromYelp
        }

        override fun onPreExecute() {
            m_progressDialog = ProgressDialog(activityReference.get())
            m_progressDialog.setMessage(activityReference.get()?.getString(R.string.finding_food_notification))
            m_progressDialog.show()
        }

        override fun onCancelled() {
            if (m_progressDialog.isShowing) {
                m_progressDialog.dismiss()
            }
        }

        override fun onPostExecute(result: String) {
            if (result == YelpDataGetter.NO_BUSINESSES_FOUND) {
                Toast.makeText(activityReference.get(), R.string.no_businesses_found, Toast.LENGTH_LONG).show()
                return
            }

            if (result != YelpDataGetter.DATA_FETCHED) {
                Toast.makeText(activityReference.get(), R.string.no_data_received.toString() + result, Toast.LENGTH_LONG).show()
                return
            }

            if (YelpDataGetter.isUsedDefaultLocation()) {
                Toast.makeText(activityReference.get(), R.string.using_default_location, Toast.LENGTH_LONG).show()
            }

            val intent = Intent(activityReference.get(), FoodChoiceActivity::class.java)
            activityReference.get()?.startActivity(intent)
        }
    }

    companion object {
        private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 24
        private lateinit var m_progressDialog: ProgressDialog
    }
}
