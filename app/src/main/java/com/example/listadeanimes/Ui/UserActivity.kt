package com.example.listadeanimes.Ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadeanimes.Model.User
import com.example.listadeanimes.R
import com.example.listadeanimes.Ui.Home.ListaActivity
import com.example.listadeanimes.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private var passwordVisible = false
    private var confirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        changeVisibility()
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
            } else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail.toString()).matches()) {

                Toast.makeText(this@UserActivity, "Por favor, insira um endereço de email válido.", Toast.LENGTH_SHORT).show()
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
    private fun changeVisibility() {
        binding.btnconfirm.setOnClickListener {
            if (passwordVisible) {
                binding.edtpwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnconfirm.setImageResource(R.drawable.ic_visibility_off_24)
            } else {
                binding.edtpwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnconfirm.setImageResource(R.drawable.ic_visibility_24)
            }
            passwordVisible = !passwordVisible
            binding.edtpwd.setSelection(binding.edtpwd.text?.length ?: 0)
        }

        binding.btnconfirmvisible.setOnClickListener {
            if (confirmPasswordVisible) {
                binding.edtpwdconfirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnconfirmvisible.setImageResource(R.drawable.ic_visibility_off_24)
            } else {
                binding.edtpwdconfirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnconfirmvisible.setImageResource(R.drawable.ic_visibility_24)
            }
            confirmPasswordVisible = !confirmPasswordVisible
            binding.edtpwdconfirm.setSelection(binding.edtpwdconfirm.text?.length ?: 0)
        }
    }
        }
