package com.billyji.datenight;

import android.app.Activity;
import android.support.annotation.NonNull;
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

    private void update()
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
    public @NonNull
    View getView(int position, View view, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        //No need for ViewHolder pattern here as scrolling never occurs
        View rowView = inflater.inflate(R.layout.food_list, parent, false);

        setText(rowView, position);
        setImages(rowView, position);

        return rowView;
    }


    private void setImages(final View rowView, final int position)
    {
        ImageView foodPicture = rowView.findViewById(R.id.picture);

        Picasso
            .with(context)
            .load(fiveRandomBusinesses.get(position).getImageUrl())
            .fit()
            .into(foodPicture);
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

    private void addBusiness(Business business)
    {
        fiveRandomBusinesses.add(business);
    }

    public void removeBusiness(int position)
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