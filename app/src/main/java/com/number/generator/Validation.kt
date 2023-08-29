package com.number.generator

import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

class Validation {
    fun validateEmail(
        inputLayout: TextInputLayout,
        msgError: String = "Invalid input entered.",
        msgSecError: String? = null,
    ): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        if (!pattern.matcher(inputLayout.editText!!.text.toString()).matches()) {
            inputLayout.error = msgError
            if (inputLayout.editText!!.text.toString().isEmpty()) {
                inputLayout.error = null
                inputLayout.isErrorEnabled = false
            }
        } else if (pattern.matcher(inputLayout.editText!!.text.toString()).matches()) {
            inputLayout.error = msgSecError
        } else {
            inputLayout.error = null
        }
        return true
    }
}