package com.example.lamadev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.databinding.ActivityLoginBinding
import com.example.lamadev.pojo.LoginRequest
import com.example.lamadev.pojo.RegisterRequest
import com.example.lamadev.pojo.User
import com.example.lamadev.repository.UserRepository
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.LoginViewModel
import com.example.lamadev.view_model.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userRepository.currentUserLiveData.observe(this){user->
            if(user.isNotEmpty() && user[0].email.isNotBlank() && user[0].password.isNotBlank() && !user[0].username.isNullOrBlank()){

                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }



        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.loginUserLiveData.observe(this) {
            when (it) {

                is Response.Processing -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginLayout.isEnabled = false

                }
                is Response.Success -> {
                    it?.let {
                        binding.progressBar.visibility = View.GONE
                        binding.email.isEnabled = false
                        binding.password.isEnabled = false
                        binding.loginButton.isEnabled = false
                        Snackbar.make(
                            binding.loginLayout,
                            it.r_data.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this, ProfileEditActivity::class.java))
                        finish()

                    }
                }
                is Response.Error -> {
                    it?.let {
                        binding.progressBar.visibility = View.GONE

                        val snackBar = it.r_message?.let { message ->
                            Snackbar.make(
                                binding.loginLayout, message, Snackbar.LENGTH_SHORT
                            )
                        }
                        snackBar?.show()
                    }
                }
            }
        }

        binding.loginButton.setOnClickListener {
            //DB / server
            //loader

            val email = binding.email.text.toString()
            if (email.isBlank() || !email.contains("@", false) || !email.contains(".", false)) {
                Snackbar.make(
                    binding.loginLayout,
                    "Email is not valid",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            val password = binding.password.text.toString()
            if (password.isNotBlank() && password.isNotBlank() && email.isNotBlank() && email.contains(
                    "@",
                    false
                ) && email.contains(".", false)
            ) {
                // binding.registerButton.setBackgroundColor(resources.getColor(R.color.gray_border,theme)
                loginViewModel.loginUser(LoginRequest(email, password))
            } else {
                if (password.isBlank() || email.isBlank() || password == "" || email == "") {
                    Snackbar.make(
                        binding.loginLayout,
                        "Email or Password is not valid",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }


        binding.registerText.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

    }
}