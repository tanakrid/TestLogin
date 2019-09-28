package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_result.*

val emailManager:String? = null
class Result : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val TAG:String = "Result Activity"
    var isManager: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        mAuth = FirebaseAuth.getInstance()

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
            Log.d(TAG, "Signed out!")
            startActivity(Intent(this@Result, MainActivity::class.java))
            finish()
        }

        // will check permission manager and send some profile to display in profileManagerActivity
        goToManager()

        employeeBtn.setOnClickListener { startActivity(Intent(this@Result, ApplyEmployeePage::class.java)) }
    }

    fun goToManager(){

        var shopReference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        var nameShop:String? = null

        val shopListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                managerBtn.setOnClickListener {
                    var currentUserEmail = currentUser!!.email.toString()
                    Log.d(TAG, "currentUserEmail : ${currentUserEmail}")
                    nameShop = dataSnapshot.child("NameShop").value.toString()
                    Log.d(TAG, "nameShop : ${nameShop}")
                    startActivity(
                        Intent(this@Result, ProfileManager::class.java)
                            .putExtra(
                            "isManager", dataSnapshot.child("EmailManager").value.toString() == currentUserEmail)
                            .putExtra("NameShop", nameShop)
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                managerBtn.setOnClickListener {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    Toast.makeText(baseContext, "Failed to access.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        shopReference.addListenerForSingleValueEvent(shopListener)
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
