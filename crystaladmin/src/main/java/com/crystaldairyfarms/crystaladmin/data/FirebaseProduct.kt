package com.crystaldairyfarms.crystaladmin.data

// Firebase RTDB requires a no-arg constructor — default values provide it.
data class FirebaseProduct(
    val id: String = "",
    val name: String = "",
    val shop: String = "",
    val weight: String = "",
    val price: Double = 0.0,
    val emoji: String = "",
    val category: String = "General"
)