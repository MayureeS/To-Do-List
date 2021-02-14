package com.example.myfinalapplication

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    lateinit var listOptions : Spinner    //For the DropDown list that contains name of all the lists
    lateinit var taskButton : Button     //For task button at the bottom that adds new tasks to different lists
    var list : MutableList<String> = ArrayList()   //List to store names of all the lists

    var taskList : MutableList<Task> = ArrayList()
    //var list1 : MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("To Do List")


        /*Here, database is used. Default lists are already added to the table in database. readAllData( function in DatabaseHandler file returns a list of **list name**
          This list is passed to the arrayadapter and arrayadapter displays those list names as individual option in spinner.
         */
        listOptions = findViewById(R.id.listSpinner) as Spinner
        var helper = DataBaseFunctionsHandler(applicationContext)
        list = helper.readAllLists()
        list.add("+ Create New List")
        listOptions.adapter = ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 , list)



        //Using onItemSelectedListener , 1. opening a new activity which displays tasks of selected list  2. If Create new activity is selected, call function DialogBox
        listOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var totalLists = listOptions.count
                when(position) {
                    0 -> {}
                    totalLists-1 -> {
                        dialogBox()
                    }
                    else -> {
                        var intent  = Intent(this@MainActivity , DisplayActivity::class.java)
                        intent.putExtra("ListName" , list[position])
                        startActivity(intent)
                    }
                }
            }

        }



        //When task button at bottom is clicked, a new activity is opened where user can enter new tasks
        taskButton = findViewById(R.id.taskButton)
        taskButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var intent = Intent(this@MainActivity , NewTaskActivity::class.java)
                startActivity(intent)
            }
        })


    }


    //function to open a dialog box to create new list
    fun dialogBox() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.list_name_dialogbox , null)
        dialog.setView(dialogView).setTitle("Create List")

        dialog.setPositiveButton("CREATE" , { dialogInterface: DialogInterface, i: Int -> })
        dialog.setNegativeButton("CANCEL" , { dialogInterface: DialogInterface, i: Int -> })

        val customDialog = dialog.create()
        customDialog.show()
        val list_name = dialogView.findViewById<EditText>(R.id.listNameEditText)
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

            if (list_name.text.toString() == "") {
                Toast.makeText(baseContext , "Enter the new list name." , Toast.LENGTH_SHORT).show()
            }
            else
            {
                var helper = DataBaseFunctionsHandler(applicationContext)
                if(helper.readOneListName(list_name.text.toString()) == 0.toInt())
                {
                    Toast.makeText(this , "This list already exists. Name your list something else." , Toast.LENGTH_SHORT).show()
                }
                else
                {
                    var result = helper.insertList(list_name = list_name.text.toString())
                    Toast.makeText(this , "New list "+list_name.text.toString()+" is created succesfully" , Toast.LENGTH_SHORT).show()
                    var intent = Intent(this , DisplayActivity::class.java)
                    intent.putExtra("ListName" , list_name.text.toString() )
                    startActivity(intent)
                    customDialog.dismiss()
                    val count = list.count()
                    list.add(count-1 , list_name.text.toString())
                    listOptions.adapter = ArrayAdapter<String>(this@MainActivity , android.R.layout.simple_list_item_1 , list)
                }

            }


        }

        customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            customDialog.dismiss()
        }



    }

}