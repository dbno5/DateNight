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
import android.widget.Toast;

import com.billyji.datenight.activities.FoodChoiceActivity;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodChoiceListAdapter extends ArrayAdapter<String>
{

    private final Activity context;

    private final List<Business> fiveRandomBusinesses;

    public FoodChoiceListAdapter(Activity context, List<String> listSizeReference)
    {
        super(context, R.layout.food_list, listSizeReference);

        this.context = context;
        this.fiveRandomBusinesses = new ArrayList<>();
        getFiveBusinesses();

    }

    private void getFiveBusinesses()
    {
        Random r = new Random();

        while (fiveRandomBusinesses.size() < 5 && YelpRunner.listBusinesses.size() > 0)
        {
            int randomBusiness = r.nextInt(YelpRunner.listBusinesses.size());
            Business curBusiness = YelpRunner.listBusinesses.get(randomBusiness);

            if (withinParameters(curBusiness) && !fiveRandomBusinesses.contains(curBusiness))
            {
                addBusiness(curBusiness);
            }
        }
    }

    private boolean withinParameters(Business business)
    {
        return business.getRating() > FoodSelectionDetails.getMinRating()
            && business.getPrice().length() < Integer.parseInt(FoodSelectionDetails.getMaxPrice());

    }

    public void update()
    {
        switch (FoodChoiceActivity.restaurantReference.size())
        {
            case 1:
                Toast.makeText(context, "Congratulations!", Toast.LENGTH_SHORT)
                    .show();
                ((FoodChoiceActivity) context).expandItem();
                break;
            case 3:
                Toast.makeText(context, "Remove two more!", Toast.LENGTH_SHORT)
                    .show();
                break;
            default:
                break;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.food_list, null, true);

        setText(rowView, position);
        setImages(rowView, position);

        //rowView.setMinimumHeight(height/5);
        return rowView;
    }


    private void setImages(final View rowView, final int position)
    {
        ImageView foodPicture = rowView.findViewById(R.id.picture);
//        ImageButton removeOption = rowView.findViewById(R.id.remove_option);


        Picasso
            .with(context)
            .load(fiveRandomBusinesses.get(position).getImageUrl())
            .fit()
            .into(foodPicture);
//
//        Picasso
//            .with(context)
//            .load(R.drawable.x)
//            .fit()
//            .into(removeOption);
    }

    private void setText(View rowView, int position)
    {
        TextView restaurantName = rowView.findViewById(R.id.name);
        restaurantName.setText(fiveRandomBusinesses.get(position).getName());

        TextView restaurantCategories = rowView.findViewById(R.id.categories);

        StringBuilder allCategories = new StringBuilder();
        for (Category category : fiveRandomBusinesses.get(position).getCategories())
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

    public void removeBusiness(int position, ViewGroup view)
    {
        if (fiveRandomBusinesses.size() == 1)
        { return; }

        fiveRandomBusinesses.remove(position);
        FoodChoiceActivity.restaurantReference.remove(0);
        update();
        notifyDataSetChanged();
    }

    public List<Business> getFiveRandomBusinesses()
    {
        return fiveRandomBusinesses;
    }


}