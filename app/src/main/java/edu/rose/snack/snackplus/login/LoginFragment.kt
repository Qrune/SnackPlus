package edu.rose.snack.snackplus.login

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ToggleButton
import com.google.firebase.auth.FirebaseAuth
import edu.rose.snack.snackplus.R
import kotlinx.android.synthetic.main.log_in_main.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnLoginButtonPressedListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : Fragment() {

    private var listener: OnLoginButtonPressedListener? = null
    val auth = FirebaseAuth.getInstance()
    private var isDriver = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.log_in_main, container, false)
        val toggle: ToggleButton = view.findViewById(R.id.is_driver)
        toggle.setOnCheckedChangeListener { _, isChecked ->
            isDriver = isChecked
        }
//        var inputEmail = view.findViewById<EditText>(R.id.login_email)
//        var inputPassword = view.findViewById<EditText>(R.id.login_password)
        view.btn_login_login.setOnClickListener{
            listener?.onUIBtnPressed(isDriver)
        }
        view.btn_login_rosefire.setOnClickListener{
            listener?.onRoseBtnPressed(isDriver)
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginButtonPressedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnLoginButtonPressedListener {
        fun onRoseBtnPressed(isDriver: Boolean)
        fun onUIBtnPressed(isDriver: Boolean)
    }


}
