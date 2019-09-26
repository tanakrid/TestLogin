package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_edit_password_page.*

class EditPasswordPage : AppCompatActivity() {

    private val TAG: String = "Edit password Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password_page)

        val mAuth = FirebaseAuth.getInstance().currentUser

        submitBtn.setOnClickListener {
            var newPass = editTextNewPass.text.toString()
            var confirmPass = editTextConfirmPass.text.toString()
            if (newPass == ""){
                Toast.makeText(applicationContext, "You must enter your new password", Toast.LENGTH_LONG).show()
            } else if (confirmPass == ""){
                Toast.makeText(applicationContext, "You must enter your confirm password", Toast.LENGTH_LONG).show()
            } else if (newPass == confirmPass){
                mAuth?.updatePassword(newPass)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User password updated.")
                            Toast.makeText(applicationContext, "Edit Password Success!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@EditPasswordPage, ProfileManager::class.java))
                        }else{
                            editTextNewPass.setText("")
                            editTextConfirmPass.setText("")
                            Toast.makeText(applicationContext, "Your password must least 6 character!", Toast.LENGTH_LONG).show()
                        }
                    }
            } else{
                editTextNewPass.setText("")
                editTextConfirmPass.setText("")
                Toast.makeText(applicationContext, "Confirm password incorrect!", Toast.LENGTH_LONG).show()
            }
        }

        backBtn.setOnClickListener { startActivity(Intent(this@EditPasswordPage, ProfileManager::class.java)) }
        cancelBtn.setOnClickListener { startActivity(Intent(this@EditPasswordPage, ProfileManager::class.java)) }
    }
}
