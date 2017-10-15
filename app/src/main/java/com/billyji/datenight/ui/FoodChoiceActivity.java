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
    @BindView(R.id.all_food_list)
    DynamicListView m_foodList;

    private List<String> m_restaurantReference;
    private FoodChoiceListAdapter m_foodListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_choices);
        ButterKnife.bind(this);

        setUpListAdapter();
        setToolbarTitle(getString(R.string.swipe_two));
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

    private void setToolbarTitle(String title)
    {
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(title);
        }
    }

    private void expandItem()
    {
        m_foodListAdapter.setOnlyOneBusiness();
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(m_foodListAdapter);

        animationAdapter.setAbsListView(m_foodList);
        m_foodList.setAdapter(animationAdapter);
        m_foodList.disableSwipeToDismiss();
    }

    public void update(int businessListSize)
    {
        removeReference();
        switch (businessListSize)
        {
            case 1:
                setToolbarTitle(getString(R.string.finished_picking));
                expandItem();
                break;
            case 3:
                setToolbarTitle(getString(R.string.remove_two_more));
                break;
            default:
                break;
        }
    }

    private void setUpListAdapter()
    {
        m_restaurantReference = new ArrayList<>(Arrays.asList("x", "x", "x", "x", "x"));
        m_foodListAdapter = new FoodChoiceListAdapter(this, m_restaurantReference);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(m_foodListAdapter);

        animationAdapter.setAbsListView(m_foodList);
        m_foodList.setAdapter(animationAdapter);
        m_foodList.enableSwipeToDismiss(
            new OnDismissCallback()
            {
                @Override
                public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions)
                {
                    for (int position : reverseSortedPositions)
                    {
                        m_foodListAdapter.removeBusiness(position);
                    }
                }
            }
        );
    }

    private void removeReference()
    {
        m_restaurantReference.remove(0);
    }
}


