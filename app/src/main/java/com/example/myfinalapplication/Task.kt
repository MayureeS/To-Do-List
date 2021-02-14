package com.example.myfinalapplication

class Task{
    var task_name : String = ""
    var list_name : String = ""
    var date : String = ""
    var time : String = ""

    constructor(task_name:String , list_name:String , date:String , time:String) {
        this.task_name = task_name
        this.list_name = list_name
        this.date = date
        this.time = time
    }
}