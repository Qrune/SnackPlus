package edu.rose.snack.snackplus.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp


data class Order(var items: MutableList<Item> = mutableListOf<Item>(), var total: Float=0F) {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null
}