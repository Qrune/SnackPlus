package edu.rose.snack.snackplus.customer.item_select


//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.CustomerActivity
import kotlinx.android.synthetic.main.customer_item_select_recycler_item.view.*
import java.io.File

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnItemSelectItemSelected].
 * TODO: Replace the implementation with code for your data type.
 */
class ItemSelectListAdapter(
    private var items: MutableList<Item>,
    private var itemQuantities: MutableMap<String, Int>

) : RecyclerView.Adapter<ItemSelectListAdapter.ItemViewHolder>() {
    private var mListener: OnItemQuantityChangedListener? = null
    private val storageReference=FirebaseStorage.getInstance().reference

    fun attachItemQuantityChangedListener(listener: OnItemQuantityChangedListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(ItemSelectListFragment.TAG, "oncreateviewholder!!!!!!!!!!!!")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.customer_item_select_recycler_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(itemHolder: ItemViewHolder, position: Int) {
        val item = items[position]
        val maxQuantity = items[position].quantity
        val localFile= File.createTempFile(item.name,"jpg")
        storageReference.child(item.imageUri).getFile(localFile).addOnSuccessListener {
            Picasso.get().load(localFile).into(itemHolder.itemImage)
        }

        itemHolder.itemName.text = item.name
        itemHolder.itemPrice.text = item.price.toString()
        itemHolder.itemQuantity.text = itemQuantities[item.id].toString()
        itemHolder.btnQuantityLess.setOnClickListener {
            if (itemQuantities[item.id]!! > 0) {
                mListener?.onItemQuantityChanged(item, false)
                itemQuantities[item.id] = itemQuantities[item.id]!! - 1
                itemHolder.itemQuantity.text = itemQuantities[item.id].toString()
            }
        }
        itemHolder.btnQuantityMore.setOnClickListener {
            if (itemQuantities[item.id]!! < maxQuantity) {
                mListener?.onItemQuantityChanged(item, true)
                itemQuantities[item.id] = itemQuantities[item.id]!! + 1
                itemHolder.itemQuantity.text = itemQuantities[item.id].toString()
            }
        }
    }

    override fun getItemCount(): Int = items.size


    inner class ItemViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

//        val itemPhoto:ImageView=mView.customer_checkout_recycler_view_item_image


        val itemName = mView.txt_item_select_item_name
        val itemQuantity = mView.txt_item_select_quantity
        val itemPrice = mView.txt_item_select_price
        val btnQuantityLess = mView.btn_item_select_quantity_less
        val btnQuantityMore = mView.btn_item_select_quantity_more
        val itemImage:ImageView=mView.img_item_select_item_image
    }

    fun orderDetail(): MutableMap<String, Int> {
        return itemQuantities
    }


    interface OnItemQuantityChangedListener {
        fun onItemQuantityChanged(item: Item, isAdd: Boolean)
    }
}

