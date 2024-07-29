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
                        this@ListaActivity,
                        "Até o momento você ainda não tem Animes Adiconados na lista",
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
                    "Infelizmente não foi possível carregar a lista! + ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

    }


    private fun setUpView() {

        binding.btnlista.setOnClickListener {
            val episodio = binding.epdAnime.text.toString()
            val titulo = binding.TituloAnime.toString()
            val ano= binding.lcdAnime.text.toString()
            val genero = binding.Genero.text.toString()


            if (episodio.isEmpty() || titulo.isEmpty() || ano.isEmpty() || genero.isEmpty()) {

                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novoAnimeChecado = AnimeLista(
                id = UUID.randomUUID().toString(),
                releaseyear  = binding.lcdAnime.text.toString(),
                tittle = binding.TituloAnime.text.toString(),
                episodes = binding.epdAnime.text.toString(),
                gender = binding.Genero.text.toString(),


                )


            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            if (auth.currentUser != null) {
                firestore.collection("USERS")
                    .document(auth.currentUser!!.uid) //
                    .collection("XPTO") //
                    .document(novoAnimeChecado.id) //
                    .set(novoAnimeChecado) //
                    .addOnSuccessListener {
                        Toast.makeText(this, "Anime Adicionado com sucesso!", Toast.LENGTH_SHORT).show()

                        binding.lcdAnime.text.clear()
                        binding.TituloAnime.text.clear()
                        binding.epdAnime.text.clear()
                        binding.Genero.text.clear()


                        animeList.add(novoAnimeChecado)
                        animeListAdapter.notifyItemInserted( animeList.size - 1)
                    }.addOnFailureListener { e ->
                        Log.e("FirestoreError", "Erro ao adicionar Anime", e)
                        Toast.makeText(this, "Erro ao adicionar Anime", Toast.LENGTH_SHORT).show()
                    }

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

