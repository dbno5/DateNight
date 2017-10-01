package com.billyji.datenight.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import com.billyji.datenight.AppConstant;
import com.billyji.datenight.FoodSelectionDetails;
import com.billyji.datenight.LocationGetter;
import android.Manifest;

import com.billyji.datenight.NoLocationAlertDialogFragment;
import com.billyji.datenight.UtilityFood;

import com.billyji.datenight.R;
import com.billyji.datenight.YelpRunner;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

	private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 24;
	private UtilityFood utilityFood=new UtilityFood();
	private final Context context = this;
	private ProgressDialog progressDialog;

	@BindView(R.id.max_distance) EditText m_maxDistance;
	@BindView(R.id.min_stars) EditText m_minStars;
	@BindView(R.id.max_price) EditText m_maxPrice;
	//@BindView(R.id.min_rating) EditText m_minRating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		//Permissions!
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.ACCESS_FINE_LOCATION)) {

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
			} else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
			}
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progressDialog!=null){
			progressDialog.dismiss();
		}
	}

	@OnClick(R.id.find_food)
	void findFood(){
				boolean haveConnection = utilityFood.checkConnection(this);

				if (!haveConnection){
					return;
				}

				setFoodSelectionDetails();

				boolean locationEnabled = LocationGetter.getLocation(this);

				if (locationEnabled) {
					new DownloadMessage().execute();
				}
				else{
					{
						DialogFragment newFragment = NoLocationAlertDialogFragment.newInstance(
								R.string.alert_dialog_two_buttons_title);
						newFragment.show(getSupportFragmentManager(), "dialog");

					}
				}
			}


	public void doPositiveClick() {
		new DownloadMessage().execute();
	}

	private void setFoodSelectionDetails()
	{
		FoodSelectionDetails.setMaxDistance(m_maxDistance.getText().length() == 0 ?
			5 : Double.parseDouble(m_maxDistance.getText().toString()));
		FoodSelectionDetails.setMinStars(Double.parseDouble(m_minStars.getText().toString()));
		FoodSelectionDetails.setMaxPrice(m_maxPrice.getText().toString());
		//FoodSelectionDetails.setMinRating(Double.parseDouble(m_minRating.getText().toString()));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length == 0
						&& grantResults[0] != PackageManager.PERMISSION_GRANTED) {
					UtilityFood.AlertMessageSimple(this,getString(R.string.permission_request_title),getString(R.string.showmoreinforforrequest));
				}
			}
		}
	}

	private class DownloadMessage extends AsyncTask<URL, Integer, String> {

		YelpRunner yelpRunner;

		protected String doInBackground(URL... urls) {

			String dataFromYelp=null;

			try {
					//Gets the restaurant data using coordinates.
					yelpRunner=new YelpRunner(LocationGetter.getLatitudeLast(),LocationGetter.getLongitudeLast());
					dataFromYelp = yelpRunner.getDataFromYelp();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return dataFromYelp;
		}

		@Override
		protected void onPreExecute() {
			progressDialog=new ProgressDialog(context);
			progressDialog.setMessage(getString(R.string.progess_dialog_text));
			progressDialog.show();
		}

		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}

		protected void onPostExecute(String result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (!result.equals(YelpRunner.WE_FETCHED_DATA)){
				UtilityFood.AlertMessageSimple(context,"Info","We could not get data:"+result);
				return;
			}


			if (yelpRunner.isUsedDefaultLocation()){
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Location default is used...");
				Toast.makeText(context,"Could not get location...default location was used...",Toast.LENGTH_LONG).show();
			}

				Intent intent=new Intent(context, FoodChoiceActivity.class);
				startActivity(intent);
		}
	}
}
