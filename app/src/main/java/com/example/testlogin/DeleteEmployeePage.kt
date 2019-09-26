package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_delete_employee_page.*

class DeleteEmployeePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_employee_page)

        backBtn.setOnClickListener { startActivity(Intent(this@DeleteEmployeePage, ProfileManager::class.java)) }
    }
}
