package com.number.generator.signIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.number.generator.ClearFocus
import com.number.generator.LoginTextWatcher
import com.number.generator.MainActivity
import com.number.generator.R
import com.number.generator.Validation
import com.number.generator.databinding.ActivitySignInBinding
import com.number.generator.signUp.SignUp

class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var btnSignIn: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        textInputLayoutEmail = binding.txtInputEmail
        textInputLayoutPassword = binding.txtInputPassword
        btnSignIn = binding.btnLogin
        auth = FirebaseAuth.getInstance()
        database = Firebase.database

        ClearFocus(
            this,
            R.id.layout_signIn,
            textInputLayoutEmail,
            textInputLayoutPassword
        ).clearFocus()

        val textWatcher =
            LoginTextWatcher(
                textInputLayoutEmail,
                textInputLayoutPassword,
                null,
                btnSignIn,
                binding.btnLoginDisabled
            )
        textInputLayoutEmail.editText!!.addTextChangedListener(textWatcher.loginTextWatcher)
        textInputLayoutPassword.editText!!.addTextChangedListener(textWatcher.loginTextWatcher)

        binding.btnGoToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        btnSignIn.setOnClickListener {
            binding.btnLogin.visibility = View.GONE
            findViewById<View>(R.id.prog_btn).visibility = View.VISIBLE
            val txtEmail = textInputLayoutEmail.editText!!.text.toString()
            val txtPassword = textInputLayoutPassword.editText!!.text.toString()
            if (txtPassword.isEmpty() || textInputLayoutEmail.isEmpty()) {
                if (txtPassword.isEmpty()) {
                    textInputLayoutPassword.error = "The field cannot be empty."
                    binding.btnLogin.visibility = View.VISIBLE
                    findViewById<View>(R.id.prog_btn_signUp).visibility = View.GONE
                }
                if (textInputLayoutEmail.isEmpty()) {
                    textInputLayoutEmail.error = "The field cannot be empty."
                    binding.btnLogin.visibility = View.VISIBLE
                    findViewById<View>(R.id.prog_btn_signUp).visibility = View.GONE
                }
            } else if (txtPassword.isNotEmpty() && textInputLayoutEmail.isNotEmpty()) {
                auth.signInWithEmailAndPassword(txtEmail, txtPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            binding.btnLogin.visibility = View.VISIBLE
                            findViewById<View>(R.id.prog_btn).visibility = View.GONE
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            binding.btnLogin.visibility = View.VISIBLE
                            findViewById<View>(R.id.prog_btn).visibility = View.GONE
                            if (task.exception!!.message == "There is no user record corresponding to this identifier. The user may have been deleted.") {
                                Validation().validateEmail(
                                    textInputLayoutEmail,
                                    "Invalid input entered.",
                                    "This user does not exist."
                                )
                            }
                            if (task.exception!!.message == "The password is invalid or the user does not have a password.") {
                                textInputLayoutPassword.error = task.exception!!.message
                            }
                            println(task.exception!!.message)
                        }
                    }
            }
        }
    }
}