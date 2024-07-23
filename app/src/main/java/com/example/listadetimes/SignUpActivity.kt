package com.example.listadetimes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadetimes.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import android.content.Intent
import android.text.InputType
import android.util.Patterns


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        changeVisibility()
    }

    private fun setUpView() {
        binding.btnSignUp.setOnClickListener {
            binding.edtPwd.error = null
            binding.edtPwdConfirmation.error = null

            val userName = binding.edtName.text
            val userEmail = binding.edtEmail.text
            val userPwd = binding.edtPwd.text
            val userConfirmPwd = binding.edtPwdConfirmation.text

            val passwordValidation = "^(?=.*@)(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$"



            if (
                userName.isNullOrEmpty() ||
                userEmail.isNullOrEmpty() ||
                userPwd.isNullOrEmpty() ||
                userConfirmPwd.isNullOrEmpty()
            ) {
                Toast.makeText(this@SignUpActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            } else if (userPwd.toString() != userConfirmPwd.toString()) {
                binding.edtPwd.error = "Verifique o Campo!"
                binding.edtPwdConfirmation.error = "Verifique o Campo!"
                Toast.makeText(this@SignUpActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            } else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail.toString()).matches()) {
                // Validação do email
                Toast.makeText(this@SignUpActivity, "Por favor, insira um endereço de email válido.", Toast.LENGTH_SHORT).show()
            }else if(userName.isEmpty()){
                // Validação de Nome
                binding.edtName.error = "O nome não pode estar vazio."
                Toast.makeText(this@SignUpActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
            }else if(!userPwd.toString().matches(passwordValidation.toRegex())){
                // Validação da senha
                binding.edtPwd.error = "A senha deve conter pelo menos um caractere especial (@), uma letra maiúscula, uma letra minúscula, um dígito e ter no mínimo 8 caracteres."
                Toast.makeText(this@SignUpActivity, "Verifique os campos e tente novamente!", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@SignUpActivity, "O Usuário foi criado com sucesso", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this@SignUpActivity, "Ops, parece que houve um problema, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun changeVisibility() {
        // Alternar visibilidade da senha
        binding.btnTogglePwdVisibility.setOnClickListener {
            if (isPasswordVisible) {
                binding.edtPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnTogglePwdVisibility.setImageResource(R.drawable.ic_visibility_off_24)
            } else {
                binding.edtPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnTogglePwdVisibility.setImageResource(R.drawable.ic_visibility_24)
            }
            isPasswordVisible = !isPasswordVisible
            binding.edtPwd.setSelection(binding.edtPwd.text?.length ?: 0)
        }

        // Alternar visibilidade da confirmação de senha
        binding.btnTogglePwdConfirmationVisibility.setOnClickListener {
            if (isConfirmPasswordVisible) {
                binding.edtPwdConfirmation.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnTogglePwdConfirmationVisibility.setImageResource(R.drawable.ic_visibility_off_24)
            } else {
                binding.edtPwdConfirmation.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnTogglePwdConfirmationVisibility.setImageResource(R.drawable.ic_visibility_24)
            }
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            binding.edtPwdConfirmation.setSelection(binding.edtPwdConfirmation.text?.length ?: 0)
        }
    }
}