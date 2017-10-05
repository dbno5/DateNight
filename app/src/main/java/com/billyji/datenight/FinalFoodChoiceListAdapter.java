package com.billyji.datenight;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class FinalFoodChoiceListAdapter extends ArrayAdapter
{
    private final Activity context;

    private final Business business;
    private boolean restaurant = true;

    public FinalFoodChoiceListAdapter(Activity context, List<String> listSizeReference, Business lastRestaurant)
    {
        super(context, R.layout.selected_food_detail, listSizeReference);

        this.context = context;
        this.business = lastRestaurant;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView;
        if (restaurant)
        {
            rowView = inflater.inflate(R.layout.food_list, null, true);
            setText(rowView, business);
            setImages(rowView, business);
        }
        else
        {
            rowView = inflater.inflate(R.layout.selected_food_detail, null, true);
            setBlah(rowView, business);
        }
        restaurant = false;
        //rowView.setMinimumHeight(height/5);
        return rowView;
    }

    private void setBlah(View view, final Business business)
    {
        TextView number = view.findViewById(R.id.number);
        number.setText(business.getDisplayPhone());
        number.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + business.getDisplayPhone()));
                context.startActivity(intent);
            }
        });

        TextView address = view.findViewById(R.id.address);
        address.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + business.getName() );

                //String uri = "http://maps.google.com/maps?daddr=" + business.getCoordinates().getLatitude() + "," + business.getCoordinates().getLongitude() + " (" + "Where the food is at" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });

        TextView website = view.findViewById(R.id.website_link);
        website.setClickable(true);
        website.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='" + business.getUrl() + "'>" + "Check out " + business.getName() + "'s Website" + "</a>";
        website.setText(Html.fromHtml(text));
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
