package edu.rose.snack.snackplus

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnLogoutBtnListener? = null
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var constraintsView = inflater.inflate(R.layout.fragment_profile, container, false)
        var userName = constraintsView.findViewById<EditText>(R.id.edit_name)
        var userEmail = constraintsView.findViewById<EditText>(R.id.edit_email)
        var userAddress = constraintsView.findViewById<EditText>(R.id.edit_address)
        var userPhone = constraintsView.findViewById<EditText>(R.id.edit_phone_number)
        var switchRole = constraintsView.findViewById<Switch>(R.id.switch_driver_mode)
        var saveBtn = constraintsView.findViewById<Button>(R.id.btn_profile_save)
        saveBtn.setOnClickListener {
            val profile = HashMap<String, String>()
            profile.put("name", userName.text.toString())
            profile.put("address",userAddress.text.toString())
            profile.put("phone",userPhone.text.toString())
            profile.put("email",userEmail.text.toString())
            if (switchRole.isChecked){
                profile.put("role","driver")
            }else{
                profile.put("role","customer")
            }
            userRef.document(auth.currentUser!!.uid).update(profile as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(
                    context, "Update Successful",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener{
                Toast.makeText(
                    context, "Update fail " + it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        var logoutBtn = constraintsView.findViewById<Button>(R.id.btn_logout)
        logoutBtn.setOnClickListener {
            listener?.onLogoutBtnPressed()
        }
        userRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {snapshot ->
            userName.setText(snapshot["name"] as String)
            userAddress.setText(snapshot["address"] as String)
            userEmail.setText(snapshot["email"] as String)
            userPhone.setText(snapshot["phone"] as String)
            switchRole.isChecked = (snapshot["role"] as String).equals("driver")

        }
        return constraintsView
    }

    // TODO: Rename method, update argument and hook method into UI event


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLogoutBtnListener) {
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
    interface OnLogoutBtnListener {
        // TODO: Update argument type and name
        fun onLogoutBtnPressed()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
