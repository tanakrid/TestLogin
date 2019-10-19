package com.example.testlogin.model

class Employee(
    var name:String,
    var email:String,
    var empKey:String
){
    companion object{
        var isEmployee = false
    }
}
