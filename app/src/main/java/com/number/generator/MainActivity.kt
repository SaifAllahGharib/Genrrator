package com.number.generator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.number.generator.databinding.ActivityMainBinding
import com.number.generator.home.HomeScreen

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var textInputLayoutCode: TextInputLayout
    private lateinit var btnLogin: MaterialButton
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        textInputLayoutCode = binding.txtInputCode
        btnLogin = binding.btnLoginCode
        database = Firebase.database
        ClearFocus(this, R.id.layout_code, binding.txtInputCode).clearFocus()

        val txtWatcher = LoginTextWatcher(
            textInputLayoutCode,
            null,
            null,
            btnLogin,
            binding.btnLoginCodeDisabled,
        )
        textInputLayoutCode.editText!!.addTextChangedListener(txtWatcher.loginTextWatcher)

        btnLogin.setOnClickListener {
            val txtCode = textInputLayoutCode.editText!!.text.toString()
            btnLogin.visibility = View.GONE
            binding.progBtnCode.root.visibility = View.VISIBLE
            database.reference.child("Codes").addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n", "InflateParams")
                override fun onDataChange(snapshot: DataSnapshot) {
                    btnLogin.visibility = View.VISIBLE
                    binding.progBtnCode.root.visibility = View.GONE

                    snapshot.children.forEach { vals ->
                        vals.children.forEach { ch ->
                            if (ch.key == "code") {
                                if (ch.value == txtCode) {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            HomeScreen::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    textInputLayoutCode.isErrorEnabled = true
                                    textInputLayoutCode.error = "This code is not valid"
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}