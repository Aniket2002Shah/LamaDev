package com.example.lamadev.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.lamadev.R
import com.example.lamadev.adapter.ViewPagerAdapter
import com.example.lamadev.databinding.FragmentNotificationBinding
import com.example.lamadev.pojo.User
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment(private val currentUser: User) : Fragment() {

    private  lateinit var notificationBinding: FragmentNotificationBinding
    private lateinit var adapter: ViewPagerAdapter
    private  val tabllayoutList = arrayOf("Notification","Request")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationBinding=  DataBindingUtil.inflate<FragmentNotificationBinding>(inflater,
            R.layout.fragment_notification,container,false)
        val view = notificationBinding.root

        adapter = ViewPagerAdapter(this)
        adapter.addFragment(Notification2Fragment(currentUser))
        adapter.addFragment(RequestFragment())
        notificationBinding.viewPage.let {
            it.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            it.adapter = adapter
        }

        TabLayoutMediator(notificationBinding.tablayout,notificationBinding.viewPage){tab,position->
            tab.text = tabllayoutList[position]
        }.attach()

        return view
    }
}