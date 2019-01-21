package edu.rose.snack.snackplus


//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.rose.snack.snackplus.ItemSelectListFragment.OnItemSelectItemSelected
import kotlinx.android.synthetic.main.customer_checkout_recycler_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnItemSelectItemSelected].
 * TODO: Replace the implementation with code for your data type.
 */
class CheckoutListAdapter(
    var context: Context,
    private val items: List<Item>

) : RecyclerView.Adapter<CheckoutListAdapter.CheckoutListViewHolder>() {

//    private val mOnClickListener: View.OnClickListener


    init {
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

    inner class CheckoutListViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
//        val itemPhoto:ImageView=mView.customer_checkout_recycler_view_item_image

        val itemName = mView.customer_checkout_recycler_view_item_name
        val itemQuantity = mView.customer_chekcout_recycler_view_item_quantity
        val itemPrice = mView.customer_checkout_recycler_view_item_price

    }
}
