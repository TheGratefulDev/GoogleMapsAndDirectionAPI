package com.example.ka.googlemapapi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ka.googlemapapi.R
import com.example.ka.googlemapapi.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view){
            btn_register -> {

                Log.d(TAG, "onClick: attempting to register.")
                if(input_email.text.toString().isNotEmpty()
                    && input_password.text.toString().isNotEmpty()
                    && input_confirm_password.text.toString().isNotEmpty()) {

                    if( input_password.text.toString() == input_confirm_password.text.toString() ) {
                        registerNewEmail( input_email.text.toString(), input_password.text.toString())
                    }else {
                        Toast.makeText(this, "Passwords do not Match", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "You must fill out all the fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showDialog() {
        progressBar_reg.visibility = View.VISIBLE
    }

    private fun hideDialog() {
        if (progressBar_reg.visibility == View.VISIBLE) {
            progressBar_reg.visibility = View.INVISIBLE
        }
    }

    private fun registerNewEmail(email: String, password: String) {
        showDialog()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)

            if (task.isSuccessful) {
                Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().currentUser!!.uid)

                val userBuilder = User.Builder().email(email).userName(email).userId(FirebaseAuth.getInstance().uid!!)
                val user = userBuilder.build()

                val settings = FirebaseFirestoreSettings.Builder().build()

                FirebaseFirestore.getInstance().firestoreSettings = settings


                val newUserRef = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().uid!!)

                newUserRef.set(user).addOnCompleteListener { thisTask ->
                    hideDialog()

                    if(thisTask.isSuccessful){
                        redirectLoginScreen()
                    }else{
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }

                }

            }else{
                hideDialog()
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login Screen")

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}