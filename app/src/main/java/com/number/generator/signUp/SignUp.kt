package com.number.generator.signUp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.number.generator.ClearFocus
import com.number.generator.CreateAlertDialog
import com.number.generator.LoginTextWatcher
import com.number.generator.MainActivity
import com.number.generator.R
import com.number.generator.Validation
import com.number.generator.databinding.ActivitySignUpBinding
import com.number.generator.model.User
import com.number.generator.signIn.SignIn

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var btnSignIn: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        textInputLayoutName = binding.txtInputName
        textInputLayoutEmail = binding.txtInputEmail
        textInputLayoutPassword = binding.txtInputPassword
        btnSignIn = binding.btnSignUp
        auth = FirebaseAuth.getInstance()
        database = Firebase.database

        ClearFocus(
            this,
            R.id.layout_signUp,
            textInputLayoutEmail,
            textInputLayoutPassword,
            textInputLayoutName
        ).clearFocus()

        val textWatcher = LoginTextWatcher(
            textInputLayoutEmail,
            textInputLayoutPassword,
            textInputLayoutName,
            btnSignIn,
            binding.btnSignUpDisabled
        )
        textInputLayoutName.editText!!.addTextChangedListener(textWatcher.loginTextWatcher)
        textInputLayoutEmail.editText!!.addTextChangedListener(textWatcher.loginTextWatcher)
        textInputLayoutPassword.editText!!.addTextChangedListener(textWatcher.loginTextWatcher)

        binding.btnGoToSignIn.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

        btnSignIn.setOnClickListener {
            binding.btnSignUp.visibility = View.GONE
            findViewById<View>(R.id.prog_btn_signUp).visibility = View.VISIBLE
            val txtName = textInputLayoutName.editText!!.text.toString()
            val txtEmail = textInputLayoutEmail.editText!!.text.toString()
            val txtPassword = textInputLayoutPassword.editText!!.text.toString()
            if (txtName.isEmpty() || txtPassword.isEmpty() || textInputLayoutEmail.isEmpty()) {
                if (txtName.isEmpty()) {
                    textInputLayoutName.error = "The field cannot be empty."
                    binding.btnSignUp.visibility = View.VISIBLE
                    findViewById<View>(R.id.prog_btn_signUp).visibility = View.GONE
                }
                if (txtPassword.isEmpty()) {
                    textInputLayoutPassword.error = "The field cannot be empty."
                    binding.btnSignUp.visibility = View.VISIBLE
                    findViewById<View>(R.id.prog_btn_signUp).visibility = View.GONE
                }
                if (textInputLayoutEmail.isEmpty()) {
                    textInputLayoutEmail.error = "The field cannot be empty."
                    binding.btnSignUp.visibility = View.VISIBLE
                    findViewById<View>(R.id.prog_btn_signUp).visibility = View.GONE
                }
            } else if (txtName.isNotEmpty() && txtPassword.isNotEmpty() && textInputLayoutEmail.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            binding.btnSignUp.visibility = View.VISIBLE
                            findViewById<View>(R.id.prog_btn_signUp).visibility = View.GONE
                            try {
                                database.reference.child("Users").child(task.result.user!!.uid)
                                    .setValue(User(txtName, txtEmail, txtPassword))
                            } catch (e: Exception) {
                                println(e)
                            }
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            binding.btnSignUp.visibility = View.VISIBLE
                            findViewById<View>(R.id.prog_btn_signUp).visibility = View.GONE
                            database.reference.child("Users")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (ds in snapshot.children) {
                                            ds.children.forEach { vals ->
                                                if (vals.key == "email") {
                                                    if (txtEmail == vals.value) {
                                                        Validation().validateEmail(
                                                            textInputLayoutEmail,
                                                            "Invalid input entered",
                                                            "This email is used by another account"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                            val alertDialog = CreateAlertDialog(
                                this,
                                layoutInflater.inflate(R.layout.alert_dialog, null),
                                R.style.CustomAlert
                            )
                            alertDialog.view.findViewById<TextView>(R.id.tvError).text =
                                task.exception!!.message.toString()
                            alertDialog.showAlertDialog()
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            println(task.exception!!.message.toString())
                        }
                    }
            }
        }
    }
}