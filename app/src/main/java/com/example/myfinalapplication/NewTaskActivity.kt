package com.example.myfinalapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import java.util.*
import kotlin.collections.ArrayList

class NewTaskActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    lateinit var listOptions : Spinner
    var list : MutableList<String> = ArrayList()
    lateinit var calenderIcon : ImageButton
    lateinit var timeIcon : ImageButton
    lateinit var taskEditText : EditText
    lateinit var dateEditText: EditText
    lateinit var timeEditText: EditText
    lateinit var addButton: Button

    lateinit var selected_list : String
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        setTitle("New Task ")

        var context = this
        calenderIcon = findViewById(R.id.dateImageButton)
        timeIcon = findViewById(R.id.timeImageButton)
        taskEditText = findViewById(R.id.taskEditText)
        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)
        addButton = findViewById(R.id.addButton)
        listOptions = findViewById(R.id.listSpinner1) as Spinner

        pickDate()
        pickTime()

        var helper = DataBaseFunctionsHandler(applicationContext)
        list = helper.readAllLists()
        listOptions.adapter = ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 , list)

        listOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selected_list = list.get(position).toString()
            }
        }


        addButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(taskEditText.text.toString().isNotEmpty() && dateEditText.text.toString().isNotEmpty() && timeEditText.text.toString().isNotEmpty() && selected_list != "All Lists")
                {
                    var task = Task(taskEditText.text.toString() , selected_list.toString() , dateEditText.text.toString() , timeEditText.text.toString())
                    var db = DataBaseFunctionsHandler(context)
                    db.insertTask(task)
                    var intent  = Intent(this@NewTaskActivity , DisplayActivity::class.java)
                    intent.putExtra("ListName" , selected_list)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this@NewTaskActivity , "Please fill all data about task." , Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    private fun getDateTimeCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        calenderIcon.setOnClickListener{
            getDateTimeCalender()

            DatePickerDialog(this , this , year , month , day).show()
        }
    }

    private fun pickTime() {
        timeIcon.setOnClickListener{
            getDateTimeCalender()
            TimePickerDialog(this , this , hour , minute , false).show()
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month+1
        savedYear = year
        dateEditText.setText("$savedDay / $savedMonth / $savedYear ")


    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
        timeEditText.setText("$savedHour : $savedMinute")

    }



}