package com.example.lamadev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.lamadev.fragments.*
import com.example.lamadev.databinding.ActivityMainBinding
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var userRepository: UserRepository
    lateinit var currentUser:User
    private lateinit var activityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        activityMainBinding.toolbarActivity.title=""

        setSupportActionBar(activityMainBinding.toolbarActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

        activityMainBinding.toolbarActivity.visibility = View.GONE

        userRepository.currentUserLiveData.observe(this) { user ->
            if (user != null && user.isNotEmpty()) {
                currentUser = user[0]
                setCurrentFragment(HomeFragment(currentUser))
            }
        }




        activityMainBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> {
                    setCurrentFragment(HomeFragment(currentUser))
                    activityMainBinding.toolbarActivity.visibility = View.GONE
                    }
                R.id.item2 -> {
                    setCurrentFragment(NotificationFragment(currentUser))
                    activityMainBinding.toolbarActivity.visibility = View.GONE
                }
                R.id.item3 -> {
                    setCurrentFragment(AddPostFragment(currentUser))
                    activityMainBinding.toolbarActivity.visibility = View.VISIBLE

                }
                R.id.item4 -> {
                    setCurrentFragment(SearchFragment(currentUser))
                    activityMainBinding.toolbarActivity.visibility = View.GONE
                }
                R.id.item5 -> {
//                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
//                    supportActionBar?.setDisplayShowHomeEnabled(false)
//                    activityMainBinding.toolbarActivity.visibility = View.VISIBLE
//                    activityMainBinding.toolbarActivity.setNavigationIcon(R.drawable.baseline_menu_24)
                    activityMainBinding.toolbarActivity.visibility = View.GONE
                    setCurrentFragment(MessageFragment(currentUser))
                }
                else->{
                    setCurrentFragment(HomeFragment(currentUser))
                    activityMainBinding.toolbarActivity.visibility = View.GONE
                }
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu,menu)
        return true

    }
    private fun setCurrentFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, fragment)
                commit()
            }

}