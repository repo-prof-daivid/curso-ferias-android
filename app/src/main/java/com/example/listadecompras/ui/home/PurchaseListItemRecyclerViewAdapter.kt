package com.example.listadecompras.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras.databinding.ItemListBinding
import com.example.listadecompras.model.PurchaseListItem

class PurchaseListItemRecyclerViewAdapter(
    private val items: List<PurchaseListItem>,
    private val onDelete: (Int) -> Unit
): RecyclerView.Adapter<PurchaseListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseListItemViewHolder {
        val binding = ItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PurchaseListItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PurchaseListItemViewHolder, position: Int) {
        holder.bind(
            items[position],
            position,
            onDelete
        )
    }

}