package com.example.jogosdivertidoss.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jogosdivertidoss.databinding.ActivitySignupBinding
import com.example.jogosdivertidoss.model.User
import com.example.jogosdivertidoss.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        // TODO VALIDAR CAMPOS DE EMAIL, NOME E SENHA (SEGUE O PADRÃO `@Ab12132`)
        binding.cadastrar.setOnClickListener {
            binding.edtsenha.error = null
            binding.cofirmsenha.error = null

            val userName = binding.Nome.text
            val userEmail = binding.Email.text
            val userPwd = binding.edtsenha.text
            val userConfirmPwd = binding.cofirmsenha.text

            if (
                userName.isNullOrEmpty() ||
                userEmail.isNullOrEmpty() ||
                userPwd.isNullOrEmpty() ||
                userConfirmPwd.isNullOrEmpty()
            ) {
                Toast.makeText(this@SignupActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            } else if (userPwd.toString() != userConfirmPwd.toString()) {
                binding.edtsenha.error = "Verifique o Campo!"
                binding.cofirmsenha.error = "Verifique o Campo!"
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