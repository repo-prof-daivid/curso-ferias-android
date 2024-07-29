package com.example.listadeanimes.Ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadeanimes.R
import com.example.listadeanimes.Ui.Home.ListaActivity
import com.example.listadeanimes.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pwdVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        changeVisibility()
    }

    private fun setupView() {
        binding.btnlogin.setOnClickListener {
            val user = binding.UserEmail.text
            val pwd = binding.pwd.text
            val auth = FirebaseAuth.getInstance()
            if (user.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this@MainActivity, "Verifique se o Email e a senha estão corretos", Toast.LENGTH_SHORT).show()
            }else if(!Patterns.EMAIL_ADDRESS.matcher(user.toString()).matches()) {
                Toast.makeText(this@MainActivity, "Por favor, insira um endereço de email válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
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
    private fun changeVisibility() {

        binding.btnvisible.setOnClickListener {
            if (pwdVisible) {
                binding.pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnvisible.setImageResource(R.drawable.ic_visibility_off_24)
            } else {

                binding.pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnvisible.setImageResource(R.drawable.ic_visibility_24)
            }
            pwdVisible  = !pwdVisible
            binding.pwd.setSelection(binding.pwd.text.length)
        }
    }
}
