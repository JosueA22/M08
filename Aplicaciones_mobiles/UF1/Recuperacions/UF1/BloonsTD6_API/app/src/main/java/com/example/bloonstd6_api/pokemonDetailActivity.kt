package com.example.bloonstd6_api

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bloonstd6_api.network.PokeApiService
import com.example.bloonstd6_api.network.Pokemon
import com.example.bloonstd6_api.network.PokemonSpecies
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_detail_activity)

        val pokemonId = intent.getIntExtra("POKEMON_ID", 0)
        val pokemonNameTextView: TextView = findViewById(R.id.pokemon_name_detail)
        val pokemonImageView: ImageView = findViewById(R.id.pokemon_image_detail)
        val pokemonDescriptionTextView: TextView = findViewById(R.id.pokemon_description_detail)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PokeApiService::class.java)

        service.getPokemon(pokemonId).enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    if (pokemon != null) {
                        pokemonNameTextView.text = pokemon.name.capitalize()
                        Glide.with(this@PokemonDetailActivity)
                            .load(pokemon.sprites.front_default)
                            .into(pokemonImageView)
                    }
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                // Manejo de errores
            }
        })

        service.getPokemonSpecies(pokemonId).enqueue(object : Callback<PokemonSpecies> {
            override fun onResponse(call: Call<PokemonSpecies>, response: Response<PokemonSpecies>) {
                if (response.isSuccessful) {
                    val pokemonSpecies = response.body()
                    if (pokemonSpecies != null) {
                        val flavorText = pokemonSpecies.flavor_text_entries.find { it.language.name == "en" }
                        pokemonDescriptionTextView.text = flavorText?.flavor_text?.replace("\n", " ")
                    }
                }
            }

            override fun onFailure(call: Call<PokemonSpecies>, t: Throwable) {
                pokemonDescriptionTextView.text = getString(R.string.error_loading_description)
            }
        })
    }
}