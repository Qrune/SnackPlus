package edu.rose.snack.snackplus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.driver.landing.DriverLandingFragment
import edu.rose.snack.snackplus.driver.landing.OrderAdapter
import edu.rose.snack.snackplus.driver.order.summary.DriverOrderSummary
import edu.rose.snack.snackplus.login.LoginFragment
import edu.rose.snack.snackplus.model.Order

class MainActivity :
    AppCompatActivity(),
    LoginFragment.OnLoginButtonPressedListener,
    DriverLandingFragment.OnOrderSelectedListener {


    val auth = FirebaseAuth.getInstance()
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 1

    private val orderRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.ORDER_COLLECTION)

    private fun initiallizeListeners() {
        Log.d("LOGIN","initialLogin")
        authListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            Log.d("LOGIN","statusChanged")
            if (user != null) {
                Log.d("USER", "UID: ${user.uid}")
                loginRoleDistr(user.uid)
//                switchFragment(DriverLandingFragment.newInstance(uid = user.uid))
            } else {
                Log.d("USER", "login failed")
                switchFragment(LoginFragment())
            }
        }
    }
    fun loginRoleDistr(uid: String){
        orderRef.whereEqualTo("driverId", uid).
            get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty){
                switchFragment(DriverLandingFragment.newInstance(uid = uid))
            }else{
                snapshot.documents[0].get("")
                var orders = snapshot.toObjects(Order::class.java)
                Log.d("DISTR","id"+orders.get(0).id)
                switchFragment(DriverOrderSummary.newInstance(Id = orders.get(0).id))
            }
        }
    }

    override fun OnOrderSelected(Id: String, uid: String) {
        val users = HashMap<String, String>()
        users.put("driverId",uid)
        orderRef.document(Id).update(users as Map<String, Any>)
        val fragment = DriverOrderSummary.newInstance(Id)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container, fragment)
        ft.addToBackStack("DriverOrderSummary")
        ft.commit()
//        switchFragment(fragment)
    }
    override fun onLoginButtonPressed(email: String, password: String) {
        Log.d("BTN","buttonpressed")
//        launchLoginUI()
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful){

                   Log.d("USER","LoginUnsuccseeful")
                }else{
                    var user = auth.currentUser
                    Log.d("USER",user?.uid)
                    loginRoleDistr(user!!.uid)
//                    switchFragment(DriverLandingFragment.newInstance(uid = user!!.uid))
                    Log.d("USER","Loginsuccseeful")
                }
            }
    }
    private fun launchLoginUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        startActivityForResult(loginIntent, RC_SIGN_IN)
    }
    lateinit var adapter: OrderAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchFragment(LoginFragment())
        initiallizeListeners()

    }

}
