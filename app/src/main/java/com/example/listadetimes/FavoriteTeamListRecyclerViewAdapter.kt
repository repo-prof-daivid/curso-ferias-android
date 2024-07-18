package com.example.listadetimes

import com.example.listadetimes.databinding.TeamListBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FavoriteTeamListRecyclerViewAdapter(
    private val teamsList: List<FavoriteTeamList>,
    private val onDelete: (Int) -> Unit
): RecyclerView.Adapter<FavoriteTeamListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTeamListViewHolder {
        val binding = TeamListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteTeamListViewHolder(binding)
    }

    override fun getItemCount(): Int = teamsList.size


    override fun onBindViewHolder(holder: FavoriteTeamListViewHolder, position: Int) {
        holder.bind(
            teamsList[position],
            position,
            onDelete
        )
    }


}