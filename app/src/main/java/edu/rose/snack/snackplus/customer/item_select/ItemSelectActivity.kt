package edu.rose.snack.snackplus.customer.item_select

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.checkout.CheckoutActivity
import edu.rose.snack.snackplus.models.Item

const val SELECTED_ITEMS = "edu.rose-hulman.zhengj2.SELECTED_ITEMS"
const val TOTAL = "edu.rose-hulman.zhengj2.TOTAL"

class ItemSelectActivity : AppCompatActivity(),
    ItemSelectListFragment.OnItemSelectItemSelected {

    private lateinit var textViewTotal: TextView
    private lateinit var buttonCheckout: Button
    private var total = 0F
    private var selectedItems = arrayListOf<String>()
    override fun onItemSelectItemSelected(item: Item?, isRemove: Boolean) {
        total += item!!.price * when (isRemove) {
            true -> -1
            false -> 1
        }
        total = Math.round(total * 100.0F) / 100.0F
        when (isRemove) {
            true -> {
                if (item.id in selectedItems) {
                    selectedItems.remove(item.id)
                }
            }
            false -> {
                selectedItems.add(item.id)
            }

        }
        textViewTotal.text = "Total: $" + total.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_item_select_main)

        textViewTotal = findViewById(R.id.text_item_select_checkout_total)
        buttonCheckout = findViewById(R.id.btn_item_select_checkout)


        buttonCheckout.setOnClickListener {
            var intent = Intent(this, CheckoutActivity::class.java)
            intent.putStringArrayListExtra(SELECTED_ITEMS, selectedItems)
            intent.putExtra(TOTAL, total)
            startActivity(intent)
        }

        val fragment = ItemSelectListFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_item_select_item_list, fragment)
        ft.commit()
    }
}
