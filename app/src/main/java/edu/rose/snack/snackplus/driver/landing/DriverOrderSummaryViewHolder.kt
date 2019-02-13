package edu.rose.snack.snackplus.driver.landing

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.driver.landing.DriverOrderSummaryAdapter
import java.io.File

class DriverOrderSummaryViewHolder: RecyclerView.ViewHolder {
    var adapter: DriverOrderSummaryAdapter
    var cardView: CardView
    val itemName: TextView = itemView.findViewById(R.id.textView_driver_order_summary_item_name)
    val itemCount: TextView = itemView.findViewById(R.id.textView_driver_order_summary_item_count)
    val itemPrice: TextView = itemView.findViewById(R.id.textView_driver_order_summary_item_price)
    val itemImage: ImageView = itemView.findViewById(R.id.imageView_driver_order_summary_item_image)
    private val storageReference= FirebaseStorage.getInstance().reference

    constructor(itemView: View, adapter: DriverOrderSummaryAdapter): super(itemView){
        this.adapter = adapter
        cardView = itemView.findViewById(R.id.cardView_driver_order_summary_item)
    }

    fun bind(item: Item){
        val localFile= File.createTempFile(item.name,"jpg")
        storageReference.child(item.imageUri).getFile(localFile).addOnSuccessListener {
            Picasso.get().load(localFile).into(itemImage)
        }
        itemName.text = item.name
        itemCount.text = item.quantity.toString()
        itemPrice.text = item.price.toString()
    }
}
