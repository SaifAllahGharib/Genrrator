package com.number.generator.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.number.generator.databinding.ActivityHomeScreenBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

class HomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnGenerate.setOnClickListener {
            binding.textNum.text = generateRandomDecimal().toString()
        }
    }

    private fun generateRandomDecimal(): Double {
        val randomValue = Random.nextDouble(1.0, 23.0)
        val decimalFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale("ar")))
        val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale("ar"))
        return numberFormat.parse(decimalFormat.format(randomValue))!!.toDouble()
    }
}