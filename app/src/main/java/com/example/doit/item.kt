package com.example.doit

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.DBHandler
import com.example.todolist.DTO.ToDoItem
import com.example.todolist.INTENT_TODO_ID
import com.example.todolist.INTENT_TODO_NAME
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_thesettings.*
import kotlinx.android.synthetic.main.activity_thesettings.toolbarsetting
import kotlinx.android.synthetic.main.rv_child_dashborad.*
import kotlinx.android.synthetic.main.rv_child_item.*


class item : AppCompatActivity() {

    lateinit var dbHandler: DBHandler
    var todoid: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(tool2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = intent.getStringExtra(INTENT_TODO_NAME)
        tool2.setNavigationOnClickListener{


            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()
        }







        dbHandler = DBHandler(this)
        todoid = intent.getLongExtra(INTENT_TODO_ID, -1)

        rv_item.layoutManager = LinearLayoutManager(this)


        fab_item.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Add Sub Task")
            val view = layoutInflater.inflate(R.layout.dialog_dashborad, null)
            val toDoName = view.findViewById<EditText>(R.id.ev_todo)
            dialog.setView(view)

            dialog.setPositiveButton("Add") { _: DialogInterface, i: Int ->
                if (toDoName.text.isNotEmpty()) {
                    val item = ToDoItem()
                    item.itemName = toDoName.text.toString()
                    item.toDoId = todoid
                    item.isCompleted = false
                    dbHandler.addToDoItem(item)
                    refreshList()
                }
            }

            dialog.setNegativeButton("Cancel") { _: DialogInterface, i: Int ->
            }
            dialog.show()

        }
    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }
   private fun refreshList(){
        rv_item.adapter = ItemAdapter(this, dbHandler, dbHandler.getToDoItems(todoid))
    }
    fun update(toDoItem: ToDoItem){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Edit Your Task")
        val view = layoutInflater.inflate(R.layout.dialog_dashborad, null)
        val toDoName = view.findViewById<EditText>(R.id.ev_todo)
        toDoName.setText(toDoItem.itemName)
        dialog.setView(view)
        dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
            if (toDoName.text.isNotEmpty()) {
                toDoItem.itemName = toDoName.text.toString()
                dbHandler.updateToDotem(toDoItem)
                refreshList()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

        }
        dialog.show()
    }








    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }


    class ItemAdapter(val activity: item, val dbHandler: DBHandler, val list: MutableList<ToDoItem>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_item,parent,false))
        }

        override fun getItemCount(): Int {
            return list.size

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemName.text = list[position].itemName
            holder.itemName.isChecked = list[position].isCompleted
            holder.itemName.setOnClickListener{

                if(holder.itemName.isChecked){
                var mediaPlayer: MediaPlayer? = MediaPlayer.create(activity,R.raw.ring)
                mediaPlayer?.start()


                }
                list[position].isCompleted = !list[position].isCompleted
                dbHandler.updateToDoItem(list[position])
            }


            holder.delete.setOnClickListener {
                val dialog = AlertDialog.Builder(activity)
                dialog.setTitle("Delete Task")
                dialog.setMessage("Do you want to delete this task ?")
                dialog.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                   activity.dbHandler.deleteToDoItem(list[position].id)
                    activity.refreshList()

                }
                dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

                }
                dialog.show()
            }

            holder.edit.setOnClickListener{

                activity.update(list[position])
            }




        }

        class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
            val itemName: CheckBox = view.findViewById(R.id.cb_item)
            val card : CardView=view.findViewById(R.id.carditem)
            val delete : ImageView =view.findViewById(R.id.delete)
            val edit : ImageView =view.findViewById(R.id.edit)
        }
    }
}