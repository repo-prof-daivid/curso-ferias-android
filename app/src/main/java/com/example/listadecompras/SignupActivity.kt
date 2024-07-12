package com.example.listadecompras

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecompras.databinding.ActivitySingUpBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        // TODO IMPLEMENT CONFIG CREATE USER
        val auth = FirebaseAuth.getInstance()
        //auth.createUserWithEmailAndPassword().addOnSuccessListener {  }.addOnFailureListener {  }
    }
}