package com.example.ka.googlemapapi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ka.googlemapapi.MainActivity
import com.example.ka.googlemapapi.R
import com.example.ka.googlemapapi.UserClient
import com.example.ka.googlemapapi.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    //firebase
    private lateinit var authListener : FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupFirebaseAuth()
        setupOnClickListener()
        hideSoftKeyBoard()
    }

    private fun setupOnClickListener() {
        sign_in_button.setOnClickListener {
            signIn()
        }

        register_link_tv.setOnClickListener {
            val intent = Intent(this@LoginActivity, com.example.ka.googlemapapi.ui.MainActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this.authListener)
    }

    override fun onStop(){
        super.onStop()

        if(authListener!= null){
            FirebaseAuth.getInstance().removeAuthStateListener(this.authListener)
        }

    }

    private fun hideSoftKeyBoard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun setupFirebaseAuth() {
        Log.d(TAG, "Setup Firebase Auth: Start")

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth->
            val user = firebaseAuth.currentUser
            if(user != null){
                Log.d(TAG, "auth listener : ${user.uid}" )
                Toast.makeText(this@LoginActivity, "Authenticated with : ${user.email} ", Toast.LENGTH_SHORT ).show();

                val db = FirebaseFirestore.getInstance()
                val firebaseFireStoreSettings = FirebaseFirestoreSettings.Builder().build()

                db.firestoreSettings = firebaseFireStoreSettings

                val userRef = db.collection("Something").document("Something")

                userRef.get().addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Log.d(TAG, "On Complete : successfully set the user client")
                        val user = task.result!!.toObject(User::class.java)
                        (applicationContext as UserClient).setUser(user)
                    }


                }

                val intent = Intent(this@LoginActivity , MainActivity::class.java )
                intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()

            }else{
                Log.d(TAG, "auth listener, sign out ")
            }

        }
    }


    private fun signIn() {
        val emailText = email_et.text.toString()
        val passwordText = password_et.text.toString()

        if( emailText.isNotEmpty() && passwordText.isNotEmpty() ){
            showDialog()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener {
                hideDialog()
            }.addOnFailureListener{
                Toast.makeText(this@LoginActivity, "Authentication Failed", Toast.LENGTH_SHORT).show()
                hideDialog()
            }
        }else{
            Toast.makeText(this@LoginActivity, "You didn't fill in all the fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun hideDialog() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showDialog() {
        progressBar.visibility = View.VISIBLE
    }

}