package edu.rose.snack.snackplus

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import edu.rose.snack.snackplus.model.Order

class OrderViewHolder: RecyclerView.ViewHolder {
     var adapter: OrderAdapter
    var cardView: CardView
    val customerNameTextView: TextView = itemView.findViewById(R.id.textView_customer_name_driver_landing)
    val customerTotalTextView: TextView = itemView.findViewById(R.id.textView_order_total_driver_landing)
    val customerAddressTextView: TextView = itemView.findViewById(R.id.textView_customerAddress_driver_landing)

    constructor(itemView: View, adapter: OrderAdapter): super(itemView){
        this.adapter = adapter
        cardView = itemView.findViewById(R.id.cardView_driver_landing_item)
    }

    fun bind(order: Order){
       customerNameTextView.text = order.customerName
        customerAddressTextView.text = String.format("Address: %s", order.customerAddress)
        customerTotalTextView.text = String.format("$ %d",order.orderTotal)

    }
}
