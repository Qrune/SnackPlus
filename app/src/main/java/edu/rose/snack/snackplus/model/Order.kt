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
    var items: MutableList<Item> = mutableListOf(),
    var orderTotal: Float = 0F,

    var customerId:String="",
    var driverId: String = "",
    var driverLat: Double = 37.419857,
    var driverLong: Double = -122.078827,
    var status: String="IN PROGRESS"
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


