package edu.rose.snack.snackplus.driver.landing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.model.Order


class DriverOrderSummaryAdapter(var context: Context, var orderId: String): RecyclerView.Adapter<DriverOrderSummaryViewHolder>(){



    private val orderRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.ORDER_COLLECTION)
        .document(orderId)
    private var _items = mutableListOf<Item>()

    init {
        Log.d("Driver","init")
        orderRef.
            get().addOnSuccessListener { documentSnapshot ->
            Log.d("DRIVER",orderId)
           _items = documentSnapshot.toObject(Order::class.java)!!.items
            notifyDataSetChanged()
            Log.d("DRIVER",_items.size.toString()+"inside")
        }
        Log.d("DRIVER",_items.size.toString()+"outside")

    }
    override fun getItemCount(): Int {
        Log.d("DRIVER","called getItemCount")
        return _items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): DriverOrderSummaryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.driver_order_summary_item, parent, false)
        return DriverOrderSummaryViewHolder(view, this)
    }
    override fun onBindViewHolder(viewHolder: DriverOrderSummaryViewHolder, index: Int) {
        viewHolder.bind(_items[index])
    }
}
