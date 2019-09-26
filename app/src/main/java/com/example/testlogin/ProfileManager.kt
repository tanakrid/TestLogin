package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile_manager.*

class ProfileManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_manager)

        editPassBtn.setOnClickListener { startActivity(Intent(this@ProfileManager, EditPasswordPage::class.java)) }
        delEmployeeBtn.setOnClickListener { startActivity(Intent(this@ProfileManager, DeleteEmployeePage::class.java))  }
        backBtn.setOnClickListener { startActivity(Intent(this@ProfileManager, Result::class.java))  }
    }
}
