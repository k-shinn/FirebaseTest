package com.kei.firebasetest

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.kei.firebasetest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.button1.setOnClickListener { _ ->
            val user = HashMap<String, Any>().apply {
                put("first", "Ada")
                put("last", "Livelace")
                put("born", 1815)
            }
            addUser(user)
        }

        binding.button2.setOnClickListener { _ ->
            val user = HashMap<String, Any>().apply {
                put("first", "Alan")
                put("middle", "Mathison")
                put("last", "Turing")
                put("born", 1912)
            }
            addUser(user)
        }

        binding.button3.setOnClickListener { _ ->
            get()
        }

    }

    private fun addUser(user: Map<String, Any>) {
        firestore.collection("users")
                .add(user)
                .addOnSuccessListener { document ->
                    val message = "DocumentSnapshot added with ID: " + document.id
                    Log.d(TAG, message)
                    showToast(message)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    showToast("Error adding document")
                }
    }

    private fun get() {
        firestore.collection("users")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "getResult size: " + task.getResult().size())
                        showToast("getResult size: " + task.getResult().size())
                        for (documentSnapshot in task.getResult()) {
                            Log.d(TAG, documentSnapshot.id + " => " + documentSnapshot.data)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                        showToast("Error getting documents.")
                    }
                }
    }

    private fun showToast(message: String) =
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}
