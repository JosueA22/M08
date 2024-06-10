package com.example.bloonstd6_api.adapter

import android.content.Intent
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bloonstd6_api.PokemonDetailActivity
import com.example.bloonstd6_api.network.PokemonResult
import com.example.bloonstd6_api.R

class PokemonAdapter(private val pokemonList: List<PokemonResult>, private val onItemClick: (PokemonResult) -> Unit) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokemonName.text = pokemon.name.capitalize()
        Glide.with(holder.itemView.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${position + 1}.png")
            .into(holder.pokemonImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PokemonDetailActivity::class.java)
            // Pasar el ID del Pokémon al detalle
            intent.putExtra("POKEMON_ID", position + 1)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pokemonList.size

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonName: TextView = itemView.findViewById(R.id.pokemon_name)
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemon_image)
    }
}