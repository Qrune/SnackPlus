package edu.rose.snack.snackplus.customer.checkout


//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.model.Order
import kotlinx.android.synthetic.main.customer_checkout_recycler_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnItemSelectItemSelected].
 * TODO: Replace the implementation with code for your data type.
 */
class CheckoutListAdapter(
    private val selectedItems:MutableMap<String,Int>,
    private val total:Float
) : RecyclerView.Adapter<CheckoutListAdapter.CheckoutListViewHolder>() {

//    private val mOnClickListener: View.OnClickListener

    private val items = mutableListOf<Item>()
    private val itemsRef = FirebaseFirestore.getInstance().collection("items")
    private val ordersRef = FirebaseFirestore.getInstance().collection("orders")

    init {
        for (itemId in selectedItems.keys){
            if(selectedItems[itemId]!! >0) {
                itemsRef.document(itemId).get().addOnSuccessListener {
                    items.add(Item.fromSnapshot(it))
                    notifyItemInserted(0)
                }
            }
        }
        Log.d("dlkfjslakjf", selectedItems.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_checkout_recycler_item, parent, false)
        return CheckoutListViewHolder(view)
    }

    override fun onBindViewHolder(itemHolder: CheckoutListViewHolder, position: Int) {
        val item = items[position]
        Log.d("sdflksj", "items is $items")

        Log.d("adkfaldfkj ","missign key:?${item.id}")
        itemHolder.itemName.text = item.name

        itemHolder.itemPrice.text = (item.price * selectedItems.getValue(item.id)).toString()
        itemHolder.itemQuantity.text = selectedItems[item.id].toString()

    }

    override fun getItemCount(): Int = items.size

    fun placeOrder() {
        ordersRef.add(Order(customerName = "Winston", customerAddress = "dummy address", customerPhone = "8121212", items = items, orderTotal = total))
    }

    inner class CheckoutListViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val itemName = mView.customer_checkout_recycler_view_item_name
        val itemQuantity = mView.customer_chekcout_recycler_view_item_quantity
        val itemPrice = mView.customer_checkout_recycler_view_item_price
    }
}