package com.example.testlogin

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    private val Tag: String = "Register Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser != null){
            startActivity(Intent(this@SignUp, Result::class.java))
            finish()
        }

        createBtn.setOnClickListener {
            val email = editTextEmail.text.toString().trim{ it <= ' '}
            val password = editTextPass.text.toString().trim{ it <= ' '}

            if(email.isEmpty()){
//                toast("Please enter your email address.")
                Log.d(Tag, "Email was empty!")
                return@setOnClickListener
            }
            if (password.isEmpty()){
                Log.d(Tag,"Password was empty!")
                return@setOnClickListener
            }

            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (!task.isSuccessful){
                    if (password.length < 6){
                        editTextPass.error = "Please check your password. Password must have minimum 6 characters."
                        Log.d(Tag, "Enter password less than 6 characters.")
                    } else{
                        Log.d(Tag, "Authentication Failed "+ task.exception)
                    }
                }else{
                    Log.d(Tag, "Sign in successfully!")
                    startActivity(Intent(this@SignUp, Result::class.java))
                    finish()
                }
            }
        }

        signInBtn.setOnClickListener { startActivity(Intent(this@SignUp, MainActivity::class.java)) }
    }
}
