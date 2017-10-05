package com.billyji.datenight.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.billyji.datenight.FinalFoodChoiceListAdapter;
import com.billyji.datenight.FoodChoiceListAdapter;
import com.billyji.datenight.R;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodChoiceActivity extends AppCompatActivity
{
    public static List<String> restaurantReference;
    private FoodChoiceListAdapter adapter;
    private FinalFoodChoiceListAdapter adapterTwo;

    @BindView(R.id.list)
    DynamicListView list;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choices);
        ButterKnife.bind(this);

        setUpListAdapter();
        setSupportActionBar(toolbar);
        Toast.makeText(this, "Swipe away two options!", Toast.LENGTH_SHORT)
            .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                setUpListAdapter();
                break;
            default:
                break;
        }

        return true;
    }

    public void expandItem()
    {
        restaurantReference = new ArrayList<>(Arrays.asList("x", "x"));
        adapterTwo = new FinalFoodChoiceListAdapter(this, restaurantReference, adapter.getFiveRandomBusinesses().get(0));
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
            new OnDismissCallback()
            {
                @Override
                public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions)
                {
                    for (int position : reverseSortedPositions)
                    {
                        adapter.removeBusiness(position);
                    }
                }
            }
        );

        list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(
                AdapterView<?> parent, View view,
                int position, long id)
            {


            }
        });
    }
}


