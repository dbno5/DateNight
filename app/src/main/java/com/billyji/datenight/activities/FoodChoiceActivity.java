package com.billyji.datenight.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.billyji.datenight.FoodChoiceListAdapter;
import com.billyji.datenight.FoodSelectionDetails;
import com.billyji.datenight.R;
import com.billyji.datenight.YelpRunner;
import com.yelp.fusion.client.models.Business;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FoodChoiceActivity extends AppCompatActivity
{
    private List<Business> fiveRandomBusinesses;
    private final List<String> restaurantReference = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choices);

        getFiveBusinesses();

        FoodChoiceListAdapter adapter=new FoodChoiceListAdapter(this, fiveRandomBusinesses, restaurantReference);
        ListView list= findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= restaurantReference.get(+position);
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
        }
        });
    }

    private void getFiveBusinesses()
    {
        fiveRandomBusinesses = new ArrayList<>();
        Random r = new Random();

        while(fiveRandomBusinesses.size() < 5 && YelpRunner.listBusinesses.size() > 0)
        {
            int randomBusiness = r.nextInt(YelpRunner.listBusinesses.size());
            Business curBusiness = YelpRunner.listBusinesses.get(randomBusiness);

            if(withinParameters(curBusiness))
            {
                fiveRandomBusinesses.add(curBusiness);
                restaurantReference.add(curBusiness.getName());
                YelpRunner.listBusinesses.remove(randomBusiness);
            }
        }
    }

    private boolean withinParameters(Business business)
    {
        return business.getRating() > FoodSelectionDetails.getMinRating()
            && business.getPrice().length() < Integer.parseInt(FoodSelectionDetails.getMaxPrice());

    }
}


