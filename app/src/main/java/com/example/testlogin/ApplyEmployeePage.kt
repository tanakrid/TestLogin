package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_apply_employee_page.*
import kotlinx.android.synthetic.main.activity_edit_password_page.backBtn

class ApplyEmployeePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_employee_page)

        submitBtn.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("message")

            myRef.setValue("Hello, World!")
        }
        backBtn.setOnClickListener { startActivity(Intent(this@ApplyEmployeePage, Result::class.java)) }
        cancelBtn.setOnClickListener { startActivity(Intent(this@ApplyEmployeePage, Result::class.java)) }
    }
}
