package edu.rose.snack.snackplus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MainActivity :
    AppCompatActivity(),
        DriverLandingFragment.OnOrderSelectedListener
{
    override fun OnOrderSelected(Id: String) {
        val fragment = DriverOrderSummary.newInstance(Id)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container, fragment)
        ft.addToBackStack("DriverOrderSummary")
        ft.commit()
//        switchFragment(fragment)
    }

    lateinit var adapter: OrderAdapter

    private fun switchFragment(switchTo: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragement_container,switchTo)
        for (i in 0 until supportFragmentManager.backStackEntryCount){
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchFragment(DriverLandingFragment())

    }

}
