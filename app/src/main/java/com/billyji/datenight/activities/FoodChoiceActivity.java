package com.billyji.datenight.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.billyji.datenight.ExpandAnimation;
import com.billyji.datenight.FinalFoodChoiceListAdapter;
import com.billyji.datenight.FoodChoiceListAdapter;
import com.billyji.datenight.FoodSelectionDetails;
import com.billyji.datenight.R;
import com.billyji.datenight.YelpRunner;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FoodChoiceActivity extends AppCompatActivity
{
    public static List<String> restaurantReference;
    private FoodChoiceListAdapter adapter;
    private FinalFoodChoiceListAdapter adapterTwo;

    private Context context;
    private PopupWindow mPopupWindow;
    @BindView(R.id.list)
    DynamicListView list;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choices);
        context = getApplicationContext();
        ButterKnife.bind(this);

        setUpListAdapter();
        setSupportActionBar(toolbar);
        Toast.makeText(context, "Swipe away two options!", Toast.LENGTH_LONG)
            .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                setUpListAdapter();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                    .show();
                break;
            default:
                break;
        }

        return true;
    }

    public void expandItem()
    {
        restaurantReference = new ArrayList<>(Arrays.asList("x", "x"));
        adapterTwo = new FinalFoodChoiceListAdapter(this, restaurantReference, adapter.getFiveRandomBusinesses());
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapterTwo);

        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);
    }

    private void setUpListAdapter()
    {
        restaurantReference = new ArrayList<>(Arrays.asList("x", "x", "x", "x", "x"));
        adapter = new FoodChoiceListAdapter(this, restaurantReference);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);

        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        list.enableSwipeToDismiss(
            new OnDismissCallback() {
                @Override
                public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                    for (int position : reverseSortedPositions) {
                        adapter.removeBusiness(position, listView);
                    }
                }
            }
        );

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                View toolbar = view.getRootView().findViewById(R.id.expanding_cell);
//
//                // Creating the expand animation for the item
//                ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);
//
//                // Start the animation on the toolbar
//                toolbar.startAnimation(expandAni);


//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//
//                View customView = inflater.inflate(R.layout.popup_dialog, null);
//                ImageView tits = customView.findViewById(R.id.tv);
//
//
////                    ImageView imageView = customView.findViewById(R.id.linear);
//
//                Picasso
//                    .with(context)
//                    .load(adapter.getFiveRandomBusinesses().get(position).getImageUrl())
//                    .fit()
//                    .into(tits);
//                /*
//                    public PopupWindow (View contentView, int width, int height)
//                        Create a new non focusable popup window which can display the contentView.
//                        The dimension of the window must be passed to this constructor.
//
//                        The popup does not provide any background. This should be handled by
//                        the content view.
//
//                    Parameters
//                        contentView : the popup's content
//                        width : the popup's width
//                        height : the popup's height
//                */
//
//                // Inflate the custom layout/view
//
//                // Initialize a new instance of popup window
//                mPopupWindow = new PopupWindow(
//                    customView,
//                    LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT
//                );
//
//                mPopupWindow.setFocusable(true);
//
//
//
//                /*
//                    public void showAtLocation (View parent, int gravity, int x, int y)
//                        Display the content view in a popup window at the specified location. If the
//                        popup window cannot fit on screen, it will be clipped.
//                        Learn WindowManager.LayoutParams for more information on how gravity and the x
//                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
//                        to specifying Gravity.LEFT | Gravity.TOP.
//
//                    Parameters
//                        parent : a parent view to get the getWindowToken() token from
//                        gravity : the gravity which controls the placement of the popup window
//                        x : the popup's x location offset
//                        y : the popup's y location offset
//                */
//                // Finally, show the popup window at the center location of root relative layout
//                mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }
}


