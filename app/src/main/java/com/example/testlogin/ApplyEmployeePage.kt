package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.testlogin.model.Employee
import com.example.testlogin.model.ProfileShop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_apply_employee_page.*
import kotlinx.android.synthetic.main.activity_edit_password_page.backBtn

class ApplyEmployeePage : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance().reference
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_employee_page)

        submitBtn.setOnClickListener {
            val empName = editTextName.text.toString().trim{ it <= ' '}
            val email = mAuth.currentUser?.email.toString()
            val key = database.child("EmployeeList").push().key
            val employee = Employee(empName, email, key.toString())

            if (key != null) {
                database.child("store")
                    .child(ProfileShop.IdShop)
                    .child("EmployeeList")
                    .child(key).setValue(employee)
                Toast.makeText(baseContext, "Apply Complete.",
                    Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(baseContext, "Apply Incomplete.",
                    Toast.LENGTH_SHORT).show()
            }

            startActivity(Intent(this@ApplyEmployeePage, Result::class.java))
        }
        backBtn.setOnClickListener { startActivity(Intent(this@ApplyEmployeePage, Result::class.java)) }
        cancelBtn.setOnClickListener { startActivity(Intent(this@ApplyEmployeePage, Result::class.java)) }
    }

//    private fun isMember():Boolean{
//        var shopReference = FirebaseDatabase.getInstance().reference
//        lateinit var shopListener: ValueEventListener
//        shopListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (child in dataSnapshot.children) {
//
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Toast.makeText(baseContext, "Failed to access.",
//                    Toast.LENGTH_SHORT).show()
//            }
//        }
//        shopReference.child("store")
//            .child(store_id)
//            .child("EmployeeList")
//            .addValueEventListener(shopListener)
//        return false
//    }
}
