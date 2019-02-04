package edu.rose.snack.snackplus.customer.item_select

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.R
import kotlinx.android.synthetic.main.customer_item_select_main.view.*

//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemSelectListFragment.OnItemSelectItemSelected] interface.
 */
class ItemSelectListFragment : Fragment(), ItemSelectListAdapter.OnItemQuantityChangedListener {


    private lateinit var textViewTotal: TextView
    private lateinit var buttonCheckout: Button
    private var listener: OnCheckoutListener? = null
    private var total: Float = 0F
    private var itemsRef = FirebaseFirestore.getInstance().collection(Constants.ITEM_COLLECTION)
    private var items= mutableListOf<Item>()
    private var itemQuantities= mutableMapOf<String,Int>()

    fun setOnCheckoutListener(l: OnCheckoutListener) {
        listener = l
    }

    private fun addSnapshotListener() {
        itemsRef.addSnapshotListener { querySnapshot: QuerySnapshot?, _ ->
            processSnapshotChanges(querySnapshot!!)
        }
    }

    private var mAdapter: ItemSelectListAdapter?=null

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val item = Item.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
//                    Log.d(Constants.TAG, "Adding $movieQuote")
                    items.add(0, item)
                    itemQuantities[item.id] = 0
                    mAdapter?.notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
//                    Log.d(Constants.TAG, "Removing $movieQuote")
                    val index = items.indexOfFirst { it.id == item.id }
                    itemQuantities.remove(items[index].id)
                    items.removeAt(index)
                    mAdapter?.notifyItemRemoved(index)
                }
                DocumentChange.Type.MODIFIED -> {
//                    Log.d(Constants.TAG, "Modifying $movieQuote")
                    val index = items.indexOfFirst { it.id == item.id }
                    itemQuantities.remove(items[index].id)
                    items[index] = item
                    itemQuantities[item.id] = 0
                    mAdapter?.notifyItemChanged(index)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.customer_item_select_main, container, false)
        val recyclerView=view.recycler_view_item_select_list
        // Set the adapter
        if(mAdapter==null) {
            mAdapter = ItemSelectListAdapter(items, itemQuantities)
            mAdapter!!.attachItemQuantityChangedListener(this)
            addSnapshotListener()
        }

        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter

                setHasFixedSize(true)
            }
        }

        textViewTotal = view.text_item_select_checkout_total
        buttonCheckout = view.btn_item_select_checkout
        buttonCheckout.setOnClickListener {


            listener?.onCheckout(mAdapter!!.orderDetail(), total)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        textViewTotal.text="Total: %.2f".format(total)
    }

    override fun onItemQuantityChanged(item: Item, isAdd: Boolean) {
        if (isAdd) {
            total += item.price
            Log.d(ItemSelectListFragment.TAG,"total: $total")
            textViewTotal.text="Total: $%.2f".format(total)
        } else {
            total -= item.price
            Log.d(ItemSelectListFragment.TAG,"total: $total")
            textViewTotal.text="Total: $%.2f".format(total)
        }
    }

    interface OnCheckoutListener {
        fun onCheckout(selectedItems:Map<String,Int>, total: Float)
    }

    companion object {
        const val TAG = "ITEM_SELECT"
    }

}
