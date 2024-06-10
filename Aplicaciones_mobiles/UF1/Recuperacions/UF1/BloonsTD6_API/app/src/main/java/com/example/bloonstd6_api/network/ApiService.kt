package com.example.bloonstd6_api.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon?limit=15")
    fun getPokemon(): Call<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<Pokemon>

    @GET("pokemon-species/{id}")
    fun getPokemonSpecies(@Path("id") id: Int): Call<PokemonSpecies>
}