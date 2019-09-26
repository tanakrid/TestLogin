package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    private val TAG: String = "Login Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser != null){
            startActivity(Intent(this@MainActivity, Result::class.java))
            finish()
        }

        loginBtn.setOnClickListener{
            val email = editTextEmail.text.toString().trim{it <= ' '}
            val password = editTextPass.text.toString().trim{it <= ' '}

            if(email.isEmpty()){
                Toast.makeText(applicationContext, "Please enter your email address.",Toast.LENGTH_LONG).show()
                Log.d(TAG, "Email was empty!")
                return@setOnClickListener
            }
            if (password.isEmpty()){
                Log.d(TAG,"Password was empty!")
                return@setOnClickListener
            }

            mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                if (!task.isSuccessful){
                    if (password.length < 6){
                        editTextPass.error = "Please check your password. Password must have minimum 6 characters."
                        Log.d(TAG, "Enter password less than 6 characters.")
                    } else{
                        Log.d(TAG, "Authentication Failed "+ task.exception)
                    }
                }else{
                    Log.d(TAG, "Sign in successfully!")
                    startActivity(Intent(this@MainActivity, Result::class.java))
                    finish()
                }
            }
        }

        signUp.setOnClickListener { startActivity(Intent(this@MainActivity, SignUp::class.java)) }


    }
}
