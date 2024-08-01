package com.example.lamadev

import android.content.Intent
import android.content.res.Resources
import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lamadev.databinding.ActivityLoginBinding
import com.example.lamadev.databinding.ActivityRegisterBinding
import com.example.lamadev.pojo.RegisterRequest
import com.example.lamadev.pojo.User
import com.example.lamadev.response_handler.Response
import com.example.lamadev.view_model.HomeViewModel
import com.example.lamadev.view_model.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)


        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]


       registerViewModel.registerUserLiveData.observe(this) {
            when(it) {

                is Response.Processing->{
                   binding.progressBar.visibility = View.VISIBLE
                    binding.registerLayout.isEnabled = false

                }
                is Response.Success-> {
                    it?.let {
                        binding.progressBar.visibility = View.GONE
                        binding.email.isEnabled= false
                        binding.password.isEnabled= false
                        binding.passwordAgain.isEnabled = false
                        binding.registerButton.isEnabled = false
                         Snackbar.make(binding.registerLayout, it.r_data.toString(), Snackbar.LENGTH_SHORT).show()

                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    }
                }
                is Response.Error->{
                    it?.let {
                        binding.progressBar.visibility = View.GONE

                        val snackBar= it.r_message?.let { message ->
                            Snackbar.make(
                                binding.registerLayout, "Error \n$message", Snackbar.LENGTH_SHORT)
                        }
                        snackBar?.show()
                    }
                }
            }
        }


        binding.registerButton.setOnClickListener {
            val email  =binding.email.text.toString()
            if(email.isBlank() || !email.contains("@",false) || !email.contains(".",false)){
                Snackbar.make(
                    binding.registerLayout,
                    "Email is not valid",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            val password = binding.password.text.toString()
            val passwordAgain = binding.passwordAgain.text.toString()
            if(password == passwordAgain && passwordAgain.isNotBlank() && password.isNotBlank() && email.isNotBlank() && email.contains("@",false) && email.contains(".",false)){
               // binding.registerButton.setBackgroundColor(resources.getColor(R.color.gray_border,theme)
                registerViewModel.registerUser(RegisterRequest(email, password))

            }else{
                if(passwordAgain.isBlank() || password.isBlank() || email.isBlank() || password =="" || passwordAgain=="" || email=="") {
                    Snackbar.make(
                        binding.registerLayout,
                        "Email or Password is not valid",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                if(password != passwordAgain && passwordAgain.isNotBlank() && password.isNotBlank()){
                    Snackbar.make(
                        binding.registerLayout,
                        "Password is not matching !!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.loginText.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

}