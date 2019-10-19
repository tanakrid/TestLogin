package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.example.testlogin.model.Employee
import com.example.testlogin.model.ProfileShop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_result.*

class Result : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val database = FirebaseDatabase.getInstance().reference
        .child("store").child(ProfileShop.IdShop).child("EmployeeList")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val user = mAuth!!.currentUser
        emailData.text = user!!.email
        UID.text = user.uid
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val users = firebaseAuth.currentUser
            if (users == null){
                startActivity(Intent(this@Result, MainActivity::class.java))
                finish()
            }
        }

        signOutBtn.setOnClickListener {
            mAuth!!.signOut()
            clearStateAfterLogout()
            startActivity(Intent(this@Result, MainActivity::class.java))
            finish()
        }

        managerBtn.setOnClickListener {

            if(ProfileShop.isManager){
                startActivity(Intent(this@Result, ProfileManager::class.java))
            }else{
                Toast.makeText(baseContext, "Failed to access.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        employeeBtn.setOnClickListener {
            isNormalUser()
        }
    }

    private fun clearStateAfterLogout(){
        ProfileShop.emailManager = ""
        ProfileShop.isManager = false
        ProfileShop.IdShop = ""
        ProfileShop.nameShop = ""
        Employee.isEmployee = false
    }

    private fun isNormalUser(){
        val shopListener = object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(baseContext, "Failed to access.",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(child in p0.children){
                    if(child.child("email").value.toString() == mAuth.currentUser?.email){
                        Employee.isEmployee = true
                        break
                    }
                }

                Log.w("user", ""+Employee.isEmployee)
                if (!(Employee.isEmployee || ProfileShop.isManager)) {
                    startActivity(Intent(this@Result, ApplyEmployeePage::class.java))
                }else{
                    Toast.makeText(baseContext, "You was applied.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        database.addValueEventListener(shopListener)
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener { mAuthListener }
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null){ mAuth!!.removeAuthStateListener { mAuthListener }}
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){ moveTaskToBack(true)}
        return super.onKeyDown(keyCode, event)
    }
}
