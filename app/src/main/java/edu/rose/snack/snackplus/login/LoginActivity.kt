package edu.rose.snack.snackplus.customer.login

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
import edu.rose.snack.snackplus.MainActivity
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.driver.landing.DriverLandingFragment
import edu.rose.snack.snackplus.login.LoginFragment
import edu.rose.snack.snackplus.model.User
import edu.rosehulman.rosefire.Rosefire
import kotlinx.android.synthetic.main.log_in_main.*

class LoginActivity : AppCompatActivity(),
    LoginFragment.OnLoginButtonPressedListener{
    var isDriver: Boolean = false
    private val RC_SIGN_IN = 1
    private val RC_ROSEFIRE_LOGIN = 2
    private val ROSE_KEY: String = "e6f7352b-86c8-47c4-9fbb-021a554755cc"
    val auth = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)


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
                    createUserIfNotExist()
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
                            createUserIfNotExist()
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

    fun createUserIfNotExist() {
        val uid = auth.currentUser!!.uid
        userRef.document(uid).get().addOnSuccessListener {
            if (it == null) {
                userRef.document(uid).set(User())
            }
        }

    }

    fun roleDistribute(uid: String) {
        //if the user login as driver
        if (this.isDriver) {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //TODO check if exist a order
//            switchFragment(DriverLandingFragment.newInstance())
        } else {
            //TODO check if exist a order
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        switchFragment(LoginFragment())
    }

    private fun switchFragment(switchTo: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container, switchTo)
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }
}
