package com.example.gamelistapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelistapp.R
import com.example.gamelistapp.models.Game

class MainGameAdapter(
    private var games: List<Game>,
    private val onItemClick: (Game) -> Unit
) : RecyclerView.Adapter<MainGameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameNameTextView: TextView = view.findViewById(R.id.gameNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.gameNameTextView.text = game.name
        holder.itemView.setOnClickListener { onItemClick(game) }
    }

    override fun getItemCount(): Int = games.size

    fun updateList(newList: List<Game>) {
        games = newList
        notifyDataSetChanged()
    }
}
