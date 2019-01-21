package edu.rose.snack.snackplus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.log_in_main.*
import kotlinx.android.synthetic.main.sign_up_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_main)

        btn_login_login.setOnClickListener{
            intent= Intent(this,ItemSelectActivity::class.java)
            startActivity(intent)
        }
    }
}
