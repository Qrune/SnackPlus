package edu.rose.snack.snackplus

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import edu.rose.snack.snackplus.ItemSelectListFragment.OnItemSelectItemSelected
//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.customer_checkout_recycler_item.view.*
import kotlinx.android.synthetic.main.customer_item_select_recycler_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnItemSelectItemSelected].
 * TODO: Replace the implementation with code for your data type.
 */
class ItemSelectListAdapter(
    var context: Context,
    private val items: List<Item>,
    private val listener: OnItemSelectItemSelected?
) : RecyclerView.Adapter<ItemSelectListAdapter.ItemViewHolder>() {

//    private val mOnClickListener: View.OnClickListener


    init {
    }

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

    inner class ItemViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
//        val itemPhoto:ImageView=mView.customer_checkout_recycler_view_item_image

        val itemName = mView.txt_item_select_item_name
        val itemQuantity = mView.txt_item_select_quantity
        val itemPrice = mView.txt_item_select_price
        val btnQuantityLess=mView.btn_item_select_quantity_less
        val btnQuantityMore=mView.btn_item_select_quantity_more

    }
}
