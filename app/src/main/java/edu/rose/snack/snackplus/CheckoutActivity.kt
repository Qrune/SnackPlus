package edu.rose.snack.snackplus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView

class CheckoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotal:TextView
    private lateinit var buttonCheckout: Button

    private val dummyItems=listOf(Item("banana",0,0.5F),Item("apple",0,0.9F),Item("peach",0,1.5F))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_checkout_main)

        textTotal=findViewById(R.id.txt_checkout_total)
        buttonCheckout=findViewById(R.id.btn_checkout_order)
        buttonCheckout.setOnClickListener{

        }

        val itemNames=intent.getStringArrayListExtra(SELECTED_ITEMS)
        for (itemName in itemNames){
            dummyItems.forEach{
                if (itemName==it.name){
                    it.quantity++
                }
            }
        }
        val total=intent.extras.get(TOTAL) as Float
        textTotal.text="Total: $"+total.toString()
        var viewManager=LinearLayoutManager(this)
        var madapter=CheckoutListAdapter(this,dummyItems)
        recyclerView=findViewById<RecyclerView>(R.id.recycler_view_checkout_item_list).apply{

            setHasFixedSize(true)
            layoutManager=viewManager
            adapter=madapter
        }
    }
}
