package edu.rose.snack.snackplus.driver.order.home

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.driver.landing.DriverOrderSummaryAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ORDER_ID = "orderId"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DriverOrderSummary.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DriverOrderSummary.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DriverOrderSummary : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var orderId: String
    val auth = FirebaseAuth.getInstance()
    private lateinit var adapter: DriverOrderSummaryAdapter
    private val orderRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.ORDER_COLLECTION)

    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(ARG_ORDER_ID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var constraintView = inflater.inflate(R.layout.driver_order_summary, container, false) as ConstraintLayout
//        var recyclerView: RecyclerView = constraintView.findViewById(R.id.driver_order_summary_recyclerView)

        var customerName = constraintView.findViewById<TextView>(R.id.textView_driver_order_name)
        var customerNumber = constraintView.findViewById<TextView>(R.id.textView_driver_order_number)
        var customerAdddress = constraintView.findViewById<TextView>(R.id.textView_driver_order_address)
        var mapView = constraintView.findViewById<MapView>(R.id.driver_map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync{
            val sydney = LatLng(-34.0, 151.0)
            it.addMarker(MarkerOptions().position(sydney).title("Store Address"))
            it.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            it.setMaxZoomPreference(19.0f)
            it.setMinZoomPreference(15.0F)
        }

        orderRef.document(orderId!!).get().addOnSuccessListener {snapshot ->
            customerName.text = snapshot["customerName"] as String
            customerNumber.text = snapshot["customerPhone"] as String
            customerAdddress.text = snapshot["customerAddress"] as String
        }
        var btn_delivered = constraintView.findViewById<Button>(R.id.btn_driver_order_delivered)
        btn_delivered.setOnClickListener {
            val users = HashMap<String, String>()
            users.put("status","DELIVERED")
            orderRef.document(orderId).update(users as Map<String, Any>)
            val order = HashMap<String, String>()
            order.put("orderId","")
            userRef.document(auth.currentUser!!.uid).update(order as Map<String, Any>)

        }

//        adapter = DriverOrderSummaryAdapter(context!!, orderId)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = adapter
//        Log.d("DRIVER","Adapter: " +adapter.getItemCount().toString())

        return constraintView
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     *
//     *
//     * See the Android Training lesson [Communicating with Other Fragments]
//     * (http://developer.android.com/training/basics/fragments/communicating.html)
//     * for more information.
//     */
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DriverOrderSummary.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(Id: String) =
            DriverOrderSummary().apply {
                arguments = Bundle().apply {
                    putString(ARG_ORDER_ID, Id)
                }
            }
    }
}
