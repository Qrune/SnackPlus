package edu.rose.snack.snackplus

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.driver.landing.DriverLandingFragment
import edu.rose.snack.snackplus.driver.landing.DriverLandingWithOrderFragment
import edu.rose.snack.snackplus.driver.order.home.DriverOrderSummary
import edu.rose.snack.snackplus.login.LoginActivity
import edu.rose.snack.snackplus.login.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.HashMap
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat



class DriverActivity :
    AppCompatActivity(),
    DriverLandingFragment.OnOrderSelectedListener,
    ProfileFragment.OnLogoutBtnListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val auth = FirebaseAuth.getInstance()
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private var home_fragment: DriverOrderSummary? = null

    private val orderRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.ORDER_COLLECTION)
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                123
            )
        }
    }

    @SuppressLint("MissingPermission")
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        Log.d("DRIVER", item.itemId.toString())
        when (item.itemId) {
            R.id.navigation_list -> {
                userRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {
                    var orderId = it.getString("orderId").toString()
                    if (!orderId.equals("")) {
                        switchFragment(DriverLandingWithOrderFragment.newInstance(orderId))
                    } else {
                        switchFragment(DriverLandingFragment.newInstance())
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_order_details -> {
                userRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {
                    var orderId = it.getString("orderId").toString()
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            val loca = HashMap<String, Double>()
                            loca.put("driverLat",location!!.latitude)
                            loca.put("driverLong",location!!.longitude)
                            orderRef.document(orderId).update(loca as Map<String, Any>)
                            Log.d("LOCATION",location!!.longitude.toString())
                        }.addOnFailureListener{
                            Log.d("LOCATION",it.message)
                        }
                    if (!orderId.equals("")) {
                        switchFragment(DriverOrderSummary.newInstance(orderId))
                    } else {
                        Toast.makeText(
                            this, "You need take a order first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                //TODO deal with profile page
                switchFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }


    private fun initiallizeListeners() {
        Log.d("LOGIN", "initialLogin")
        authListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            Log.d("LOGIN", "statusChanged")
            if (user != null) {
                Log.d("USER", "UID: ${user.uid}")
//                switchFragment(DriverLandingFragment.newInstance(uid = user.uid))
            } else {
                Log.d("USER", "login failed")
                switchFragment(LoginFragment())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initiallizeListeners()
        checkPermission()
        driver_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        userRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {
            var orderId = it.getString("orderId").toString()
            if (!orderId.equals("")) {
                switchFragment(DriverLandingWithOrderFragment.newInstance(orderId))
            } else {
                switchFragment(DriverLandingFragment.newInstance())
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onLogoutBtnPressed() {
        auth.signOut()
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(
            this, "Signed out",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun OnOrderSelected(Id: String, uid: String) {
        val users = HashMap<String, String>()
        users.put("driverId", uid)
        orderRef.document(Id).update(users as Map<String, Any>)
        val order = HashMap<String, String>()
        order.put("orderId", Id)
        userRef.document(uid).update(order as Map<String, Any>)

        val fragment = DriverOrderSummary.newInstance(Id)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container, fragment)
        ft.addToBackStack("DriverOrderSummary")
        ft.commit()
        driver_navigation.menu.findItem(R.id.navigation_order_details).isChecked = true
    }


    private fun switchFragment(switchTo: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container, switchTo)
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener { authListener }
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth.removeAuthStateListener { authListener }
        }
    }


}
