package edu.rose.snack.snackplus.utils

import edu.rose.snack.snackplus.Models.Item
import edu.rose.snack.snackplus.model.Order

class OrdersHardCode{
    companion object {
        var order: Order = Order("Jerry","Rose Hulman", "81243233333", 29F, items=mutableListOf<Item>(Item("banana",6,3.0F),Item("apple",6,3.20F),Item("beef",6,0.8F))
        )
        fun getInstance(): Order{
            return order
        }
    }
}
