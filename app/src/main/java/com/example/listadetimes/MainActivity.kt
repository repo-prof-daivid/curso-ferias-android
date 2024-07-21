package com.example.listadetimes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadetimes.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import android.text.InputType

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

    /**
     * Aqui é uma função é quando o botão de login for clicado, ele verifica se os campos userName e pwd estão preenchidos e tentar fazer login.
     * As Mensagens de Toast Informam ao usuário se os campos estão vazios ou se houve erro no login.
     * É no final vai para HomeActivity se o login for bem-sucedido, senão exibe mensagem de erro.
     */
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

        /**
         * Basicamento o código abaixo vai fazer a navegação entre as telas quando o botão de
         * inscriçao for clicado.
         */

        binding.signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }





    }

    private fun changeVisibility() {
        // Lógica para alternar a visibilidade da senha
        binding.btnTogglePwdVisibility.setOnClickListener {
            if (isPasswordVisible) {
                // Ocultar senha
                binding.pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnTogglePwdVisibility.setImageResource(R.drawable.ic_visibility_off_24)
            } else {
                // Mostrar senha
                binding.pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnTogglePwdVisibility.setImageResource(R.drawable.ic_visibility_24)
            }
            isPasswordVisible = !isPasswordVisible
            // Mover o cursor para o final do texto
            binding.pwd.setSelection(binding.pwd.text.length)
        }
    }

}