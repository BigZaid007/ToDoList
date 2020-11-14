package com.example.doit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_thesettings.*


class thesettings : AppCompatActivity() {

    private var MyFont = 14f
    private var titleFont = 20f






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thesettings)

        toolbarsetting.setNavigationOnClickListener{


            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()
        }


















        notify.setOnClickListener {
            if (notify.text == "ON") {
                notify.setText("OFF")
                Alerter.Companion.create(this)
                    .setTitle("Notification")
                    .setText("Notifications are On")
                    .setIcon(R.drawable.ic_notifications)
                    .setBackgroundColorRes(R.color.colorPrimaryDark)
                    .setDuration(4000)
                    .setOnClickListener(View.OnClickListener {

                        Toast.makeText(applicationContext, "Notifications ON", Toast.LENGTH_SHORT)
                            .show()
                    })
                    .show()
            } else {
                notify.setText("ON")
                Alerter.Companion.create(this)
                    .setTitle("Notification")
                    .setText("Notifications are OFF")
                    .setIcon(R.drawable.ic_notifications_off)
                    .setBackgroundColorRes(R.color.colorPrimaryDark)
                    .setDuration(4000)
                    .setOnClickListener(View.OnClickListener {

                        Toast.makeText(applicationContext, "Notifications OFF", Toast.LENGTH_SHORT)
                            .show()
                    })
                    .show()

            }


        }







          val sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            switcher.isChecked=sharedPreferences.getBoolean("PREF_SWITCH_PUSH",false)


        if (switcher.isChecked)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }


    switcher.setOnCheckedChangeListener { buttonView, isChecked ->



        if (isChecked) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

              editor.putBoolean("PREF_SWITCH_PUSH", true)

        }

        else  {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
             editor.putBoolean("PREF_SWITCH_PUSH", false)

        }
        editor.apply()



    }








    }




    }




