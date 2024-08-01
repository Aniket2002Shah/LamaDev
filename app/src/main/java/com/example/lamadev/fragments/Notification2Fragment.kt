package com.example.lamadev.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lamadev.R
import com.example.lamadev.adapter.NotificationRecyclerAdapter
import com.example.lamadev.databinding.FragmentNotification2Binding
import com.example.lamadev.pojo.*
import com.example.lamadev.repository.NotificationRepository
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.NotificationViewModel
import com.example.lamadev.view_model_factory.NotificationViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class Notification2Fragment(private val currentUser: User) : Fragment() {

    @Inject
    lateinit var repository: NotificationRepository

    private lateinit var notification2Binding: FragmentNotification2Binding
    private lateinit var arrayList: ArrayList<Notification>

    private lateinit var notifyViewModel: NotificationViewModel
    private lateinit var notifyFactory: NotificationViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notification2Binding = DataBindingUtil.inflate<FragmentNotification2Binding>(
            inflater,
            R.layout.fragment_notification2, container, false
        )

        notifyFactory = NotificationViewModelFactory(currentUser.id, repository)
        notifyViewModel = ViewModelProvider(this, notifyFactory)[NotificationViewModel::class.java]

        notifyViewModel.userLivedata.observe(viewLifecycleOwner){
            when (it) {
                is Response.Processing -> {}
                is Response.Success -> { it?.let {} }
                is Response.Error->{ it?.let {} } } }

        notifyViewModel.updateNotifyAt(currentUser.id)

        notification2Binding.notificationTabRecyclerView.layoutManager =
            LinearLayoutManager(context)
        val adapter = NotificationRecyclerAdapter()
        notification2Binding.notificationTabRecyclerView.setHasFixedSize(true)
        notification2Binding.notificationTabRecyclerView.adapter = adapter
        notification2Binding.notificationTabRecyclerView.isNestedScrollingEnabled = false

        notifyViewModel.notifyLiveData.observe(viewLifecycleOwner) {
            when (it) {

                is Response.Processing -> {
                    notification2Binding.progressBar.visibility = View.VISIBLE

                }
                is Response.Success -> {
                    it?.let {
                        notification2Binding.progressBar.visibility = View.GONE

                        val data = it.r_data!!

                        if (data.isNotEmpty()) {
                            adapter.onChange(data)
                        } else {
                            notification2Binding.errorPage.errorLayout.visibility = View.VISIBLE
                            Toast.makeText(
                                activity,
                                "You have no notification !!",
                                Toast.LENGTH_LONG
                            ).show()
                            notification2Binding.errorPage.refreshBtn.setOnClickListener {
                                notifyViewModel.loadNotification()
                            }
                        }
                    }
                }
                    is Response.Error->{
                        it?.let {
                            notification2Binding.progressBar.visibility = View.GONE
                            notification2Binding.errorPage.errorLayout.visibility = View.VISIBLE
                            Toast.makeText(activity, it.r_message, Toast.LENGTH_LONG).show()
                            notification2Binding.errorPage.refreshBtn.setOnClickListener {
                                notifyViewModel.loadNotification()
                            }
                        }
                    }
                }
            }
        return notification2Binding.root
    }

}
