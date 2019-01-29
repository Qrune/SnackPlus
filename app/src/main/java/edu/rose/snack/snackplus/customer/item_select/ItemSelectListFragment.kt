package edu.rose.snack.snackplus.customer.item_select

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.models.Item

//import edu.rose.snack.snackplus.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemSelectListFragment.OnItemSelectItemSelected] interface.
 */
class ItemSelectListFragment : Fragment() {

    private var listener: OnItemSelectItemSelected? = null

//    private val dummyItems=listOf(
//        Item("banana", 5, 0.5F),
//        Item("apple", 3, 0.9F),
//        Item("peach", 2, 1.5F)
//    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.customer_item_select_item_list, container, false)

        // Set the adapter
        var mAdapter= ItemSelectListAdapter(context!!, listener)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
                mAdapter.addSnapshotListener()
                setHasFixedSize(true)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectItemSelected) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnItemSelectItemSelected")
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
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnItemSelectItemSelected {
        // TODO: Update argument type and name
        fun onItemSelectItemSelected(item: Item?, isRemove:Boolean)
    }

}
