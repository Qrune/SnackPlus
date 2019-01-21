package edu.rose.snack.snackplus

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

const val SELECTED_ITEMS="edu.rose-hulman.zhengj2.SELECTED_ITEMS"
const val TOTAL="edu.rose-hulman.zhengj2.TOTAL"

class ItemSelectActivity : AppCompatActivity(), ItemSelectListFragment.OnItemSelectItemSelected {

    private lateinit var textViewTotal: TextView
    private lateinit var buttonCheckout: Button
    private var total = 0F
    private var selectedItems= mutableListOf<Item>()
    override fun onItemSelectItemSelected(item: Item?, isRemove: Boolean) {
        total += item!!.price * when (isRemove) {
            true -> -1
            false -> 1
        }
        selectedItems.add(item)
        textViewTotal.text = "Total: $"+total.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_item_select_main)

        textViewTotal = findViewById(R.id.text_item_select_checkout_total)
        buttonCheckout = findViewById(R.id.btn_item_select_checkout)


        buttonCheckout.setOnClickListener {
            var intent = Intent(this, CheckoutActivity::class.java)
            var itemNames= arrayListOf<String>()
            for (item in selectedItems){
                itemNames.add(item.name)
            }
            intent.putStringArrayListExtra(SELECTED_ITEMS,itemNames)
            intent.putExtra(TOTAL,total)
            startActivity(intent)
        }

        val fragment = ItemSelectListFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_item_select_item_list, fragment)
        ft.commit()
    }
}
