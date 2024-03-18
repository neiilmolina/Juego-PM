package com.example.proyectopm

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Juego : AppCompatActivity(), OnCartaClickListener {
    private val cartas = mutableListOf<Carta>(
        Carta(1, CartaCategories.c, true),
        Carta(2, CartaCategories.c, true),
        Carta(3, CartaCategories.java, true),
        Carta(4, CartaCategories.java, true),
        Carta(5, CartaCategories.javascript, true),
        Carta(6, CartaCategories.javascript, true),
        Carta(7, CartaCategories.pseint, true),
        Carta(8, CartaCategories.pseint, true),
        Carta(9, CartaCategories.python, true),
        Carta(10, CartaCategories.python, true),
        Carta(11, CartaCategories.kotlin, true),
        Carta(12, CartaCategories.kotlin, true),
    )
    private val cartasParejas = mutableListOf<Carta>()
    private val cartasGiradas = mutableListOf<Carta>()
    private var puntosJ1: Int = 0
    private var puntosJ2: Int = 0
    private var turnoJugador1: Boolean = true

    private var volumen:Boolean = true

    private lateinit var mediaPlayerPareja: MediaPlayer
    private lateinit var mediaPlayerNoPareja: MediaPlayer

    private lateinit var rvCartas: RecyclerView
    private lateinit var cartasAdapter: CartaAdapter
    private lateinit var tvJugador1: TextView
    private lateinit var tvJugador2: TextView
    private lateinit var tvVerTurno:TextView
    private lateinit var cvVolumen: CardView
    private lateinit var imgSonido: ImageView

    //dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)
        mediaPlayerPareja = MediaPlayer.create(this, R.raw.siuuu)
        mediaPlayerNoPareja = MediaPlayer.create(this, R.raw.bruh)
        cartas.shuffle()
        initComponents()
        initListeners()
        initUI()
    }

    private fun initComponents() {
        rvCartas = findViewById(R.id.rvCartas)
        tvJugador1 = findViewById(R.id.tvJugador1)
        tvJugador2 = findViewById(R.id.tvJugador2)
        tvVerTurno = findViewById(R.id.tvVerTurno)
        cvVolumen = findViewById(R.id.cvVolumen)
        imgSonido = findViewById(R.id.imgSonido)

    }

    private fun initUI() {
        cartasAdapter = CartaAdapter(cartas, this)
        rvCartas.layoutManager = GridLayoutManager(this, 3)
        rvCartas.adapter = cartasAdapter
        setTextViewJugdador(tvJugador1, puntosJ1, "J1")
        setTextViewJugdador(tvJugador2, puntosJ2, "J2")
        Handler(Looper.getMainLooper()).postDelayed({
            girarCartas()
        }, 3000)
        setTextTurno()

    }

    private fun initListeners() {
        cvVolumen.setOnClickListener {
            if (volumen) {
                imgSonido.setImageResource(R.drawable.ic_volume_off)
                volumen = false
            } else {
                imgSonido.setImageResource(R.drawable.ic_volume_on)
                volumen = true
            }
        }
    }

    private fun setTextViewJugdador(textview: TextView, puntos: Int, jugador: String) {
        textview.text = "${jugador}: ${puntos}"
    }

    fun girarCartas() {
        cartas.forEach { it.girada = false }
        cartasAdapter.notifyDataSetChanged()
    }

    override fun onCartaClick(position: Int) {
        val cartaSeleccionada = cartas[position]

        if (!cartaSeleccionada.girada && cartasGiradas.size < 2) {
            cartaSeleccionada.girada = true
            cartasGiradas.add(cartaSeleccionada)
            cartasAdapter.notifyItemChanged(position)

            if (cartasGiradas.size == 2) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (cartasGiradas[0].contenido == cartasGiradas[1].contenido) {
                        cartasParejas.add(cartasGiradas[0])
                        cartasParejas.add(cartasGiradas[1])
                        dejarCartasGiradas()
                        sumarPuntos()
                        mostrarDialog()
                        reproducirSonido(mediaPlayerPareja)
                    } else {
                        girarCartasNoPareja()
                        reproducirSonido(mediaPlayerNoPareja)
                    }
                    cartasGiradas.clear()
                    cambiarTurno()
                }, 900)
            }
        }
    }

    private fun reproducirSonido(mediaPlayer: MediaPlayer){
        if (volumen) {
            // Iniciar el sonido si volumen es true
            mediaPlayer.start()
        } else {
            // Detener el sonido si volumen es false
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0) // Reiniciar la reproducción al principio del sonido
            }
        }
    }

    private fun dejarCartasGiradas() {
        for (girada in cartasGiradas) {
            for (carta in cartas) {
                if (girada.id == carta.id) {
                    carta.girada = true
                }
            }
        }
        cartasAdapter.notifyDataSetChanged()
    }

    private fun girarCartasNoPareja() {
        for (carta in cartas) {
            if (cartasGiradas.contains(carta)) {
                // Cartas giradas y no parejas
                carta.girada = false
            }
        }
        cartasAdapter.notifyDataSetChanged()

    }

    private fun cambiarTurno() {
        turnoJugador1 = !turnoJugador1
        setTextTurno()
    }

    private fun sumarPuntos(){
        if (turnoJugador1) {
            puntosJ1 += 1
        } else {
            puntosJ2 += 1
        }
        setTextViewJugdador(tvJugador1, puntosJ1, "J1")
        setTextViewJugdador(tvJugador2, puntosJ2, "J2")

    }

    private fun mostrarDialog(){
        val ganador: String
        if(cartasParejas.size == cartas.size){
            if(puntosJ1 < puntosJ2){
                ganador = "GANADOR J2"
            } else if (puntosJ1 > puntosJ2){
                ganador = "GANADOR J1"
            } else{
                ganador = "EMPATE"
            }
            showDialog(ganador)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Liberar recursos de los MediaPlayers
        mediaPlayerPareja.release()
        mediaPlayerNoPareja.release()
    }

    fun setTextTurno(){
        if(turnoJugador1){
            tvVerTurno.text = "Turno de J1"
        } else{
            tvVerTurno.text = "Turno de J2"
        }
    }

    private fun showDialog(resultado:String) {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_final)

        val tvResultado:TextView = dialog.findViewById(R.id.tvResultado)
        val btnVolverInicio: Button = dialog.findViewById(R.id.btnVolverInicio)

        tvResultado.text = resultado

        btnVolverInicio.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            dialog.hide()
        }

        dialog.show()
    }
}


