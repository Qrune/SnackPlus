package edu.rose.snack.snackplus.login

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.DriverActivity
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.CustomerActivity
import edu.rose.snack.snackplus.model.User
import edu.rosehulman.rosefire.Rosefire

class LoginActivity : AppCompatActivity(),
    LoginFragment.OnLoginButtonPressedListener {
    var isDriver: Boolean = false
    private val RC_SIGN_IN = 1
    private val RC_ROSEFIRE_LOGIN = 2
    private val ROSE_KEY: String = "e6f7352b-86c8-47c4-9fbb-021a554755cc"
    val auth = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)
    //    val auth = FirebaseAuth.getInstance()
    lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initiallizeListeners()
//        generateDummyItems()
        switchFragment(LoginFragment())
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

    fun generateDummyItems() {
        var itemsRef = FirebaseFirestore.getInstance().collection("items")
        var items = listOf<Item>(
            Item("banana", 3, 1f),
            Item("apple", 2, .3f),
            Item("beef", 5, 2f)
        )
        for (item in items) {
            itemsRef.add(item)
        }
    }

    override fun onRoseBtnPressed(isDriver: Boolean) {
        this.isDriver = isDriver
        val signInIntent = Rosefire.getSignInIntent(this, ROSE_KEY)
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN)
    }

    override fun onUIBtnPressed(isDriver: Boolean) {
        this.isDriver = isDriver
        val provider = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(provider)
            .build()
        startActivityForResult(loginIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed i
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Log.d("USER", user?.uid)
                    roleDistribute(user.uid)
//                    createUserIfNotExist()
//                    mapUserToObject()
//                    switchFragment(DriverLandingFragment.newInstance(uid = user.uid))
                }
            } else {
                Log.d("LOGIN", "login failed")
                switchFragment(LoginFragment())
            }
        } else if (requestCode == RC_ROSEFIRE_LOGIN) {
            val result = Rosefire.getSignInResultFromIntent(data)
            if (!result.isSuccessful) {
                // The user cancelled login
            }
            FirebaseAuth.getInstance().signInWithCustomToken(result.token)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            roleDistribute(user.uid)
//                            createUserIfNotExist()
//                            mapUserToObject()
//                            switchFragment(DriverLandingFragment.newInstance(uid = user.uid))
                        }
                    } else {
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        switchFragment(LoginFragment())
                    }

                }
        }
    }


    fun roleDistribute(uid: String) {
        //if the user login as driver
        val uid = auth.currentUser!!.uid
        userRef.document(uid).get().addOnSuccessListener {
            Log.d("CREATE", "USER CREATING")
            if (!it.exists()) {
                switchFragment(SignUpFragment())

            } else {
                Log.d("CREATE", it.toString())
                var user:User = it.toObject(User::class.java)!!
                Log.d("LOGIN",user.role)
                if (user.role.equals("driver")) {
                    Log.d("LOGIN", "DRIVER")
                    intent = Intent(this, DriverActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("LOGIN", "CUSTOMER")
                    intent = Intent(this, CustomerActivity::class.java)
                    startActivity(intent)
                }
            }
        }


    }

    private fun switchFragment(switchTo: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container_login, switchTo)
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }


}
