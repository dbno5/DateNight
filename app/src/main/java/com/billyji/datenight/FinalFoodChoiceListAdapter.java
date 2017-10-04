package com.billyji.datenight;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.billyji.datenight.activities.FoodChoiceActivity;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bj0716 on 10/3/17.
 */

public class FinalFoodChoiceListAdapter extends ArrayAdapter
{
    private final Activity context;

    private final Business business;
    private boolean test = true;

    public FinalFoodChoiceListAdapter(Activity context, List<String> listSizeReference, List<Business> lastRestaurant)
    {
        super(context, R.layout.food_list, listSizeReference);

        this.context = context;
        this.business = lastRestaurant.get(0);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView;
        if (test)
        {
            rowView = inflater.inflate(R.layout.food_list, null, true);
            setText(rowView, business);
            setImages(rowView, business);
            test = false;
        }
        else
        {
            rowView = inflater.inflate(R.layout.food_list, null, true);
            setText(rowView, business);
        }
        //rowView.setMinimumHeight(height/5);
        return rowView;
    }


    private void setImages(final View rowView, Business business)
    {
        ImageView foodPicture = rowView.findViewById(R.id.picture);
//        ImageButton removeOption = rowView.findViewById(R.id.remove_option);


        Picasso
            .with(context)
            .load(business.getImageUrl())
            .fit()
            .into(foodPicture);
//
//        Picasso
//            .with(context)
//            .load(R.drawable.x)
//            .fit()
//            .into(removeOption);
    }

    private void setText(View rowView, Business business)
    {
        TextView restaurantName = rowView.findViewById(R.id.name);
        restaurantName.setText(business.getName());

        TextView restaurantCategories = rowView.findViewById(R.id.categories);

        StringBuilder allCategories = new StringBuilder();
        for (Category category : business.getCategories())
        {
            allCategories.append(category.getTitle());
            allCategories.append(", ");
        }
        allCategories.deleteCharAt(allCategories.length() - 2);

        restaurantCategories.setText(allCategories);
    }
}
