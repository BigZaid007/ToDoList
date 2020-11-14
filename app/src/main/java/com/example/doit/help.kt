package com.example.doit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.activity_thesettings.*

class help : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)



        toolbarhelp.setNavigationOnClickListener{
            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()

        }


    }
}
