package com.example.simon

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var simonGameView: SimonGameView
    private lateinit var restartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear el layout principal y añadir la vista del juego
        val mainLayout = FrameLayout(this)
        simonGameView = SimonGameView(this)
        mainLayout.addView(simonGameView)

        // Crear el botón de "Volver a Empezar" y configurarlo
        restartButton = Button(this).apply {
            text = "Volver a Empezar"
            visibility = View.GONE // Inicialmente oculto
            setOnClickListener {
                simonGameView.startGame() // Reinicia el juego
                this.visibility = View.GONE // Oculta el botón después de reiniciar
            }
        }

        // Añadir el botón al layout principal
        val buttonParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // Centrar el botón en el medio de la pantalla
            gravity = android.view.Gravity.CENTER
        }
        mainLayout.addView(restartButton, buttonParams)

        // Establecer el layout principal como la vista de contenido de la actividad
        setContentView(mainLayout)
    }

    // Clase interna para la vista personalizada del juego Simon
    inner class SimonGameView(context: Context) : View(context) {
        private val colors = arrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW)

        private val paint = Paint()
        private val circleRadius = 100f
        private val buttonSpacing = 150f
        private var sequence: MutableList<Int> = mutableListOf()
        private var playerSequence: MutableList<Int> = mutableListOf()
        private var isPlayerTurn = false
        private var isGameOver = false
        private var wins = 0
        private var currentSpeed = 1000L // Tiempo constante (en milisegundos)
        private val buttonDelay = 1000L // Tiempo de espera para el botón seleccionado (en milisegundos)

        private var soundPool: SoundPool
        private var soundId = 0

        private var pressedButtonIndex: Int? = null

        init {
            paint.isAntiAlias = true

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            soundPool = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()

            soundId = soundPool.load(context, R.raw.button_sound, 1)

            startGame()
        }

        fun startGame() {
            sequence.clear()
            playerSequence.clear()
            isPlayerTurn = false
            isGameOver = false
            wins = 0

            // Mostrar el mensaje "Comenzando"
            postDelayed({
                generateNextButton()
                playSequence()
            }, 2000L) // Retraso de 2 segundos antes de comenzar
        }

        private fun generateNextButton() {
            val nextButton = Random.nextInt(4)
            sequence.add(nextButton)
            flashButton(nextButton)
        }

        private fun playSequence() {
            var delay = 0L
            sequence.forEachIndexed { index, _ ->
                postDelayed({
                    invalidate()
                    playSound()
                }, delay)
                delay += currentSpeed
            }
            postDelayed({
                isPlayerTurn = true
                invalidate()
            }, delay)
        }

        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            // Dibujar los círculos
            for (i in colors.indices) {
                val x = (buttonSpacing * (i + 1)) + (circleRadius * 2 * i) + circleRadius
                val y = height / 2f
                paint.color = colors[i]
                canvas.drawCircle(x, y, circleRadius, paint)
            }

            // Dibujar el botón presionado
            pressedButtonIndex?.let { index ->
                paint.color = Color.BLACK
                val x = (buttonSpacing * (index + 1)) + (circleRadius * 2 * index) + circleRadius
                val y = height / 2f
                canvas.drawCircle(x, y, circleRadius, paint)
            }

            // Dibujar el mensaje de victoria o fin del juego
            if (isGameOver) {
                val message = if (wins >= 5) "¡Victoria!" else "¡Juego Terminado!"
                val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.BLACK
                    textSize = 72f
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText(message, width / 2f, height / 2f, textPaint)
            }

            // Dibujar el número de victorias
            val winsText = "Victorias: $wins"
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.BLACK
                textSize = 48f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(winsText, width / 2f, (height - 100).toFloat(), textPaint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (!isPlayerTurn || isGameOver) return true

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y
                    for (i in colors.indices) {
                        val centerX = (buttonSpacing * (i + 1)) + (circleRadius * 2 * i) + circleRadius
                        val centerY = height / 2f
                        val distance = Math.sqrt((x - centerX).toDouble().pow(2) + (y - centerY).toDouble().pow(2))
                        if (distance <= circleRadius) {
                            playSound()
                            playerSequence.add(i)
                            pressedButtonIndex = i
                            invalidate()
                            checkSequence()
                            break
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    pressedButtonIndex = null
                    invalidate()
                }
            }

            return true
        }

        private fun checkSequence() {
            if (playerSequence.size == sequence.size) {
                if (playerSequence == sequence) {
                    wins++
                    if (wins < 5) {
                        generateNextButton()
                        playerSequence.clear()
                        isPlayerTurn = false
                        playSequence()
                    } else {
                        isGameOver = true
                        invalidate()
                        showRestartButton()
                    }
                } else {
                    isGameOver = true
                    invalidate()
                    showRestartButton()
                }
            }
        }

        private fun showRestartButton() {
            // Mostrar el botón de "Volver a Empezar"
            (context as MainActivity).runOnUiThread {
                restartButton.visibility = View.VISIBLE
            }
        }

        private fun playSound() {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }

        private fun flashButton(index: Int) {
            pressedButtonIndex = index
            invalidate()

            // Cambiar el color del botón durante 1 segundo
            Handler().postDelayed({
                pressedButtonIndex = null
                invalidate()
            }, 1000L)
        }
    }
}