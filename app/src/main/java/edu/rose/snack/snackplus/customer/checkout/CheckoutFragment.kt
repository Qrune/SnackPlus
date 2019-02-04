package edu.rose.snack.snackplus.customer.checkout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import edu.rose.snack.snackplus.R
import kotlinx.android.synthetic.main.customer_checkout_main.view.*

/*
class CheckoutFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotal: TextView
    private lateinit var buttonCheckout: Button
private lateinit var selectedItems:MutableMap<String,Int>
    var total: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            var keys=it.getStringArray(CheckoutFragment.KEYS)
            var values=it.getIntArray(CheckoutFragment.VALUES)
            selectedItems= mutableMapOf()
            for ((i,k) in keys.withIndex()){
                selectedItems[k] = values[i]
            }
            total=it.getFloat(CheckoutFragment.TOTAL, total)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.customer_checkout_main, container, false)

        super.onCreate(savedInstanceState)


        textTotal = view.txt_checkout_total
        buttonCheckout = view.btn_checkout_order
        textTotal.text = "Total: $%.2f".format(total)

        var madapter = CheckoutListAdapter(selectedItems, total)
        buttonCheckout.setOnClickListener {
            madapter.placeOrder()
        }
        recyclerView = view.recycler_view_checkout_item_list
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = madapter
        }

        return view
    }

    companion object {
        const val TAG = "CHECKOUT"

        const val KEYS = "KEYS"
        const val VALUES = "VALUES"
        const val TOTAL = "TOTAL"
    }
}
*/

class CheckoutFragment : Fragment(), CheckoutListAdapter.OnOrderPlacedListener {
    override fun onOrderPlaced(orderId: String) {
        // update use.orderId field
//        Toast.makeText(context,"orderPlaced: $orderId",Toast.LENGTH_LONG)
//        textTotal.text=orderId
    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotal: TextView
    private lateinit var buttonCheckout: Button
    private lateinit var selectedItems: MutableMap<String, Int>
    var total: Float = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            var keys = it.getStringArray(CheckoutFragment.KEYS)
            var values = it.getIntArray(CheckoutFragment.VALUES)
            selectedItems = mutableMapOf()
            for ((i, k) in keys.withIndex()) {
                selectedItems[k] = values[i]
            }
            total = it.getFloat(CheckoutFragment.TOTAL, total)
        }
    }

    private var textAddress: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.customer_checkout_main, container, false)

        super.onCreate(savedInstanceState)


        textTotal = view.txt_checkout_total
        buttonCheckout = view.btn_checkout_order
        textAddress = view.txt_checkout_address

        textTotal.text = "Total: $%.2f".format(total)

        var madapter = CheckoutListAdapter(selectedItems, total)
        madapter.attachOnOrderPlacedListener(this)
        buttonCheckout.setOnClickListener {
            madapter.placeOrder(textAddress!!.text.toString())
        }
        recyclerView = view.recycler_view_checkout_item_list
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = madapter
        }

        return view
    }

    companion object {
        const val TAG = "CHECKOUT"

        const val KEYS = "KEYS"
        const val VALUES = "VALUES"
        const val TOTAL = "TOTAL"
    }
}