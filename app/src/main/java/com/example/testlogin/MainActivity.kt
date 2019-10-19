package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.testlogin.model.ProfileShop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference.child("store")
    private val TAG: String = "Login Activity"
    private var email:String = ""
    private var password:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mAuth!!.currentUser != null){
            startActivity(Intent(this@MainActivity, Result::class.java))
            finish()
        }

        loginBtn.setOnClickListener{
            email = editTextEmail.text.toString()
            password = editTextPass.text.toString()
            if(email.isEmpty()){
                Toast.makeText(applicationContext, "Please enter your email address.",Toast.LENGTH_LONG).show()
                Log.d(TAG, "Email was empty!")
                return@setOnClickListener
            }
            if (password.isEmpty()){
                Log.d(TAG,"Password was empty!")
                return@setOnClickListener
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                if (!task.isSuccessful){
                    if (password.length < 6){
                        editTextPass.error = "Please check your password. Password must have minimum 6 characters."
                        Log.d(TAG, "Enter password less than 6 characters.")
                    } else{
                        Log.d(TAG, "Authentication Failed "+ task.exception)
                    }
                }else{
                    isManager()
                    startActivity(Intent(this@MainActivity, Result::class.java))
                    finish()
                }
            }
            holdIdShop()
        }
        signUp.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUp::class.java)
            startActivity(intent)
        }
    }

    // check and set store_id that user entered if this id don't exist in database will toast to warning
    // but exist shopId will be set in companion ProfileShop.shopID
    private fun holdIdShop(){
        val idShop = fillShopId.text.toString()
        val shopListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var hasStore = false
                for (child in dataSnapshot.children) {
                    try {
                        if (child.key.toString() == idShop){
                            ProfileShop.IdShop = idShop
                            ProfileShop.nameShop = child.child("NameShop").value.toString()
                            ProfileShop.emailManager = child.child("EmailManager").value.toString()
                            hasStore = true
                            Log.w("holdIdShop", ProfileShop.IdShop + " , " + ProfileShop.nameShop)
                            break
                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
                if(!hasStore){
                    Toast.makeText(baseContext, "Don't have store that has this ID",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Failed to access.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.addValueEventListener(shopListener)
    }

    // check permission of user that be manager or not then set companion ProfileShop.isManager
    private fun isManager(){
        val shopListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(baseContext, "Can't connect to database.",
                    Toast.LENGTH_SHORT).show()            }

            override fun onDataChange(p0: DataSnapshot) {
                if( p0.child(ProfileShop.IdShop).child("EmailManager").value.toString() == mAuth.currentUser?.email.toString() ){
                    ProfileShop.isManager = true
                    Log.w("isManager", ""+ProfileShop.isManager)
                }else{
                    Log.w("isManager", "not true")
                }
            }
        }
        database.addValueEventListener(shopListener)
    }
}
