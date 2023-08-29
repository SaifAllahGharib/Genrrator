package com.number.generator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.number.generator.signIn.SignIn


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val time: Long = 2000
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
                finish()
            }, time)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(
                    Intent(
                        this,
                        SignIn::class.java
                    )
                )
                finish()
            }, time)
        }
    }
}