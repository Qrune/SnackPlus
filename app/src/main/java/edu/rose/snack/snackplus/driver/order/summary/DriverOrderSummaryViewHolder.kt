package edu.rose.snack.snackplus.driver.order.summary

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.driver.landing.OrderAdapter
import edu.rose.snack.snackplus.model.Order

class DriverOrderSummaryViewHolder: RecyclerView.ViewHolder {
    var adapter: DriverOrderSummaryAdapter
    var cardView: CardView
    val itemName: TextView = itemView.findViewById(R.id.textView_driver_order_summary_item_name)
    val itemCount: TextView = itemView.findViewById(R.id.textView_driver_order_summary_item_count)
    val itemPrice: TextView = itemView.findViewById(R.id.textView_driver_order_summary_item_price)

    constructor(itemView: View, adapter: DriverOrderSummaryAdapter): super(itemView){
        this.adapter = adapter
        cardView = itemView.findViewById(R.id.cardView_driver_order_summary_item)
    }

    fun bind(item: Item){
        itemName.text = item.name
        itemCount.text = item.quantity.toString()
        itemPrice.text = item.price.toString()
    }
}
