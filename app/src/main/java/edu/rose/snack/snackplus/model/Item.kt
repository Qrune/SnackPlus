package edu.rose.snack.snackplus.Models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp


data class Item(var name: String="", var quantity: Int=0, var price: Float=0F, var imageUri:String="") {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot):Item{
            val item=snapshot.toObject(Item::class.java)!!
            item.id=snapshot.id
            return item
        }
    }

}