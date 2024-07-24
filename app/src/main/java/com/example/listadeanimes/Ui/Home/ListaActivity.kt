package com.example.listadeanimes.Ui.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.listadeanimes.Model.AnimeLista
import com.example.listadeanimes.Ui.MainActivity
import com.example.listadeanimes.databinding.ActivityListaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaBinding
    private lateinit var animeListAdapter: AnimerecycleViewAdapter
    private val animeList: ArrayList<AnimeLista> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadList() // Certifique-se de chamar o método loadList em algum ponto, como aqui
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
                        this@ListaActivity,
                        "Até o momento você ainda não tem times na sua lista, adicione times a sua lista",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    result.forEach { item ->
                        val itemAnime = item.toObject(AnimeLista::class.java)
                        itemAnime.id = item.id
                        animeList.add(itemAnime)
                        // utilizar o notifyItemRangeChanged
                        animeListAdapter.notifyItemRangeChanged(0, animeList.size)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@ListaActivity,
                    "Infelizmente não foi possível carregar a lista!!! + ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun setUpView() {
        // 1. Validar campos
        binding.btnlista.setOnClickListener {
            val episodio = binding.epdAnime.text.toString()
            val titulo = binding.TituloAnime.toString()
            val ano= binding.lcdAnime.text.toString()
            val genero = binding.Genero.text.toString()


            if (episodio.isEmpty() || titulo.isEmpty() || ano.isEmpty() || genero.isEmpty()) {
                // Exibir mensagem de erro, por exemplo, usando um Toast
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Sai da função se houver campos vazios
            }

            val novoAnimeChecado = AnimeLista(
                id = UUID.randomUUID().toString(),
                releaseyear  = binding.lcdAnime.text.toString(),
                tittle = binding.TituloAnime.text.toString(),
                episodes = binding.epdAnime.text.toString(),
                gender = binding.Genero.text.toString(),


                )

            // 3. Adicionar no Firebase
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            if (auth.currentUser != null) { // Verifica se o usuário está logado
                firestore.collection("USERS")
                    .document(auth.currentUser!!.uid) // Usa o ID do usuário atual
                    .collection("XPTO") // Nome da coleção para times favoritos
                    .document(novoAnimeChecado.id) // Usa o ID do novo time como nome do documento
                    .set(novoAnimeChecado) // Adiciona o novo time
                    .addOnSuccessListener {
                        Toast.makeText(this, "Time adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                        // Limpa os campos após adicionar
                        binding.lcdAnime.text.clear()
                        binding.TituloAnime.text.clear()
                        binding.epdAnime.text.clear()
                        binding.Genero.text.clear()


                        // faz aparecer os itens na tela
                        animeList.add(novoAnimeChecado)
                        animeListAdapter.notifyItemInserted( animeList.size - 1)
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
        animeListAdapter = AnimerecycleViewAdapter(
            items = animeList,
            onDelete = { position ->
                animeList.remove(animeList[position])
                animeListAdapter.notifyItemRemoved(position)
            }
        )

        binding.AnimeList.layoutManager = GridLayoutManager(this, 2)
        binding.AnimeList.adapter =  animeListAdapter


    }

}

