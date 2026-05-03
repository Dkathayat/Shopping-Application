package com.crystaldairyfarms.crystaldairyfarms.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shop: String,
    val weight: String,
    val price: Double,
    val emoji: String,
    val quantity: Int
)

fun CartItemEntity.toCartItem() = CartItem(id, name, shop, weight, price, emoji, quantity)
fun CartItem.toEntity() = CartItemEntity(id, name, shop, weight, price, emoji, quantity)