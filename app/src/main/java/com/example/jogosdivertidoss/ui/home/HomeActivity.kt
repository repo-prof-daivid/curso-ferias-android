package com.example.jogosdivertidoss.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jogosdivertidoss.databinding.ActivityHomeBinding
import com.example.jogosdivertidoss.model.GamesListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.util.UUID
import android.util.Log
import android.content.Intent
import com.example.jogosdivertidoss.ui.MainActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var GamesAdapter: PurchaseListItemRecyclerViewAdapter
    private val Gameslist: ArrayList<GamesListItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadList()
        setUpView()
        setUpRecyclerView()
    }

    private fun loadList() {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("USERS").document(auth.uid.toString()).collection("PURCHASE_LIST")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Você ainda não adicionou nenhum jogos.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    result.forEach { item ->
                        val purchaseListItem = item.toObject<GamesListItem>()
                        purchaseListItem.id = item.id
                        Gameslist.add(purchaseListItem)
                        // utilizar o notifyItemRangeChanged
                        GamesAdapter.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@HomeActivity,
                    "Infelizmente não foi possível atualização de jogos + ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

    }

    private fun setUpView() {
        // 1. Validar campos
        binding.btnAdd.setOnClickListener {
            val nomeJogo = binding.nome.text.toString()
            val generoJogo = binding.genero.text.toString()
            val duraçaoJogo = binding.tempoDeDuraO.text.toString()
            val notaJogo = binding.nota.text.toString()

            if (nomeJogo.isEmpty() || generoJogo.isEmpty() || duraçaoJogo.isEmpty() || notaJogo.isEmpty()) {
                Toast.makeText(this, "Preencha os espaços em branco", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novojogodivertido = GamesListItem(
                id = UUID.randomUUID().toString(),
                name = binding.nome.text.toString(),
                type = binding.genero.text.toString(),
                durationtime = binding.tempoDeDuraO.text.toString(),
                grade = binding.nota.text.toString(),

                )

            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            if (auth.currentUser != null) {
                firestore.collection("USERS")
                    .document(auth.currentUser!!.uid)
                    .collection("XPTO")
                    .document(novojogodivertido.id)
                    .set(novojogodivertido)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Time adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                        // Limpa os campos após adicionar
                        binding.nome.text?.clear()
                        binding.genero.text?.clear()
                        binding.tempoDeDuraO.text?.clear()
                        binding.nota.text?.clear()


                        Gameslist.add(novojogodivertido)
                        GamesAdapter.notifyItemInserted(Gameslist.size - 1)
                    }.addOnFailureListener { e ->
                        Log.e("FirestormError", "Ação falhou!", e)
                        Toast.makeText(this, "Ação falhou!", Toast.LENGTH_SHORT).show()
                    }

            }else{
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Conecte-se a uma conta para adicionar jogos.", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }

    private fun setUpRecyclerView() {
        GamesAdapter = PurchaseListItemRecyclerViewAdapter(
            items = Gameslist,
            onDelete = { position ->
                Gameslist.remove(Gameslist[position])
                GamesAdapter.notifyItemRemoved(position)
            }
        )
        binding.listaJogos.layoutManager = GridLayoutManager(this, 2)
        binding.listaJogos.adapter = GamesAdapter
    }
}