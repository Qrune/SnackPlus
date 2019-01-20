package edu.rose.snack.snackplus

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import edu.rose.snack.snackplus.model.Order
import edu.rose.snack.snackplus.utils.OrdersHardCode

class OrderAdapter(var context: Context, var listener: DriverLandingFragment.OnOrderSelectedListener?): RecyclerView.Adapter<OrderViewHolder>(){
    private val orders = ArrayList<Order>()
    private val orderRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.ORDER_COLLECTION)
    private lateinit var listenerRegistration: ListenerRegistration
    fun addSnapshotListener(){
        listenerRegistration = orderRef
            .orderBy(Order.LAST_TOUCHED_KEY)
            .addSnapshotListener{ querySnapshot, e ->
                if (e!=null){
                    return@addSnapshotListener
                }
                processSnapshotChanges(querySnapshot!!)
            }
    }
    private fun processSnapshotChanges(querySnapshot: QuerySnapshot){
        for (documentChange in querySnapshot.documentChanges){
            val order = Order.fromSnapshot(documentChange.document)
            when (documentChange.type){
                DocumentChange.Type.ADDED -> {
                    orders.add(0,order)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    for ((k, o) in orders.withIndex()){
                        if (o.id == order.id){
                            orders.removeAt(k)
                            notifyItemRemoved(k)
                            break
                        }
                    }
                }
                DocumentChange.Type.MODIFIED ->{
                    for ((k,o) in orders.withIndex()){
                        if (o.id == order.id){
                            orders[k] = order
                            notifyItemChanged(k)
                            break
                        }
                    }
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(viewHolder: OrderViewHolder, index: Int) {
        viewHolder.bind(orders[index])
    }

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.driver_landing_item, parent, false)
        return OrderViewHolder(view, this)
    }

    fun add(order: Order){
        orderRef.add(order)
    }
    fun remove(position: Int){
        orderRef.document(orders[position].id).delete()
    }

    fun selectOrderAt(adapterPosition: Int) {
        var Id = orders[adapterPosition].id
        Log.d("Order",Id)
        listener?.OnOrderSelected(Id)
    }
}
