package edu.rose.snack.snackplus.customer.checkout


//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.customer.item_select.ItemSelectListFragment.OnItemSelectItemSelected
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.models.Item
import edu.rose.snack.snackplus.models.Order
import kotlinx.android.synthetic.main.customer_checkout_recycler_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnItemSelectItemSelected].
 * TODO: Replace the implementation with code for your data type.
 */
class CheckoutListAdapter(
    var context: Context,
    val selectedItems:ArrayList<String>

) : RecyclerView.Adapter<CheckoutListAdapter.CheckoutListViewHolder>() {

//    private val mOnClickListener: View.OnClickListener

private val items= mutableListOf<Item>()
    private val itemsRef=FirebaseFirestore.getInstance().collection("items")
    private val OrdersRef=FirebaseFirestore.getInstance().collection("orders")
    init {
        itemsRef.get().addOnSuccessListener {

            val map=selectedItems.groupingBy { it }.eachCount()
            for ((itemId,count) in map){

                val item=it.documents.find { itemId==it.id }!!.toObject(Item::class.java)!!
                item.quantity=count
                items.add(item)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutListViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.customer_checkout_recycler_item, parent, false)
        return CheckoutListViewHolder(view)
    }

    override fun onBindViewHolder(itemHolder: CheckoutListViewHolder, position: Int) {
        val item = items[position]

        itemHolder.itemName.text = item.name
        itemHolder.itemPrice.text = (item.price * item.quantity).toString()
        itemHolder.itemQuantity.text = item.quantity.toString()

    }

    override fun getItemCount(): Int = items.size
    fun placeOrder() {
        var total=items.fold(0F) {
                acc,item ->
            acc+item.price*item.quantity
        }
        OrdersRef.add(Order(items,total))
    }

    inner class CheckoutListViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
//        val itemPhoto:ImageView=mView.customer_checkout_recycler_view_item_image

        val itemName = mView.customer_checkout_recycler_view_item_name
        val itemQuantity = mView.customer_chekcout_recycler_view_item_quantity
        val itemPrice = mView.customer_checkout_recycler_view_item_price

    }
}
