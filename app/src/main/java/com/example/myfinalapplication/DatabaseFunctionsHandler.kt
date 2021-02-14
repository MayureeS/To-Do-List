package com.example.myfinalapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText as makeText1


val DATABASE_NAME = "To_Do_Database"



class DataBaseFunctionsHandler(var context: Context) : SQLiteOpenHelper(context , DATABASE_NAME , null , 1){


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Lists_Name(id INTEGER PRIMARY KEY AUTOINCREMENT , lst_name VARCHAR(100))") //Create Table in ToDoDatabase

        db?.execSQL("CREATE TABLE all_tasks( id INTEGER PRIMARY KEY AUTOINCREMENT ,task_name VARCHAR(100) , list_name VARCHAR(100) , date VARCHAR(25) , time varchar(25))")

        //Insert the default list names available in the app
        db?.execSQL("INSERT INTO Lists_Name(lst_name) VALUES ('All Lists')")
        db?.execSQL("INSERT INTO Lists_Name(lst_name) VALUES ('Work')")
        db?.execSQL("INSERT INTO Lists_Name(lst_name) VALUES ('Shopping')")
        db?.execSQL("INSERT INTO Lists_Name(lst_name) VALUES ('Wishlist')")
        db?.execSQL("INSERT INTO Lists_Name(lst_name) VALUES ('Personal')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    //This function returns the list of all the to-do list names from database
    fun readAllLists() : MutableList<String> {
        var list : MutableList<String> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT lst_name FROM Lists_Name"
        val result = db.rawQuery(query , null)
        if(result.moveToFirst()) {
            do{
                val individualListName = result.getString(result.getColumnIndex("lst_name")).toString()
                list.add(individualListName)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }


    //This function inserts the given data to the database
    fun insertList(list_name : String) : Long{
        val db = this.writableDatabase
        var cv = ContentValues()

        var db1 = this.readableDatabase
        cv.put("lst_name" , list_name)
        var result = db.insert("Lists_Name" , null , cv)
        return  result.toLong()

    }

    fun readOneListName(list_name: String) : Int{
        val db = this.readableDatabase
        val query = "SELECT lst_name FROM Lists_Name WHERE lst_name = '$list_name'"
        val result = db.rawQuery(query , null)

        if(result.count == 0)
        {
            return 1
        }
        else
        {
            return 0
        }
    }

    fun insertTask(task : Task) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("task_name", task.task_name)
        cv.put("list_name", task.list_name)
        cv.put("date", task.date)
        cv.put("time", task.time)
        db.insert("all_tasks", null, cv)


    }


    fun readTaskInOneList(list_name: String) : MutableList<Task>
    {
        var list : MutableList<Task> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT task_name,date,time FROM all_tasks WHERE list_name='$list_name'"
        val result = db.rawQuery(query , null)
        if(result.moveToFirst()) {
            do{
                val task_name = result.getString(result.getColumnIndex("task_name")).toString()
                val listName = list_name
                val date = result.getString(result.getColumnIndex("date")).toString()
                val time = result.getString(result.getColumnIndex("time")).toString()
                val task = Task(task_name , listName , date , time)
                list.add(task)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

}
