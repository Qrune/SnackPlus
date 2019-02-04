package edu.rose.snack.snackplus.customer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.checkout.CheckoutFragment
import edu.rose.snack.snackplus.customer.item_select.ItemSelectListFragment
import edu.rose.snack.snackplus.customer.order_details.OrderDetailsFragment

class CustomerActivity : AppCompatActivity(),
    ItemSelectListFragment.OnCheckoutListener  {
    override fun onCheckout(selectedItems: Map<String, Int>, total: Float) {
        Log.d("CHECKOUT", "checkout clicked")
        val fragment = CheckoutFragment()
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

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.customer_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->


        when (item.itemId) {
            R.id.navigation_order_details -> {
//                message.setText(R.string.title_home)
                switchFragment(OrderDetailsFragment())
                Log.d("dlfkj","switch to OrderDetailsFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_browse_snacks -> {
//                message.setText(R.string.title_dashboard)
                switchFragment(ItemSelectListFragment())
                Log.d("dlfakjf","Switch to ItemSelectFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
//                message.setText(R.string.title_notifications)
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
