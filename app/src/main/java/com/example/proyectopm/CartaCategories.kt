package com.example.proyectopm

sealed class CartaCategories {
    object c : CartaCategories()
    object java : CartaCategories()
    object javascript : CartaCategories()
    object pseint : CartaCategories()
    object python : CartaCategories()
    object kotlin : CartaCategories()
}