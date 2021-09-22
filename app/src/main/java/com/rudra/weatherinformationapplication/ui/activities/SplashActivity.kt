package com.rudra.weatherinformationapplication.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.rudra.weatherinformationapplication.R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Handler
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        setContentView(R.layout.activity_splash)

        // Create an executor that executes tasks in a background thread.
        val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        backgroundExecutor.execute {
            // Your code logic goes here.
            val sp = providesSharedPreference()
            i = if (sp.getBoolean("isLogin", false)) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
        }
        backgroundExecutor.schedule({
            startActivity(i)
            finish()
        }, 2, TimeUnit.SECONDS)
    }

    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
    private fun providesSharedPreference(): SharedPreferences {
        val sharedPreferences: SharedPreferences

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sharedPreferences = EncryptedSharedPreferences.create(
                application,
                VerifyOtpActivity.login_pref,
                getMasterKey(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        } else {
            sharedPreferences =
                application.getSharedPreferences(
                    VerifyOtpActivity.login_pref,
                    Context.MODE_PRIVATE
                )
        }
        return sharedPreferences
    }

    companion object {
        const val login_pref = "LOGIN_PREFS"
    }
}