package com.example.listadecompras

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecompras.databinding.ActivityMainBinding
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
        binding.btnLogin.setOnClickListener {
            val user = binding.userName.text
            val pwd = binding.pwd.text
            val auth = FirebaseAuth.getInstance()
            if (user.isNullOrEmpty() || pwd.isNullOrEmpty()) {
                Toast.makeText(this@MainActivity, "Verifique se o e-mail e a senha estão corretos!", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(user.toString(), pwd.toString())
                    .addOnSuccessListener {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@MainActivity, "Verifique se o e-mail e a senha estão corretos!", Toast.LENGTH_LONG).show()
                    }
            }
        }

        binding.signup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


    }

}