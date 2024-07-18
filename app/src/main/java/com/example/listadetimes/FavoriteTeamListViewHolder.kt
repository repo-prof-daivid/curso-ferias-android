package com.example.listadetimes

import com.example.listadetimes.databinding.TeamListBinding
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FavoriteTeamListViewHolder(
    private val teamListBinding: TeamListBinding
): RecyclerView.ViewHolder(teamListBinding.root)
{
    fun bind(favoriteTeamList: FavoriteTeamList,
             position: Int,
             onDelete: (position: Int) -> Unit){

        teamListBinding.txtNameTeam.text = favoriteTeamList.nameTeam
        teamListBinding.txtCity.text = favoriteTeamList.city
        teamListBinding.txtMascot.text = favoriteTeamList.mascot
        teamListBinding.txtStadiumAudience.text = favoriteTeamList.stadiumAudience
        teamListBinding.txtFoundationYear.text = favoriteTeamList.foundationYear
        teamListBinding.checkFavorite.isChecked = favoriteTeamList.favoriteTeam

        teamListBinding.btnDelete.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("USERS").document(auth.uid.toString()).collection("XPTO")
                .document(favoriteTeamList.id).delete()
                .addOnSuccessListener {
                    onDelete.invoke(position)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        teamListBinding.root.context,
                        "Não foi possível deletar esse item!",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }


        teamListBinding.checkFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
            if(buttonView.isShown){
                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                favoriteTeamList.favoriteTeam = isChecked
                firestore.collection("USERS").document(auth.uid.toString()).collection("XPTO")
                    .document(favoriteTeamList.id).set(favoriteTeamList)
                    .addOnSuccessListener {
                        Toast.makeText(
                            teamListBinding.root.context,
                            "Item atualizado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener {
                        favoriteTeamList.favoriteTeam = false
                    }
            }
        }
    }

    }


