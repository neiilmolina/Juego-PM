package com.example.proyectopm

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CartaViewHolder(view: View, private val onCartaClickListener: OnCartaClickListener) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    val imgCarta: ImageView = view.findViewById(R.id.imgCarta)
    private var isFlipped = false

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            // Invocar la interfaz de escucha solo si la posición es válida
            onCartaClickListener.onCartaClick(position)
        }
    }

    fun flipCard() {
        val animation: Animation = if (isFlipped) {
            AnimationUtils.loadAnimation(itemView.context, R.anim.rotate_down)
        } else {
            AnimationUtils.loadAnimation(itemView.context, R.anim.rotate_up)
        }
        imgCarta.startAnimation(animation)
        isFlipped = !isFlipped
    }
}

