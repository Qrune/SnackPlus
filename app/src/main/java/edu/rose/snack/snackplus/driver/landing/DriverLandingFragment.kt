package edu.rose.snack.snackplus.driver.landing

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import edu.rose.snack.snackplus.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UID = "arguid"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DriverLandingFragment.OnFragmentInteractionListenCer] interface
 * to handle interaction events.
 * Use the [DriverLandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DriverLandingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnOrderSelectedListener? = null
    private lateinit var adapter: OrderAdapter
    private lateinit var view: ConstraintLayout
    private var uid: String = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            uid = it.getString(ARG_UID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var constraintView = inflater.inflate(R.layout.driver_landing, container, false) as ConstraintLayout
        var recyclerView: RecyclerView = constraintView.findViewById(R.id.customer_checkout_recycler_view)
        adapter = OrderAdapter(context!!, listener, uid!!)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        adapter.addSnapshotListener()
//        adapter.add(OrdersHardCode.getInstance())
        view = constraintView
        // Inflate the layout for this fragment
        return constraintView
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOrderSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnOrderSelectedListener {
        // TODO: Update argument type and name
        fun OnOrderSelected(Id: String, uid: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DriverLandingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DriverLandingFragment().apply {
                arguments = Bundle().apply {
                    //                   putString(ARG_UID, uid)
                }
            }
    }
}
