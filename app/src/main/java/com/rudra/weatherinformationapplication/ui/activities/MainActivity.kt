package com.rudra.weatherinformationapplication.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.rudra.weatherinformationapplication.R
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    //ui
    private lateinit var currentLocationTextView: TextView
    private lateinit var chooseLocationButton: Button
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    //vars
    private var mMap: GoogleMap? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = 21.7667
    private var lng = 72.15

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentLocationTextView = findViewById(R.id.currentLocationTextView)
        chooseLocationButton = findViewById(R.id.chooseLocationButton)
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        val headerView: View = navigationView.getHeaderView(0)
        val nameTextView: TextView = headerView.findViewById(R.id.nameTextView)

        val phoneNumber = providesSharedPreference().getString("number", "none")

        Log.d(TAG, "onCreate: phoneNumber $phoneNumber")

        nameTextView.text = phoneNumber

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_logout -> logout()
                R.id.menu_history -> startActivity(
                    Intent(
                        this@MainActivity,
                        WeatherHistoryActivity::class.java
                    )
                )
            }
            // Close the drawer
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (statusOfGPS) {
            checkLocationPermission()
        } else {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener { locationSettingsResponse ->
                checkLocationPermission()
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(
                            this@MainActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }

        chooseLocationButton.setOnClickListener {
            val intent = Intent(this@MainActivity, WeatherInfoActivity::class.java)
            intent.putExtra("lat", lat.toString())
            intent.putExtra("lon", lng.toString())
            startActivity(intent)
        }
    }

    private fun logout() {
        mAuth!!.signOut()
        val sp = providesSharedPreference()
        sp.edit().clear().apply()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        moveMap()
        googleMap.setOnCameraIdleListener {
            val midLatLng = googleMap.cameraPosition.target
            Log.d(
                TAG,
                "onCameraIdle: Latitude:- " + midLatLng.latitude + " Longitude:- " + midLatLng.longitude
            )
            val yourAddresses: List<Address>
            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
            try {
                yourAddresses = geocoder.getFromLocation(midLatLng.latitude, midLatLng.longitude, 1)
                if (yourAddresses.isNotEmpty()) {
                    val yourAddress = yourAddresses[0].getAddressLine(0)
                    val yourCity = yourAddresses[0].getAddressLine(1)
                    val yourCountry = yourAddresses[0].getAddressLine(2)
                    currentLocationTextView.text = yourAddress

                    lat = midLatLng.latitude
                    lng = midLatLng.longitude
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun moveMap() {
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val locationProvider = LocationManager.NETWORK_PROVIDER
        // I suppressed the missing-permission warning because this wouldn't be executed in my
        // case without location services being enabled
        @SuppressLint("MissingPermission") val lastKnownLocation =
            locationManager.getLastKnownLocation(locationProvider)

        if (lastKnownLocation != null) {
            val userLat = lastKnownLocation.latitude
            val userLong = lastKnownLocation.longitude
            val latLng = LatLng(userLat, userLong)
            //        mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .draggable(true)
//                .title("Marker in India"));
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
            mMap!!.uiSettings.isZoomControlsEnabled = true
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
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

    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                // Got last known location. In some rare situations this can be null.
                                mAuth = FirebaseAuth.getInstance()
                                val mapFragment =
                                    fragmentManager.findFragmentById(R.id.map) as MapFragment
                                mapFragment.getMapAsync(this)
                            }
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mAuth = FirebaseAuth.getInstance()
            val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
            mapFragment.getMapAsync(this)
        }
    }

    companion object {
        const val login_pref = "LOGIN_PREFS"
        private const val TAG = "MainActivity"
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val REQUEST_CHECK_SETTINGS = 2
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}