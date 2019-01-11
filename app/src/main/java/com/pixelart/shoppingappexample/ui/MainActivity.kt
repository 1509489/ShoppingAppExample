package com.pixelart.shoppingappexample.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.ui.drinkscreen.DrinksFragment
import com.pixelart.shoppingappexample.ui.foodscreen.FoodsFragment
import com.pixelart.shoppingappexample.ui.homescreen.HomeFragment
import com.pixelart.shoppingappexample.ui.loginscreen.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val prefsManager = PrefsManager.INSTANCE

    private lateinit var homeFragment: HomeFragment
    private lateinit var currentPage: CurrentPage
    private val foodFragment = FoodsFragment(); private val drinksFragment = DrinksFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        prefsManager.setContext(this)
        val customer = prefsManager.getCustomer()

        val headerView = nav_view.getHeaderView(0)

        headerView.tvName_NavHeader.text = "Hi, ${customer.firstname}"
        headerView.tvEmail_NavHeader.text = customer.email

        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.home_content, homeFragment, "home_fragment")
            .commit()
        currentPage = CurrentPage.Home

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        when {
            supportFragmentManager.findFragmentByTag("home_fragment") == homeFragment -> currentPage = CurrentPage.Home
            supportFragmentManager.findFragmentByTag("drink_fragment") == drinksFragment -> currentPage = CurrentPage.Drinks
            supportFragmentManager.findFragmentByTag("food_fragment") == foodFragment -> currentPage = CurrentPage.Foods
        }

        homeFragment.hideLoadingIndicator(homeFragment.getLoadingIndicator())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                homeFragment.hideLoadingIndicator(homeFragment.getLoadingIndicator())
                if (currentPage == CurrentPage.Home)
                    drawer_layout.closeDrawer(GravityCompat.START)
                else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content, homeFragment)
                        .commit()
                    currentPage = CurrentPage.Home
                }

                //if (supportFragmentManager.findFragmentByTag("home_fragment") == homeFragment)
                   // homeFragment.hideLoadingIndicator(homeFragment.getLoadingIndicator())
            }
            R.id.nav_food -> {

                if(currentPage != CurrentPage.Foods){
                    currentPage = CurrentPage.Foods
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content, foodFragment, "food_fragment")
                        .addToBackStack("food_fragment")
                        .commit()
                }
                else
                    drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_drink -> {

                if(currentPage != CurrentPage.Drinks){
                    currentPage = CurrentPage.Drinks
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content, drinksFragment, "drink_fragment")
                        .addToBackStack("drink_fragment")
                        .commit()
                }
                else
                    drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_manage -> {

            }
            R.id.nav_orders -> {

            }
            R.id.nav__accountSettings -> {

            }
            R.id.nav_logout -> {
                prefsManager.onLogout()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    enum class CurrentPage{
        Home,
        Foods,
        Drinks
    }
}
