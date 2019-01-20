package edu.rose.snack.snackplus

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore


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
    private var orderId: String? = null
//    private var listener: OnFragmentInteractionListener? = null
    private val orderRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.ORDER_COLLECTION)

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
        var customerName = constraintView.findViewById<TextView>(R.id.textView_driver_order_name)
        var customerNumber = constraintView.findViewById<TextView>(R.id.textView_driver_order_number)
        var customerAdddress = constraintView.findViewById<TextView>(R.id.textView_driver_order_address)
        orderRef.document(orderId!!).get().addOnSuccessListener {snapshot ->
            customerName.text = snapshot["customerName"] as String
            customerNumber.text = snapshot["customerPhone"] as String
            customerAdddress.text = snapshot["customerAddress"] as String
        }
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
