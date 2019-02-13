package edu.rose.snack.snackplus.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rose.snack.snackplus.Constants
import edu.rose.snack.snackplus.DriverActivity

import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.CustomerActivity
import edu.rose.snack.snackplus.driver.landing.DriverLandingFragment
import edu.rose.snack.snackplus.driver.landing.DriverLandingWithOrderFragment
import edu.rose.snack.snackplus.model.User
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.lang.reflect.Field
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignUpFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class SignUpFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USER_COLLECTION)
    var AUTOCOMPLETE_REQUEST_CODE = 9
    lateinit var address: EditText
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var constraintView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        var name = constraintView.findViewById<EditText>(R.id.edit_signup_name)
        address = constraintView.findViewById(R.id.edit_signup_address)

        var email = constraintView.findViewById<EditText>(R.id.edit_signup_email)
        var phone = constraintView.findViewById<EditText>(R.id.edit_signup_phone)
        var nextBtn = constraintView.findViewById<Button>(R.id.btn_signup_next)
        nextBtn.setOnClickListener {
            userRef.document(auth.currentUser!!.uid).set(
                User(
                    name = name.text.toString(),
                    address = address.text.toString(),
                    email = email.text.toString(),
                    phone = phone.text.toString(),
                    role = "driver"
                )
            ).addOnSuccessListener {
                var intent = Intent(context, DriverActivity::class.java)
                startActivity(intent)
            }
        }



        return constraintView
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
