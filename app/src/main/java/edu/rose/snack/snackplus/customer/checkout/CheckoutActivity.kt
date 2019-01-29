package edu.rose.snack.snackplus.customer.checkout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.item_select.SELECTED_ITEMS
import edu.rose.snack.snackplus.customer.item_select.TOTAL

class CheckoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotal:TextView
    private lateinit var buttonCheckout: Button
    private val ordersRef=FirebaseFirestore.getInstance().collection("orders")
//
//    private var dummyItems= mutableListOf<Item>(Item("banana",6,3.0F),Item("apple",6,3.20F),Item("beef",6,0.8F))
//
//    init{
//        dummyItems.forEach{
//            FirebaseFirestore.getInstance().collection("items").add(it)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_checkout_main)

        textTotal=findViewById(R.id.txt_checkout_total)
        buttonCheckout=findViewById(R.id.btn_checkout_order)


        val itemNames=intent.getStringArrayListExtra(SELECTED_ITEMS)
//        for (itemName in itemNames){
//            dummyItems.forEach{
//                if (itemName==it.name){
//                    it.quantity++
//                }
//            }
//        }
        val total=intent.extras.get(TOTAL) as Float
        val selectedItems=intent.extras.getStringArrayList(SELECTED_ITEMS)
        textTotal.text="Total: $"+total.toString()

        var madapter= CheckoutListAdapter(this, selectedItems)
        buttonCheckout.setOnClickListener{
            madapter.placeOrder()
            finish()
        }
        recyclerView=findViewById<RecyclerView>(R.id.recycler_view_checkout_item_list).apply{

            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(context)
            adapter=madapter
        }
    }
}
