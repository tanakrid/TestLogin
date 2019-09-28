package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile_manager.*

class ProfileManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_manager)

        if (!intent.getBooleanExtra("isManager", false)){
            Toast.makeText(baseContext,"Failed to access manager profile.",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ProfileManager, Result::class.java))
        }

        nameShop.text = intent.getStringExtra("NameShop")

        editPassBtn.setOnClickListener { startActivity(Intent(this@ProfileManager, EditPasswordPage::class.java)) }
        delEmployeeBtn.setOnClickListener { startActivity(Intent(this@ProfileManager, DeleteEmployeePage::class.java))  }
        backBtn.setOnClickListener { startActivity(Intent(this@ProfileManager, Result::class.java))  }
    }
}
