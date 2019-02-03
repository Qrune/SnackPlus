package edu.rose.snack.snackplus.driver.landing


import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import edu.rose.snack.snackplus.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ORDER_ID = "orderId"
/**
 * A simple [Fragment] subclass.
 * Use the [DriverLandingWithOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DriverLandingWithOrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var orderId: String? = null
    private lateinit var adapter: DriverOrderSummaryAdapter
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
        var constraintView = inflater.inflate(R.layout.fragment_driver_landing_with_order, container, false) as ConstraintLayout
        var recyclerView: RecyclerView = constraintView.findViewById(R.id.driver_order_summary_recyclerView)


        adapter = DriverOrderSummaryAdapter(context!!, orderId!!)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        Log.d("DRIVER","Adapter: " +adapter.getItemCount().toString())

        return constraintView
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DriverLandingWithOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(orderId: String) =
            DriverLandingWithOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ORDER_ID, orderId)
                }
            }
    }
}
