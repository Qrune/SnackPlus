package edu.rose.snack.snackplus.model

import com.google.firebase.firestore.DocumentSnapshot

data class User(var name:String="",
                var address:String="",
                var orderId:String="",
                var phone: String="",
                var email:String="",
                var role:String="customer",
                var customerorderId:String=""){


}