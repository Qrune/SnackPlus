package edu.rose.snack.snackplus.customer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.checkout.CheckoutFragment
import edu.rose.snack.snackplus.customer.item_select.ItemSelectListFragment

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
