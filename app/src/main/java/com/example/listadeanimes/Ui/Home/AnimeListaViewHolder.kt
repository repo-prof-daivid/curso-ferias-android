package com.example.listadeanimes.Ui.Home

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.listadeanimes.Model.AnimeLista
import com.example.listadeanimes.databinding.AnimeListaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AnimeListaViewHolder (
    private val animeListabinding: AnimeListaBinding
): RecyclerView.ViewHolder(animeListabinding.root) {

    fun bind(animeLista: AnimeLista, position: Int, onDelete: (Int) -> Unit){

        animeListabinding.txtyear.text = animeLista.releaseyear
        animeListabinding.txttittle.text = animeLista.tittle
        animeListabinding.txtept.text = animeLista.episodes
        animeListabinding.txtgender.text = animeLista.gender
        animeListabinding.animeIsChecked.isChecked = animeLista.isChecked


        animeListabinding.btnDeletar.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("USERS").document(auth.uid.toString()).collection("XPTO")
                .document(animeLista.id).delete()
                .addOnSuccessListener {
                    onDelete.invoke(position)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        animeListabinding.root.context,
                        "Não foi possível deletar esse item!",
                        Toast.LENGTH_LONG
                    ).show()

                }

        }

        animeListabinding.animeIsChecked.setOnCheckedChangeListener { buttonView, isChecked ->
            if(buttonView.isShown){
                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                animeLista.isChecked = isChecked
                firestore.collection("USERS").document(auth.uid.toString()).collection("XPTO")
                    .document(animeLista.id).set(animeLista)
                    .addOnSuccessListener {
                        Toast.makeText(
                            animeListabinding.root.context,
                            "Item atualizado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener {
                        animeLista.isChecked = false
                    }
            }
        }
    }



}

