package com.billyji.datenight.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.billyji.datenight.R
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter
import kotlinx.android.synthetic.main.activity_food_choices.*
import kotlinx.android.synthetic.main.selected_food_detail.*

import java.util.ArrayList
import java.util.Arrays


class FoodChoiceActivity : AppCompatActivity() {

    private var m_restaurantReference: MutableList<String>? = null
    private var m_foodListAdapter: FoodChoiceListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_choices)

        setUpListAdapter()
        setToolbarTitle(getString(R.string.swipe_two))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> setUpListAdapter()
            android.R.id.home -> finish()
            else -> {
            }
        }

        return true
    }

    private fun setToolbarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }

    private fun expandItem() {
        m_foodListAdapter!!.setOnlyOneBusiness()
        val animationAdapter = SwingLeftInAnimationAdapter(m_foodListAdapter!!)

        animationAdapter.setAbsListView(all_food_list!!)
        all_food_list!!.adapter = animationAdapter
        all_food_list!!.disableSwipeToDismiss()
    }

    fun update(businessListSize: Int) {
        removeReference()
        when (businessListSize) {
            1 -> {
                setToolbarTitle(getString(R.string.finished_picking))
                expandItem()
            }
            3 -> setToolbarTitle(getString(R.string.remove_two_more))
            else -> {
            }
        }
    }

    private fun setUpListAdapter() {
        m_restaurantReference = ArrayList(Arrays.asList("x", "x", "x", "x", "x"))
        m_foodListAdapter = FoodChoiceListAdapter(this, m_restaurantReference!!)
        val animationAdapter = SwingLeftInAnimationAdapter(m_foodListAdapter!!)

        animationAdapter.setAbsListView(all_food_list!!)
        all_food_list!!.adapter = animationAdapter
        all_food_list!!.enableSwipeToDismiss { listView, reverseSortedPositions ->
            for (position in reverseSortedPositions) {
                m_foodListAdapter!!.removeBusiness(position)
            }
        }
    }

    private fun removeReference() {
        m_restaurantReference!!.removeAt(0)
    }
}


