package com.billyji.datenight.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.billyji.datenight.FoodChoiceListAdapter;
import com.billyji.datenight.FoodSelectionDetails;
import com.billyji.datenight.R;
import com.billyji.datenight.YelpRunner;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class FoodChoiceActivity extends AppCompatActivity
{
    public static final List<String> restaurantReference = new ArrayList<>(Arrays.asList("x", "x", "x", "x", "x"));
    private FoodChoiceListAdapter adapter;

    private Context context;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choices);
        context = getApplicationContext();

        adapter = new FoodChoiceListAdapter(this, restaurantReference);
        getFiveBusinesses();
        final ListView list= findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);


                    View customView = inflater.inflate(R.layout.popup_dialog, null);
                    ImageView tits = customView.findViewById(R.id.tv);


//                    ImageView imageView = customView.findViewById(R.id.linear);

                    Picasso
                        .with(context)
                        .load(adapter.getFiveRandomBusinesses().get(position).getImageUrl())
                        .fit()
                        .into(tits);
                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */

                    // Inflate the custom layout/view

                    // Initialize a new instance of popup window
                    mPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                    );

                    mPopupWindow.setFocusable(true);



                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                    // Finally, show the popup window at the center location of root relative layout
                    mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
        });
    }

    private void getFiveBusinesses()
    {
        Random r = new Random();

        while(adapter.getFiveRandomBusinesses().size() < 5 && YelpRunner.listBusinesses.size() > 0)
        {
            int randomBusiness = r.nextInt(YelpRunner.listBusinesses.size());
            Business curBusiness = YelpRunner.listBusinesses.get(randomBusiness);

            if(withinParameters(curBusiness))
            {
                adapter.addBusiness(curBusiness);
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


