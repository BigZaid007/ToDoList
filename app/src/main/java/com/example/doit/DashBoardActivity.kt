package com.example.doit

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.DBHandler
import com.example.todolist.DTO.ToDo
import com.example.todolist.INTENT_TODO_ID
import com.example.todolist.INTENT_TODO_NAME
import kotlinx.android.synthetic.main.activity_dash_board.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class DashBoardActivity : AppCompatActivity() {

    lateinit var dbHandler: DBHandler
    lateinit var toggle: ActionBarDrawerToggle







    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setSupportActionBar(toolbar)


//toggle for the the side list
        toggle = ActionBarDrawerToggle(this, D_layout, R.string.open, R.string.close)
        D_layout.addDrawerListener(toggle)
        toggle.syncState()
//end of the toggle



//go to the settings
        nav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.itemSet -> {
                        startActivity(Intent(this, thesettings::class.java))
                        finish()
                    }
                    R.id.itemHelp -> {
                        startActivity(Intent(this, help::class.java))
                        finish()
                    }
                    R.id.itemExit -> this.finish()
                }
                true
            }
        //end of the settings
       val now= LocalDateTime.now()
        val formatter= DateTimeFormatter
            .ofPattern("dd")


        date.text=formatter.format(now)

        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        val month =calendar[Calendar.MONTH]

        when (day) {
            Calendar.SUNDAY -> {
                dateday.text=" Sunday, "
            }
            Calendar.MONDAY -> {
                dateday.text = " Monday, "
            }
            Calendar.TUESDAY -> {
                dateday.text=" Tuesday, "
            }
            Calendar.WEDNESDAY -> {
                dateday.text=" Wednesday, "
            }
            Calendar.THURSDAY -> {
                dateday.text=" Thursday, "
            }
            Calendar.FRIDAY -> {
                dateday.text=" Friday, "
            }
            Calendar.SATURDAY -> {
                dateday.text=" Saturday, "
            }
        }
        when (month) {
            Calendar.JANUARY -> {
                monthtxt.text=" -JANUARY "
            }
            Calendar.FEBRUARY -> {
                monthtxt.text=" -FEBRUARY "
            }
            Calendar.MARCH -> {
                monthtxt.text=" -MARCH "
            }
            Calendar.APRIL -> {
                monthtxt.text=" -APRIL "
            }
            Calendar.MAY -> {
                monthtxt.text=" -MAY "
            }
            Calendar.JUNE -> {
                monthtxt.text=" -JUNE "
            }
            Calendar.JULY -> {
                monthtxt.text=" -JULY "
            }
            Calendar.AUGUST -> {
                monthtxt.text=" -AUGUST "
            }
            Calendar.SEPTEMBER -> {
                monthtxt.text=" -SEPTEMBER "
            }
            Calendar.OCTOBER -> {
                monthtxt.text=" -OCTOBER "
            }
            Calendar.NOVEMBER -> {
                monthtxt.text=" -NOVEMBER "
            }
            Calendar.DECEMBER -> {
                monthtxt.text=" -DECEMBER "
            }

        }



        date.text=formatter.format(now)





        dbHandler = DBHandler(this)
        rv_dashboard.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Add Task")
            val view = layoutInflater.inflate(R.layout.dialog_dashborad, null)
            val toDoName = view.findViewById<EditText>(R.id.ev_todo)
            dialog.setView(view)
            dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                if (toDoName.text.isNotEmpty()) {
                    val toDo = ToDo()
                    toDo.name = toDoName.text.toString()
                    dbHandler.addToDo(toDo)
                    refreshList()
                }
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

            }
            dialog.show()
        }

    }

    fun updateToDo(toDo: ToDo){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Edit Your Task")
        val view = layoutInflater.inflate(R.layout.dialog_dashborad, null)
        val toDoName = view.findViewById<EditText>(R.id.ev_todo)
        toDoName.setText(toDo.name)
        dialog.setView(view)
        dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
            if (toDoName.text.isNotEmpty()) {
                toDo.name = toDoName.text.toString()
                dbHandler.updateToDo(toDo)
                refreshList()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

        }
        dialog.show()
    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }

    private fun refreshList(){
        rv_dashboard.adapter = DashboardAdapter(this,dbHandler.getToDos())
    }


    class DashboardAdapter(val activity: DashBoardActivity, val list: MutableList<ToDo>) :
        RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_dashborad, p0, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            holder.toDoName.text = list[p1].name


            holder.edit.setOnClickListener{
                activity.updateToDo(list[p1])
            }

            holder.delete.setOnClickListener{

                    val dialog = AlertDialog.Builder(activity)
                    dialog.setTitle("Delete Task")
                    dialog.setMessage("Do you want to delete this task ?")
                    dialog.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                        activity.dbHandler.deleteToDo(list[p1].id)
                        activity.refreshList()
                    }
                    dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

                    }
                    dialog.show()



            }



            holder.card.setOnClickListener {



                val intent = Intent(activity,item::class.java)
                intent.putExtra(INTENT_TODO_ID,list[p1].id)
                intent.putExtra(INTENT_TODO_NAME,list[p1].name)
                activity.startActivity(intent)
            }

            if (p1 < 0)
            {
                activity.note.visibility=View.VISIBLE
                activity.textnote.visibility=View.VISIBLE



            }

            else
            {
                activity.note.visibility=View.INVISIBLE
                activity.textnote.visibility=View.INVISIBLE
            }



            when (p1) {

                1,5 -> holder.color.setBackgroundColor(Color.parseColor("#ff6b6b"))
                2,6,9 -> holder.color.setBackgroundColor(Color.parseColor("#0ca678"))
                0,4,10 -> holder.color.setBackgroundColor(Color.parseColor("#4dabf7"))
                3,20,7 -> holder.color.setBackgroundColor(Color.parseColor("#f59f00"))
                -1 -> if(p1<0)  {activity.note.visibility=View.VISIBLE
                activity.textnote.visibility=View.VISIBLE}

            }





        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val toDoName: TextView = v.findViewById(R.id.tv_todo_name)
            val card :CardView=v.findViewById(R.id.itemCard)
            val delete : ImageView=v.findViewById(R.id.delete)
            val edit : ImageView=v.findViewById(R.id.edit)
            val color:RelativeLayout=v.findViewById(R.id.cardcolor)





        }
    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}


