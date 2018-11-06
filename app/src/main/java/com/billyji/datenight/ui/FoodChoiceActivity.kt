package com.billyji.datenight.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.billyji.datenight.R
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter
import kotlinx.android.synthetic.main.activity_food_choices.*

import java.util.ArrayList
import java.util.Arrays

class FoodChoiceActivity : AppCompatActivity() {
    private var restaurantReference: MutableList<String>? = null
    private var foodListAdapter: FoodChoiceListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_choices)
        setUpListAdapter()

        val fab: View = findViewById(R.id.action_refresh)
        fab.setOnClickListener { _ ->
            setUpListAdapter()
        }
    }

    private fun setUpListAdapter() {
        restaurantReference = ArrayList(Arrays.asList("x", "x", "x", "x", "x"))
        foodListAdapter = FoodChoiceListAdapter(this, restaurantReference!!)
        val animationAdapter = SwingLeftInAnimationAdapter(foodListAdapter!!)

        animationAdapter.setAbsListView(all_food_list!!)
        all_food_list!!.adapter = animationAdapter
        all_food_list!!.enableSwipeToDismiss { _, reverseSortedPositions ->
            for (position in reverseSortedPositions) {
                foodListAdapter!!.removeBusiness(position)
            }
        }
    }

    fun update(businessListSize: Int) {
        removeReference()
        if (businessListSize == 1) {
            expandItem()
        }
    }

    private fun removeReference() {
        restaurantReference!!.removeAt(0)
    }

    private fun expandItem() {
        foodListAdapter!!.setOnlyOneBusiness()
        all_food_list!!.disableSwipeToDismiss()
    }
}


