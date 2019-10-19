package com.example.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testlogin.model.Employee
import com.example.testlogin.model.ProfileShop
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_delete_employee_page.*
import kotlinx.android.synthetic.main.model.view.*

class DeleteEmployeePage : AppCompatActivity() {

    private var employeeList: ArrayList<Employee> = ArrayList()
    private var shopReference = FirebaseDatabase.getInstance().reference
    private lateinit var shopListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_employee_page)

        loadAllEmployee()

        backBtn.setOnClickListener { startActivity(Intent(this@DeleteEmployeePage, ProfileManager::class.java)) }
    }

    private fun displayEmployeeList(productList: ArrayList<Employee>) {
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        allEmployeeList.layoutManager = linearLayoutManager
        allEmployeeList.adapter = EmployeeListAdapter(productList)
    }

    private fun loadAllEmployee() {
        shopListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                employeeList.clear()

                for (child in dataSnapshot.children) {
                    try {
                        employeeList.add(Employee(
                            child.child("name").value.toString(),
                            child.child("emailEmployee").value.toString(),
                            child.key.toString()
                        ))
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }

                displayEmployeeList(employeeList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Failed to access.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        shopReference.child("store")
            .child(ProfileShop.IdShop)
            .child("EmployeeList")
            .addValueEventListener(shopListener)
    }

    inner class EmployeeListAdapter(private val myDataset: ArrayList<Employee>) :
        RecyclerView.Adapter<EmployeeListAdapter.EmployeeListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): EmployeeListViewHolder {

            val textView = LayoutInflater.from(applicationContext)
                .inflate(R.layout.model, parent, false)

            return EmployeeListViewHolder(textView)
        }

        override fun onBindViewHolder(holder: EmployeeListViewHolder, position: Int) {
            val employee = myDataset[position]

            holder.nameEmployee.text = employee.name
            holder.cardEmployee.setOnClickListener {
                Toast.makeText(baseContext, employee.name,
                    Toast.LENGTH_SHORT).show()
                shopReference.child("store")
                    .child(ProfileShop.IdShop)
                    .child("EmployeeList")
                    .child(employee.empKey)
                    .removeValue()
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size

        inner class EmployeeListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cardEmployee : CardView = view.cardEmployee
            val constraintLayout: ConstraintLayout = view.constraintLayout
            val imageEmployee: ImageView = view.imageEmployee
            val nameEmployee: TextView = view.nameEmployee
            val role: TextView = view.role
        }
    }

}
