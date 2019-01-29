package edu.rose.snack.snackplus.customer.item_select

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.rose.snack.snackplus.R


import edu.rose.snack.snackplus.customer.item_select.ItemSelectListFragment.OnItemSelectItemSelected
import edu.rose.snack.snackplus.models.Item
//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.customer_item_select_recycler_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnItemSelectItemSelected].
 * TODO: Replace the implementation with code for your data type.
 */
class ItemSelectListAdapter(
    var context: Context,
    private val listener: OnItemSelectItemSelected?
) : RecyclerView.Adapter<ItemSelectListAdapter.ItemViewHolder>() {

//    private val mOnClickListener: View.OnClickListener
private var items= mutableListOf<Item>()
    private var itemsRef=FirebaseFirestore.getInstance().collection("items")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.customer_item_select_recycler_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(itemHolder: ItemViewHolder, position: Int) {
        val item = items[position]
        val maxQuantity=items[position].quantity
        itemHolder.itemName.text = item.name
        itemHolder.itemPrice.text = item.price.toString()
        itemHolder.itemQuantity.text ="0"
        itemHolder.btnQuantityLess.setOnClickListener{
            val quantity=itemHolder.itemQuantity.text.toString().toInt()
            if (quantity>0){
                itemHolder.itemQuantity.text=(quantity-1).toString()
                listener!!.onItemSelectItemSelected(item,true)
            }
        }
        itemHolder.btnQuantityMore.setOnClickListener{
            val quantity=itemHolder.itemQuantity.text.toString().toInt()
            if (quantity<maxQuantity){
                itemHolder.itemQuantity.text=(quantity+1).toString()

                listener!!.onItemSelectItemSelected(item,false)
            }
        }
    }

    override fun getItemCount(): Int = items.size
    fun addSnapshotListener() {
        itemsRef.addSnapshotListener{ querySnapshot: QuerySnapshot?, _ ->
            processSnapshotChanges(querySnapshot!!)
        }
    }


    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val item=Item.fromSnapshot(documentChange.document)

            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
//                    Log.d(Constants.TAG, "Adding $movieQuote")
                    items.add(0, item)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
//                    Log.d(Constants.TAG, "Removing $movieQuote")
                    val index = items.indexOfFirst { it.id == item.id }
                    items.removeAt(index)
                    notifyItemRemoved(index)
                }
                DocumentChange.Type.MODIFIED -> {
//                    Log.d(Constants.TAG, "Modifying $movieQuote")
                    val index = items.indexOfFirst { it.id == item.id }
                    items[index] = item
                    notifyItemChanged(index)
                }
            }
        }
    }

    inner class ItemViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
//        val itemPhoto:ImageView=mView.customer_checkout_recycler_view_item_image

        val itemName = mView.txt_item_select_item_name
        val itemQuantity = mView.txt_item_select_quantity
        val itemPrice = mView.txt_item_select_price
        val btnQuantityLess=mView.btn_item_select_quantity_less
        val btnQuantityMore=mView.btn_item_select_quantity_more

    }
}
