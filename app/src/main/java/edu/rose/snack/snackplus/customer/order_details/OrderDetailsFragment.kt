package edu.rose.snack.snackplus.customer.order_details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.model.Order
import kotlinx.android.synthetic.main.customer_order_details_item_main.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [OrderDetailsFragment.OnListFragmentInteractionListener] interface.
 */
class OrderDetailsFragment : Fragment() {

    // TODO: Customize parameters
//    private var columnCount = 1
//
//    private var listener: OnListFragmentInteractionListener? = null

    private val ordersRef = FirebaseFirestore.getInstance().collection(Constants.ORDER_COLLECTION)
    private val usersRef = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION)
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.customer_order_details_item_main, container, false)

        // Set the adapter
        val recyclerView = view.recycler_view_order_details_list
        if (recyclerView is RecyclerView) {
            if (auth.uid != null) {
                usersRef.document(auth.uid!!).get().addOnSuccessListener {userDocument->
                    if (userDocument != null) {
                        var orderId = userDocument.get("orderId") as String
                        Log.d("OrderDetails","orderId is $orderId")
                        Log.d("OrderDetails","i am ${auth.uid}")
                        ordersRef.document(orderId).get().addOnSuccessListener {orderDocument->

                                val order = Order.fromSnapshot(orderDocument)
                                view.txt_order_details_customer_address.text =
                                    "Delivery address: ${order.customerAddress}"
                                view.txt_order_details_customer_name.text = "Customer name: ${order.customerName}"
                                view.txt_order_details_customer_phone_number.text =
                                    "Phone number: ${order.customerPhone}"
                                view.txt_order_details_driver_id.text = "Driver: ${order.driverId}"
                                view.txt_order_details_total.text = "Total: %.2f".format(order.orderTotal)

                                with(recyclerView) {
                                    setHasFixedSize(true)
                                    layoutManager = LinearLayoutManager(context)
                                    adapter = OrderDetailsAdapter(order.items)
                                }


                        }
                    }

                }
            }
        }
        return view
    }
}
