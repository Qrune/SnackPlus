package edu.rose.snack.snackplus.customer.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import edu.rose.snack.snackplus.R
import edu.rose.snack.snackplus.customer.item_select.ItemSelectActivity
import kotlinx.android.synthetic.main.log_in_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_main)

        btn_login_login.setOnClickListener{
            intent= Intent(this, ItemSelectActivity::class.java)
            startActivity(intent)
        }
    }
}
