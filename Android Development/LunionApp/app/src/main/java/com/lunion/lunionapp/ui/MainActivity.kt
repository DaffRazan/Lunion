package com.lunion.lunionapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
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
        Log.d("dataku", "typeUser: $typeUser")

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Toast.makeText(this, "LogOut...", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}