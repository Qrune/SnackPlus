package edu.rose.snack.snackplus.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rose.snack.snackplus.Models.Item

data class Order(
    var customerName: String = "",
    var customerAddress: String = "",
    var customerPhone: String = "",
    var orderTotal: Int = 0,
    var driverId: String = "",
    var items: MutableList<Item> = mutableListOf(),
    var total: Float=0F
) {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun fromSnapshot(snapshot: DocumentSnapshot): Order {
            val order = snapshot.toObject(Order::class.java)!!
            order.id = snapshot.id
            return order
        }
    }

}


