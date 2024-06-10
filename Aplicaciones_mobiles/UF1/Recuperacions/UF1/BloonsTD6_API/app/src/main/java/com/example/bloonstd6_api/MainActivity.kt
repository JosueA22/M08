package com.example.bloonstd6_api


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import retrofit2.*
import com.example.bloonstd6_api.adapter.PokemonAdapter
import com.example.bloonstd6_api.network.PokeApiService
import com.example.bloonstd6_api.network.PokemonResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PokeApiService::class.java)

        service.getPokemon().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val pokemonList = response.body()?.results?.take(15) ?: emptyList()
                    adapter = PokemonAdapter(pokemonList) { pokemonResult ->
                        // añadir accion para obtener informacion del pokemon
                    }
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                // Manejo de errores
            }
        })
    }
}