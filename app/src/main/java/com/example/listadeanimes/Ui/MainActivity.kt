package com.example.listadeanimes.Ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadeanimes.Ui.Home.ListaActivity
import com.example.listadeanimes.databinding.ActivityMainBinding
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
        setupView()
    }

    private fun setupView() {
        binding.btnlogin.setOnClickListener {
            val user = binding.UserName.text
            val pwd = binding.pwd.text
            val auth = FirebaseAuth.getInstance()
            if (user.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this@MainActivity, "Verifique se o Email e a senha estão corretos", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(user.toString(), pwd.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, ListaActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@MainActivity, "Verifique se o Email e a senha estão corretos", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.signup.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
