package edu.rose.snack.snackplus.customer.order_details


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import kotlinx.android.synthetic.main.customer_order_details_item.view.*
import java.io.File

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class OrderDetailsAdapter(
    val items: MutableList<Item>
) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

private val storageReference=FirebaseStorage.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_order_details_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val localFile= File.createTempFile(item.name,"jpg")
        storageReference.child(item.imageUri).getFile(localFile).addOnSuccessListener {
            Picasso.get().load(localFile).into(holder.itemImage)
        }

        holder.itemNameView.text = item.name.toString()
        holder.subTotalView.text = item.price.toString()
        holder.quantityView.text = item.quantity.toString()

    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val itemNameView: TextView = mView.customer_order_details_recycler_view_item_name
        val subTotalView: TextView = mView.customer_order_details_recycler_view_item_price
        val quantityView: TextView = mView.customer_order_details_recycler_view_item_quantity
        val itemImage=mView.customer_order_details_recycler_view_item_image
    }
}
