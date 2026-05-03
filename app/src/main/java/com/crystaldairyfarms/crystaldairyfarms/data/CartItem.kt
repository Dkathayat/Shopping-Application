package com.crystaldairyfarms.crystaldairyfarms.data

data class CartItem(
    val id: String,
    val name: String,
    val shop: String,
    val weight: String,
    val price: Double,
    val emoji: String,
    val quantity: Int = 0
)
