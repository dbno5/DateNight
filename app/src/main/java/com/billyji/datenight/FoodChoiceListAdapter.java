package com.billyji.datenight;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.billyji.datenight.activities.FoodChoiceActivity;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;

import java.util.ArrayList;
import java.util.List;

public class FoodChoiceListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final List<Business> fiveRandomBusinesses;

    public FoodChoiceListAdapter(Activity context, List<String> listSizeReference) {
        super(context, R.layout.food_list, listSizeReference);

        this.context=context;
        this.fiveRandomBusinesses = new ArrayList<>();
    }


    public void update(View view)
    {
        TextView chooseRestaurants = view.getRootView().findViewById(R.id.choose_restaurants);
        switch(FoodChoiceActivity.restaurantReference.size())
        {
            case 1:
                chooseRestaurants.setText("Good shit");
                break;
            case 3:
                chooseRestaurants.setText("Eliminate two more!");
                break;
            default:
                break;
        }
    }

    @Override
    public View getView(int position,View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.food_list, null,true);

        setText(rowView, position);
        setImages(rowView, position);

        //rowView.setMinimumHeight(height/5);
        return rowView;
    }




    private void setImages(final View rowView, final int position)
    {
        ImageView foodPicture = rowView.findViewById(R.id.picture);
        ImageButton removeOption = rowView.findViewById(R.id.remove_option);

        removeOption.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(fiveRandomBusinesses.size() == 0)
                        return;
                    fiveRandomBusinesses.remove(position);
                    FoodChoiceActivity.restaurantReference.remove(0);
                    update(view);
                    notifyDataSetChanged();
                    Log.e("hey", "hey");
                }
            }
        );


        Picasso
            .with(context)
            .load(fiveRandomBusinesses.get(position).getImageUrl())
            .fit()
            .into(foodPicture);

        Picasso
            .with(context)
            .load(R.drawable.x)
            .fit()
            .into(removeOption);
    }

    private void setText(View rowView, int position)
    {
        TextView restaurantName = rowView.findViewById(R.id.name);
        restaurantName.setText(fiveRandomBusinesses.get(position).getName());

        TextView restaurantCategories = rowView.findViewById(R.id.categories);

        StringBuilder allCategories = new StringBuilder();
        for(Category category : fiveRandomBusinesses.get(position).getCategories())
        {
            allCategories.append(category.getTitle());
            allCategories.append(", ");
        }
        allCategories.deleteCharAt(allCategories.length() - 2);

        restaurantCategories.setText(allCategories);
    }

    public void addBusiness(Business business)
    {
        fiveRandomBusinesses.add(business);
    }

    public List<Business> getFiveRandomBusinesses()
    {
        return fiveRandomBusinesses;
    }


}