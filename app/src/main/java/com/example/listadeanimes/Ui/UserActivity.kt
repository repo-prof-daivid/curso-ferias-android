package com.example.listadeanimes.Ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadeanimes.Model.User
import com.example.listadeanimes.Ui.Home.ListaActivity
import com.example.listadeanimes.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        binding.btnUsersign.setOnClickListener {
            binding.edtpwd.error = null
            binding.edtpwdconfirm.error = null

            val userEmail = binding.edtEmail.text
            val userPwd = binding.edtpwd.text
            val userConfirmPwd = binding.edtpwdconfirm.text

            if (
                userEmail.isNullOrEmpty() &&
                userPwd.isNullOrEmpty() &&
                userConfirmPwd.isNullOrEmpty()
            ) {
                Toast.makeText(this@UserActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            } else if (userPwd.toString() != userConfirmPwd.toString()) {
                binding.edtpwd.error = "Verifique o Campo!"
                binding.edtpwdconfirm.error = "Verifique o Campo!"
                Toast.makeText(this@UserActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            } else {
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(
                    userEmail.toString(), userPwd.toString()
                ).addOnSuccessListener {
                    val userName = null
                    val user = User(uId = auth.currentUser?.uid.orEmpty(), email = userEmail.toString(), name = userName.toString())
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection("USERS").document(user.uId).set(user)
                    val intent = Intent(this,  ListaActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@UserActivity, "Ops, parece que houve um problema, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
                }
            }
           }
         }
        }
