package com.billyji.datenight;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.billyji.datenight.activities.FoodChoiceActivity;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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

    private void setStars(View rowView, int position)
    {
        final ImageView starsPicture = rowView.findViewById(R.id.stars);
        String numStars = Double.toString(fiveRandomBusinesses.get(position).getRating());
        String processedNumStars = numStars.replace('.', '_');

        int resourceID = context.getResources().getIdentifier("stars_" + processedNumStars, "drawable", context.getPackageName());

        starsPicture.setImageResource(resourceID);
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
        setReviews(rowView, position);

        return rowView;
    }


    private void setImages(final View rowView, final int position)
    {
        final ImageView foodPicture = rowView.findViewById(R.id.picture);

        setStars(rowView, position);
        Picasso
            .with(context)
            .load(fiveRandomBusinesses.get(position).getImageUrl())
            .fit()
            .into(foodPicture);

        final LayoutInflater inflater = context.getLayoutInflater();
        final View customView = inflater.inflate(R.layout.popup_dialog, null);
        final ImageView tits = customView.findViewById(R.id.tv);

        foodPicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Picasso
                    .with(context)
                    .load(fiveRandomBusinesses.get(position).getImageUrl())
                    .fit()
                    .into(tits);

                // Initialize a new instance of popup window
                PopupWindow mPopupWindow = new PopupWindow(
                    customView,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                );

                mPopupWindow.setFocusable(true);

                // Finally, show the popup window at the center location of root relative layout
                mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void setReviews(View rowView, int position)
    {
        TextView numReviews = rowView.findViewById(R.id.reviews);
        numReviews.setText(fiveRandomBusinesses.get(position).getReviewCount() + " Reviews");
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