package com.example.listadeanimes.Ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listadeanimes.Model.AnimeLista
import com.example.listadeanimes.databinding.AnimeListaBinding

class AnimerecycleViewAdapter(

    private val items: List<AnimeLista>,
    private val onDelete: (Int) -> Unit

): RecyclerView.Adapter<AnimeListaViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeListaViewHolder {
        val binding = AnimeListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeListaViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size



    override fun onBindViewHolder(holder: AnimeListaViewHolder, position: Int) {
        holder.bind(items[position], position , onDelete)
    }

}