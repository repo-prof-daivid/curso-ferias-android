package com.example.jogosdivertidoss.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jogosdivertidoss.R
import com.example.jogosdivertidoss.databinding.ActivityMainBinding
import com.example.jogosdivertidoss.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Classe Responsável por manipular os elementos que ficarão visíveis na tela principal.
 * Nela, você pode fazer as configurações iniciais.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        changeVisibility()
    }

    private fun setUpView() {
        //TODO VALIDAR E-MAIL, CASO EMAIL INVÁLIDA NÃO SEGUIR COM A REQUEST E SOLICITAR QUE O USUÁRIO VERIFIQUE AS INFORMAÇÕES.
        binding.login.setOnClickListener {
            val user = binding.nome.text
            val pwd = binding.senha.text
            val auth = FirebaseAuth.getInstance()
            if (user.isNullOrEmpty() || pwd.isNullOrEmpty()) {
                Toast.makeText(this@MainActivity, "Email ou Senha incorretos!", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(user.toString(), pwd.toString())
                    .addOnSuccessListener {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@MainActivity, "Email ou senha incorretos!", Toast.LENGTH_LONG).show()
                    }
            }
        }
        binding.signup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changeVisibility() {
        // Lógica para alternar a visibilidade da senha
        binding.btnTogglePwdVisibility.setOnClickListener {
            if (isPasswordVisible) {
                // Ocultar senha
                binding.senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnTogglePwdVisibility.setImageResource(R.drawable.baseline_visibility_off_24)
            } else {
                // Mostrar senha
                binding.senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnTogglePwdVisibility.setImageResource(R.drawable.baseline_visibility_24)
            }
            isPasswordVisible = !isPasswordVisible
            // Mover o cursor para o final do texto
            binding.senha.setSelection(binding.senha.text.length)
        }
    }

}