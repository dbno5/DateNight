package com.billyji.datenight.ui

import android.Manifest
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
        setUpSpinners()
        setUpFlipperAndPageIndicator()
    }

    override fun onStart() {
        super.onStart()

        checkForPermissions()
        find_food.setOnClickListener {
            callYelpAPI()
        }
        find_food_default.setOnClickListener {
            callYelpAPI()
        }
    }

    override fun onResume() {
        super.onResume()
        logo_image_view.isAnimating = false
    }

    private fun setUpSpinners() {
        var adapter = ArrayAdapter.createFromResource(this,
                R.array.distance_options, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        distance_spinner.setAdapter(adapter)
        distance_spinner.selectedIndex = 2

        adapter = ArrayAdapter.createFromResource(this,
                R.array.price_options, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        price_spinner.setAdapter(adapter)
        price_spinner.selectedIndex = 1

        adapter = ArrayAdapter.createFromResource(this,
                R.array.stars_options, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        stars_spinner.setAdapter(adapter)
        stars_spinner.selectedIndex = 2
    }

    private fun setUpFlipperAndPageIndicator() {
        yelp_options_flipper.displayedChild = 2
        val circlePageIndicator: CirclePageIndicator = findViewById(R.id.circlePageIndicator)
        circlePageIndicator.setViewFlipper(yelp_options_flipper)
        entire_screen.setOnTouchListener(OnSwipeTouchListener(applicationContext, yelp_options_flipper, circlePageIndicator))
    }

    private fun checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
            else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        }
    }
    private fun setFoodSelectionDetails() {
        FoodSelectionDataModel.maxDistance = distance_spinner.selectedIndex.toString().toInt()
        FoodSelectionDataModel.maxDollarSigns = price_spinner.selectedIndex.toString().toInt() + 1
        //Need to add 1 to index to match number of stars(Index 0 == 1 star)
        FoodSelectionDataModel.minStars = stars_spinner.selectedIndex.toString().toDouble() + 1
    }

    private fun callYelpAPI() {
        setFoodSelectionDetails()
        if (!checkConnection()) return
        LocationGetter.getLocation(this)
        logo_image_view.isAnimating = true
        DownloadMessage(this).execute()
    }

    private fun checkConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?

        activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            val toast = Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        return isConnected
    }

    private class DownloadMessage internal constructor(context: MainActivity) : AsyncTask<URL, Int, String>() {
        internal var yelpDataGetter: YelpDataGetter? = null
        private val activityReference: WeakReference<MainActivity> = WeakReference(context)
        internal val apiKey = context.getString(R.string.api_key)

        override fun doInBackground(vararg urls: URL): String? {
            var dataFromYelp: String? = null

            try {
                yelpDataGetter = YelpDataGetter(LocationGetter.getLatitudeLast(), LocationGetter.getLongitudeLast(), apiKey)
                dataFromYelp = yelpDataGetter!!.dataFromYelp
            } catch (e: Exception) {
                e.printStackTrace()
                onCancelled()
            }

            return dataFromYelp
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

            val intent = Intent(activityReference.get(), FoodChoiceActivity::class.java)
            activityReference.get()?.startActivity(intent)
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 24
    }
}
