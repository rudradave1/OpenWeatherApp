package com.rudra.weatherinformationapplication.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.rudra.weatherinformationapplication.R
import java.util.concurrent.TimeUnit


class VerifyOtpActivity : AppCompatActivity() {

    // ui
    private lateinit var verifyButton: Button
    private lateinit var resendButton: Button
    private lateinit var otpEditText: EditText
    private lateinit var counterTextView: TextView

    // vars
    private var verificationId = ""
    private var mAuth: FirebaseAuth? = null
    private var number = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        mAuth = FirebaseAuth.getInstance()
        counterTextView = findViewById(R.id.counterTextView)
        verifyButton = findViewById(R.id.verifyButton)
        resendButton = findViewById(R.id.resendButton)
        otpEditText = findViewById(R.id.otpEditText)
        number = intent.getStringExtra("number").toString()
        sendVerificationCode(number)

        verifyButton.setOnClickListener {
            // validating if the OTP text field is empty or not.

            if (TextUtils.isEmpty(otpEditText.text.toString())) {
                Toast.makeText(this@VerifyOtpActivity, "Please enter OTP", Toast.LENGTH_SHORT)
                    .show()
            } else {
                verifyCode(otpEditText.text.toString(), verificationId)
            }
        }

        resendButton.setOnClickListener {
            sendVerificationCode(number)
        }

        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counterTextView.text = "Seconds remaining: " + millisUntilFinished / 1000
                //here you can have your logic to set text to edittext
            }
            override fun onFinish() {
                counterTextView.text = "0"
                resendButton.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun sendVerificationCode(number: String?) {
        // this method is used for getting
        // OTP on user phone number.
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counterTextView.text = "Seconds remaining: " + millisUntilFinished / 1000
                //here you can have your logic to set text to edittext
            }
            override fun onFinish() {
                counterTextView.text = "0"
                resendButton.visibility = View.VISIBLE
            }
        }.start()
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(number!!) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    otpEditText.setText(code)
                    verifyCode(code, verificationId)
                }
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifyOtpActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Encrypted shared preference
//                    val encryption = Encryption.getDefault("Key", "Salt", ByteArray(16))
//                    val preferences = PreferenceManager.getDefaultSharedPreferences(application)

                    val editor = providesSharedPreference().edit()

                    editor.putBoolean("isLogin",true)
                    editor.putString("number", number)
                    editor.apply()

                    val i = Intent(this@VerifyOtpActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    Toast.makeText(
                        this@VerifyOtpActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    // below method is use to verify code from Firebase.
    @RequiresApi(Build.VERSION_CODES.M)
    private fun verifyCode(code: String, verificationId: String) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential)
    }

    private fun providesSharedPreference(): SharedPreferences {
        var sharedPreferences: SharedPreferences

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sharedPreferences = EncryptedSharedPreferences.create(
                application,
                login_pref,
                getMasterKey(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        } else {
            sharedPreferences =
                application.getSharedPreferences(
                    login_pref,
                    Context.MODE_PRIVATE
                )
        }
        return sharedPreferences
    }

    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    companion object {
        const val login_pref = "LOGIN_PREFS"
    }
}