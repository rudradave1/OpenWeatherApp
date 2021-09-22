package com.rudra.weatherinformationapplication.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rudra.weatherinformationapplication.R
import com.rudra.weatherinformationapplication.ui.activities.LoginActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var numberEditText: EditText
    private lateinit var otpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        numberEditText = findViewById(R.id.numberEditText)
        otpButton = findViewById(R.id.otpButton)

        otpButton.setOnClickListener {
            // below line is for checking weather the user
            // has entered his mobile number or not.
            if (TextUtils.isEmpty(numberEditText.text.toString())) {
                // when mobile number text field is empty
                // displaying a toast message.
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter a valid phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // if the text field is not empty we are calling our
                // send OTP method for getting OTP from Firebase.
                val phone = "+91" + numberEditText.text.toString()
                if (isValidMobile(phone)) {
                    sendVerificationCode(phone)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter a valid mobile number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val intent = Intent(this@LoginActivity, VerifyOtpActivity::class.java)
        intent.putExtra("number", number)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        fun isValidMobile(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches()
        }
    }
}