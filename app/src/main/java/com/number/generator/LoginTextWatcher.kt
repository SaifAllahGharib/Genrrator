package com.number.generator

import android.annotation.SuppressLint
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class LoginTextWatcher(
    editTextOne: TextInputLayout,
    editTextTwo: TextInputLayout?,
    editTextThree: TextInputLayout?,
    btnLoginShow: MaterialButton,
    btnLoginNotShow: MaterialButton,
) {
    val loginTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        @SuppressLint("ResourceAsColor")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val textEditTextOne = editTextOne.editText!!.text.toString()
            if (editTextTwo == null && editTextThree == null) {
                if (textEditTextOne.isNotEmpty()) {
                    btnLoginShow.visibility = View.VISIBLE
                    btnLoginNotShow.visibility = View.GONE
                    editTextOne.error = null
                    editTextOne.isErrorEnabled = false
                } else {
                    btnLoginShow.visibility = View.GONE
                    btnLoginNotShow.visibility = View.VISIBLE
                    editTextOne.error = null
                    editTextOne.isErrorEnabled = false
                }
            } else if (editTextThree == null && editTextTwo != null) {
                val textEditTextTwo = editTextTwo.editText!!.text.toString()
                if (textEditTextOne.isNotEmpty() && textEditTextTwo.isNotEmpty()) {
                    btnLoginShow.visibility = View.VISIBLE
                    btnLoginNotShow.visibility = View.GONE
                    editTextOne.error = null
                    editTextOne.isErrorEnabled = false
                    editTextTwo.error = null
                    editTextTwo.isErrorEnabled = false
                } else {
                    btnLoginShow.visibility = View.GONE
                    btnLoginNotShow.visibility = View.VISIBLE
                    editTextOne.error = null
                    editTextOne.isErrorEnabled = false
                }
            } else if (editTextThree != null && editTextTwo != null) {
                val textEditTextThree = editTextThree.editText!!.text.toString()
                val textEditTextTwo = editTextTwo.editText!!.text.toString()
                if (textEditTextOne.isNotEmpty() && textEditTextTwo.isNotEmpty() && textEditTextThree.isNotEmpty()) {
                    btnLoginShow.visibility = View.VISIBLE
                    btnLoginNotShow.visibility = View.GONE
                    editTextOne.error = null
                    editTextOne.isErrorEnabled = false
                    editTextTwo.error = null
                    editTextTwo.isErrorEnabled = false
                    editTextThree.error = null
                    editTextThree.isErrorEnabled = false
                } else {
                    btnLoginShow.visibility = View.GONE
                    btnLoginNotShow.visibility = View.VISIBLE
                    editTextOne.error = null
                    editTextOne.isErrorEnabled = false
                }
            }
            if (editTextOne.editText!!.inputType == InputType.TYPE_CLASS_TEXT) {
                Validation().validateEmail(editTextOne)
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}