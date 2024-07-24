package com.example.jogosdivertidoss.ui.home

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jogosdivertidoss.R
import com.example.jogosdivertidoss.databinding.GamesListBinding
import com.example.jogosdivertidoss.model.GamesListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GamesListItemViewHolder(
    private val itemListBinding: GamesListBinding
) : RecyclerView.ViewHolder(itemListBinding.root) {

    fun bind(
        GamesListItem: GamesListItem,
        position: Int,
        onDelete: (position: Int) -> Unit
    ) {
        itemListBinding.txtgamename.text = GamesListItem.name
        itemListBinding.txtgametype.text = GamesListItem.type
        itemListBinding.txtdurationtime.text = GamesListItem.durationtime
        itemListBinding.cbfinished.isChecked = GamesListItem.finished

        itemListBinding.btnDelete.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("USERS").document(auth.uid.toString()).collection("PURCHASE_LIST")
                .document(GamesListItem.id).delete()
                .addOnSuccessListener {
                    onDelete.invoke(position)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        itemListBinding.root.context,
                        "Não foi possível deletar esse item!",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        itemListBinding.cbfinished.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isShown) {
                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                GamesListItem.finished = isChecked
                firestore.collection("USERS").document(auth.uid.toString()).collection("PURCHASE_LIST")
                    .document(GamesListItem.id).set(GamesListItem)
                    .addOnSuccessListener {
                        Toast.makeText(
                            itemListBinding.root.context,
                            "Item atualizado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener { GamesListItem.finished = false }
            }
        }
    }

}