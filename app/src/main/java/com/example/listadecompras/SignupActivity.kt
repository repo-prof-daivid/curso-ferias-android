package com.example.listadecompras

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecompras.databinding.ActivitySingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        binding.btnSingUp.setOnClickListener {
            binding.edtPwd.error = null
            binding.edtPwdConfirmation.error = null

            val userName = binding.edtName.text
            val userEmail = binding.edtEmail.text
            val userPwd = binding.edtPwd.text
            val userConfirmPwd = binding.edtPwdConfirmation.text

            if (
                userName.isNullOrEmpty() &&
                userEmail.isNullOrEmpty() &&
                userPwd.isNullOrEmpty() &&
                userConfirmPwd.isNullOrEmpty()
            ) {
                Toast.makeText(this@SignupActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            } else if (userPwd.toString() != userConfirmPwd.toString()) {
                binding.edtPwd.error = "Verifique o Campo!"
                binding.edtPwdConfirmation.error = "Verifique o Campo!"
                Toast.makeText(this@SignupActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            } else {
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(
                    userEmail.toString(), userPwd.toString()
                ).addOnSuccessListener {
                    val user = User(uId = auth.currentUser?.uid.orEmpty(), email = userEmail.toString(), name = userName.toString())
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection("USERS").document(user.uId).set(user)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@SignupActivity, "Ops, parece que houve um problema, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}