package edu.rose.snack.snackplus.customer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.BottomNavigationMenu
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.ProfileFragment
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.checkout.CheckoutFragment
import edu.rose.snack.snackplus.customer.item_select.ItemSelectListFragment
import edu.rose.snack.snackplus.customer.order_details.OrderDetailsFragment
import edu.rose.snack.snackplus.login.LoginActivity
import edu.rose.snack.snackplus.model.User
import kotlinx.android.synthetic.main.activity_customer.*

class CustomerActivity : AppCompatActivity(),
    ItemSelectListFragment.OnCheckoutListener, ProfileFragment.OnLogoutBtnListener,CheckoutFragment.OnViewOrderDetails{
    private var orderDetailsAvailable:Boolean=false
    private var usersRef=FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION)

    fun changeViewOrderDetailsState(state:Boolean){
        orderDetailsAvailable=state
        customer_navigation.menu.getItem(0).isEnabled = state
    }
    override fun onViewOrderDetails() {

        Toast.makeText(this,"Order placed",Toast.LENGTH_LONG).show()
        changeViewOrderDetailsState(true)
        customer_navigation.menu.getItem(0).isChecked=true
        switchFragment(OrderDetailsFragment())
    }

    val auth = FirebaseAuth.getInstance()


    override fun onLogoutBtnPressed() {
        auth.signOut()
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(
            this, "Signed out",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onCheckout(selectedItems: Map<String, Int>, total: Float) {
        Log.d("CHECKOUT", "checkout clicked")
        val fragment = CheckoutFragment()
        fragment.attachOnViewOrderDetailsListener(this)
        val bundle = Bundle()
        bundle.putStringArray(CheckoutFragment.KEYS,selectedItems.keys.toTypedArray())
        bundle.putIntArray(CheckoutFragment.VALUES,selectedItems.values.toIntArray())
        bundle.putFloat(CheckoutFragment.TOTAL, total)
        fragment.arguments = bundle
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container_customer, fragment)
        ft.addToBackStack(ItemSelectListFragment.TAG)
        ft.commit()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
        switchFragment(ItemSelectListFragment())

//        usersRef.add(User(address = "5500 Wabash Avenue",email="happy@happy.com",name = "Jerry",phone = "322232322323",role="customer"))
//        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.customer_navigation)

        customer_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        customer_navigation.menu.getItem(0).isChecked = false
        customer_navigation.menu.getItem(1).isChecked = true
        usersRef.get().addOnSuccessListener {
            for (d in it.documents){
                if(d.id==auth.uid){
                    var user=d
                    var orderId=user.get("orderId")
                    if(orderId !=null){

                        changeViewOrderDetailsState(true)
                    }else{
                        changeViewOrderDetailsState(false)
                    }
                }
            }
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->


        when (item.itemId) {
            R.id.navigation_list  -> {
//                message.setText(R.string.title_home)
                switchFragment(OrderDetailsFragment())
                Log.d("dlfkj","switch to OrderDetailsFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_order_details -> {
//                message.setText(R.string.title_dashboard)
                switchFragment(ItemSelectListFragment())
                Log.d("dlfakjf","Switch to ItemSelectFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                switchFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onAttachFragment(fragment: Fragment?) {
        if (fragment is ItemSelectListFragment) {
            fragment.setOnCheckoutListener(this)
        }
    }

    private fun switchFragment(switchTo: Fragment) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container_customer, switchTo)
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()

    }
}
