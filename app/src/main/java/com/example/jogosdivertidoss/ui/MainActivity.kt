package com.example.jogosdivertidoss.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jogosdivertidoss.databinding.ActivityMainBinding
import com.example.jogosdivertidoss.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Classe Responsável por manipular os elementos que ficarão visíveis na tela principal.
 * Nela, você pode fazer as configurações iniciais.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        //TODO VALIDAR E-MAIL, CASO EMAIL INVÁLIDA NÃO SEGUIR COM A REQUEST E SOLICITAR QUE O USUÁRIO VERIFIQUE AS INFORMAÇÕES.
        binding.Login.setOnClickListener {
            val user = binding.usuario.text
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

        binding.cadastro.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


    }

}