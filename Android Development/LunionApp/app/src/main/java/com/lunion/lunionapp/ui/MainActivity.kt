package com.lunion.lunionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lunion.lunionapp.R
import com.lunion.lunionapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //type user
        val typeUser = intent.getStringExtra("DATA")
        Log.d("dataku", "Data: $typeUser")

        //default select fragment
        if (typeUser.equals("patient")){
            viewBinding.bottomNavigationView.menu.removeItem(R.id.nav_detection)
            val fragment = HistoryTreatmentFragment.newInstance()
            addFragment(fragment)
        } else {
            val fragment = DetectionFragment.newInstance()
            addFragment(fragment)
        }

        viewBinding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_detection -> {
                    val fragment = DetectionFragment.newInstance()
                    addFragment(fragment)
                }
                R.id.nav_history -> {
                    val bundle = Bundle()
                    bundle.putString("DATA", typeUser.toString())
                    val fragment = HistoryTreatmentFragment()
                    fragment.arguments = bundle
                    addFragment(fragment)
                }
                R.id.nav_news -> {
                    val fragment = NewsFragment()
                    addFragment(fragment)
                }
                R.id.nav_air_quality -> {
                    val fragment = AirQualityFragment()
                    addFragment(fragment)
                }
            }
            false
        }

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.layout_container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

}