package com.example.myfinalapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class DisplayActivity : AppCompatActivity()  ,AdapterView.OnItemClickListener {
    var list1 : MutableList<String> = ArrayList()
    lateinit var listView : ListView
    private var arrayAdapter:ArrayAdapter<String> ?= null
    var list : MutableList<Task> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        val intent = getIntent()
        val list_name : String = intent.getStringExtra("ListName").toString()
        setTitle(list_name)
        var helper = DataBaseFunctionsHandler(applicationContext)
        list = helper.readTaskInOneList(list_name)
        if(list.count() == 0)
        {
            Toast.makeText(this , "No tasks in this list." , Toast.LENGTH_SHORT).show()
        }
        else
        {
            for(task in list)
            {
                list1.add(task.task_name)
            }

            listView=findViewById(R.id.multiple_list_view)
            arrayAdapter = ArrayAdapter(applicationContext , android.R.layout.simple_list_item_multiple_choice, list1)

            listView?.adapter = arrayAdapter
            listView?.choiceMode = ListView.CHOICE_MODE_MULTIPLE
            listView?.onItemClickListener = this
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items:String = parent?.getItemAtPosition(position) as String
        //Toast.makeText(applicationContext ,"Subject Name $items" , Toast.LENGTH_SHORT).show()
    }

    
}