package edu.rose.snack.snackplus

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.driver.landing.DriverLandingFragment
import edu.rose.snack.snackplus.driver.landing.DriverLandingWithOrderFragment
import edu.rose.snack.snackplus.driver.landing.OrderAdapter
import edu.rose.snack.snackplus.driver.order.home.DriverOrderSummary
import edu.rose.snack.snackplus.login.LoginFragment
import edu.rose.snack.snackplus.model.User
import edu.rosehulman.rosefire.Rosefire
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.HashMap

class DriverActivity :
    AppCompatActivity(),
    DriverLandingFragment.OnOrderSelectedListener {


    val auth = FirebaseAuth.getInstance()
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private var home_fragment: DriverOrderSummary? = null

    private val orderRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.ORDER_COLLECTION)
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)


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
            R.id.navigation_home -> {
                userRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {
                    var orderId = it.getString("orderId").toString()
                    if (!orderId.equals("")) {
                        switchFragment(DriverOrderSummary.newInstance(orderId))
                    } else {
                        //TODO deal with non-exist order
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                //TODO deal with profile page
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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        userRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {
            var orderId = it.getString("orderId").toString()
            if (!orderId.equals("")) {
                switchFragment(DriverLandingWithOrderFragment.newInstance(orderId))
            } else {
                switchFragment(DriverLandingFragment.newInstance())
            }
        }
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
        navigation.menu.findItem(R.id.navigation_home).isChecked = true
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
