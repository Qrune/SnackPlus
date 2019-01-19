package edu.rose.snack.snackplus.utils

import edu.rose.snack.snackplus.model.Order

class OrdersHardCode{
    companion object {
        var order: Order = Order("Jerry","Rose Hulman", "81243233333", 29)
        fun getInstance(): Order{
            return order
        }
    }
}
