package com.example.jogosdivertidoss.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jogosdivertidoss.databinding.GamesListBinding
import com.example.jogosdivertidoss.model.GamesListItem

class PurchaseListItemRecyclerViewAdapter(
    private val items: List<GamesListItem>,
    private val onDelete: (Int) -> Unit
): RecyclerView.Adapter<GamesListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesListItemViewHolder {
        val binding = GamesListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GamesListItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GamesListItemViewHolder, position: Int) {
        holder.bind(
            items[position],
            position,
            onDelete
        )
    }

}