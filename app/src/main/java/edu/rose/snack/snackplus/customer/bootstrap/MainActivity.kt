package edu.rose.snack.snackplus.customer.bootstrap

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import edu.rose.snack.snackplus.customer.login.LoginActivity
import edu.rose.snack.snackplus.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
