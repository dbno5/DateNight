package com.billyji.datenight.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.billyji.datenight.R;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodChoiceActivity extends AppCompatActivity
{
    @BindView(R.id.list)
    DynamicListView list;

    private List<String> restaurantReference;
    private FoodChoiceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choices);
        ButterKnife.bind(this);

        setUpListAdapter();
        setToolbarTitle("Swipe away two options");
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
            case android.R.id.home:
                finish();
            default:
                break;
        }

        return true;
    }

    public void setToolbarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    public void expandItem()
    {
        adapter.setOnlyOneBusiness(true);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);

        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);
        list.disableSwipeToDismiss();
    }

    public void update(int businessListSize)
    {
        removeReference();
        switch (businessListSize)
        {
            case 1:
                setToolbarTitle("Congratulations!");
                expandItem();
                break;
            case 3:
                setToolbarTitle("Remove two more");
                break;
            default:
                break;
        }
    }

    private void setUpListAdapter()
    {
        restaurantReference = new ArrayList<>(Arrays.asList("x", "x", "x", "x", "x"));
        adapter = new FoodChoiceListAdapter(this, restaurantReference);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);

        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);
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
    }

    private void removeReference()
    {
        restaurantReference.remove(0);
    }
}


