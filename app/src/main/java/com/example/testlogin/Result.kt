package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_result.*

class Result : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val TAG:String = "Result Activity"

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

        managerBtn.setOnClickListener { startActivity(Intent(this@Result, ProfileManager::class.java)) }
        employeeBtn.setOnClickListener { startActivity(Intent(this@Result, ApplyEmployeePage::class.java)) }
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
