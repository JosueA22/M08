package com.example.reproductor_de_musica

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentSongResId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lista de canciones y sus IDs de recursos
        val songs = listOf("Song 1", "Song 2", "Song 3")
        val songResIds = listOf(R.raw.music1, R.raw.music2, R.raw.music3)

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SimpleSongAdapter(songs) { position ->
            playSong(songResIds[position])
        }

        // Configuración de los botones de control
        val playButton: Button = findViewById(R.id.playButton)
        val pauseButton: Button = findViewById(R.id.pauseButton)
        val stopButton: Button = findViewById(R.id.stopButton)

        playButton.setOnClickListener {
            if (::mediaPlayer.isInitialized && !mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        pauseButton.setOnClickListener {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        stopButton.setOnClickListener {
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.stop()
                mediaPlayer.reset()
                currentSongResId?.let {
                    mediaPlayer = MediaPlayer.create(this, it)
                }
            }
        }
    }

    private fun playSong(resId: Int) {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
        currentSongResId = resId
        mediaPlayer = MediaPlayer.create(this, resId)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}

class SimpleSongAdapter(
    private val songs: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<SimpleSongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitle: TextView = itemView.findViewById(android.R.id.text1)
        fun bind(song: String, position: Int) {
            songTitle.text = song
            itemView.setOnClickListener { onItemClick(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position], position)
    }

    override fun getItemCount(): Int = songs.size
}