package com.number.generator

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class ClearFocus(
    private val activity: Activity,
    private val view: Int,
    private var inputOne: TextInputLayout? = null,
    private var inputTwo: TextInputLayout? = null,
    private var inputThree: TextInputLayout? = null,
    private var inputFour: TextInputLayout? = null,
    private var inputFive: TextInputLayout? = null,
) {
    fun clearFocus() {
        activity.findViewById<View>(view).setOnClickListener { v ->
            val imm: InputMethodManager =
                v.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            inputOne?.clearFocus()
            inputTwo?.clearFocus()
            inputThree?.clearFocus()
            inputFour?.clearFocus()
            inputFive?.clearFocus()
        }
    }
}