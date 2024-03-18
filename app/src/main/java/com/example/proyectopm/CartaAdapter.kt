package com.example.proyectopm

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CartaAdapter(
    private val cartas: List<Carta>,
    private val onCartaClickListener: OnCartaClickListener  // Cambio aquí
) : RecyclerView.Adapter<CartaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carta, parent, false)
        return CartaViewHolder(view, onCartaClickListener)
    }

    override fun getItemCount() = cartas.size

    override fun onBindViewHolder(holder: CartaViewHolder, position: Int) {
        val contenido = cartas[position].contenido
        // Establecer la imagen de la carta según el estado
        if (cartas[position].girada) {
            when (contenido){
                CartaCategories.c -> holder.imgCarta.setImageResource(R.drawable.c)
                CartaCategories.java -> holder.imgCarta.setImageResource(R.drawable.java)
                CartaCategories.javascript -> holder.imgCarta.setImageResource(R.drawable.javascript)
                CartaCategories.kotlin -> holder.imgCarta.setImageResource(R.drawable.kotlin)
                CartaCategories.pseint -> holder.imgCarta.setImageResource(R.drawable.pseint)
                CartaCategories.python -> holder.imgCarta.setImageResource(R.drawable.python)
            }
        } else {
            holder.imgCarta.setImageResource(R.drawable.carta)
        }

        holder.flipCard()

        // Configurar el clic en el ViewHolder
        holder.itemView.setOnClickListener {
            // Cambio aquí: pasar la posición al llamar a la función onCartaClick
            onCartaClickListener.onCartaClick(position)
        }
    }
}