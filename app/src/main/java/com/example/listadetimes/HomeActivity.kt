package com.example.listadetimes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listadetimes.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.util.UUID
import androidx.recyclerview.widget.GridLayoutManager


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var favoriteTeamListAdapter: FavoriteTeamListRecyclerViewAdapter
    private val favoriteListItems: ArrayList<FavoriteTeamList> = arrayListOf()

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
        firestore.collection("USERS").document(auth.uid.toString()).collection("XPTO")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Até o momento você ainda não tem Times na sua lista, começe o cadastro!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    result.forEach { item ->
                        val listTeamItem = item.toObject<FavoriteTeamList>()
                        listTeamItem.id = item.id
                        favoriteListItems.add(listTeamItem)
                        // utilizar o notifyItemRangeChanged
                        favoriteTeamListAdapter.notifyItemRangeChanged(0, favoriteListItems.size)

                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@HomeActivity,
                    "Infelizmente não foi possível carregar a lista!!! + ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

    }




    private fun setUpView() {
        // 1. Validar campos
        binding.btnAddTeam.setOnClickListener {
            val nomeTime = binding.hoNomeTime.text.toString()
            val publicoEstadio = binding.hoQuantidade.text.toString()
            val cidade = binding.hoCidade.text.toString()
            val mascote = binding.hoMascote.text.toString()
            val anoFundacao = binding.hoFundacao.text.toString()

            if (nomeTime.isEmpty() || publicoEstadio.isEmpty() || cidade.isEmpty() || mascote.isEmpty() || anoFundacao.isEmpty()) {
                // Exibir mensagem de erro, por exemplo, usando um Toast
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Sai da função se houver campos vazios
            }

            val novoTimeFavorito = FavoriteTeamList(
                id = UUID.randomUUID().toString(),
                nameTeam = binding.hoNomeTime.text.toString(),
                stadiumAudience = binding.hoQuantidade.text.toString(),
                city = binding.hoCidade.text.toString(),
                mascot = binding.hoMascote.text.toString(),
                foundationYear = binding.hoFundacao.text.toString(),

            )

            // 3. Adicionar no Firebase
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            if (auth.currentUser != null) { // Verifica se o usuário está logado
                firestore.collection("USERS")
                    .document(auth.currentUser!!.uid) // Usa o ID do usuário atual
                    .collection("XPTO") // Nome da coleção para times favoritos
                    .document(novoTimeFavorito.id) // Usa o ID do novo time como nome do documento
                    .set(novoTimeFavorito) // Adiciona o novo time
                    .addOnSuccessListener {
                        Toast.makeText(this, "Time adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                        // Limpa os campos após adicionar
                        binding.hoNomeTime.text.clear()
                        binding.hoQuantidade.text.clear()
                        binding.hoCidade.text.clear()
                        binding.hoMascote.text.clear()
                        binding.hoFundacao.text.clear()

                        // faz aparecer os itens na tela
                        favoriteListItems.add(novoTimeFavorito)
                        favoriteTeamListAdapter.notifyItemInserted(favoriteListItems.size - 1)
                    }.addOnFailureListener { e ->
                        Log.e("FirestoreError", "Erro ao adicionar time", e)
                        Toast.makeText(this, "Erro ao adicionar time", Toast.LENGTH_SHORT).show()
                    }

            }else{
                // Redireciona para a Activity de Login
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Você precisa estar logado para adicionar seus times favoritos.", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }


    private fun setUpRecyclerView() {
        favoriteTeamListAdapter = FavoriteTeamListRecyclerViewAdapter(
            teamsList = favoriteListItems,
            onDelete = { position ->
                favoriteListItems.remove(favoriteListItems[position])
                favoriteTeamListAdapter.notifyItemRemoved(position)
            }
        )

        binding.rvListTimes.layoutManager = GridLayoutManager(this, 2)
        binding.rvListTimes.adapter = favoriteTeamListAdapter


    }


}