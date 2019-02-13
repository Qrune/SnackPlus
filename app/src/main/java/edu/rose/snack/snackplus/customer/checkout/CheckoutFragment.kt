package edu.rose.snack.snackplus.customer.checkout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.R
import kotlinx.android.synthetic.main.customer_checkout_main.view.*


class CheckoutFragment : Fragment(), CheckoutListAdapter.OnOrderPlacedListener {
    override fun onOrderPlaced(orderId: String) {
        // update use.orderId field
//        Toast.makeText(context,"orderPlaced: $orderId",Toast.LENGTH_LONG)
//        textTotal.text=orderId
        Log.d("checkout","orderid=$orderId")
        listener!!.onViewOrderDetails()
    }

    private  var listener:OnViewOrderDetails?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotal: TextView
    private lateinit var buttonCheckout: ImageView
    private lateinit var selectedItems: MutableMap<String, Int>
    var total: Float = 0F
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)
    val auth = FirebaseAuth.getInstance()
    private lateinit var user: edu.rose.snack.snackplus.model.User


    fun attachOnViewOrderDetailsListener(l:OnViewOrderDetails){
        listener=l
    }
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
        textAddress = view.findViewById(R.id.txt_checkout_address) as EditText
        userRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {
            user = it.toObject(edu.rose.snack.snackplus.model.User::class.java)!!
            textAddress!!.setText(user.address)
        }

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
    interface OnViewOrderDetails{
        fun onViewOrderDetails()
    }

    companion object {
        const val TAG = "CHECKOUT"

        const val KEYS = "KEYS"
        const val VALUES = "VALUES"
        const val TOTAL = "TOTAL"
    }
}