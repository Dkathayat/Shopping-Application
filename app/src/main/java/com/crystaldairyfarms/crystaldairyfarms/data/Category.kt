package com.crystaldairyfarms.crystaldairyfarms.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Browen
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.MilkWhite
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.PinkCard
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.PrimaryLight


data class Category(val name: String, val emoji: String)
data class Product(
    val name: String,
    val shop: String,
    val weight: String,
    val price: String,
    val emoji: String,
    var quantity: Int = 0
)

data class DeliverySlot(val label: String, val time: String, val color: Color, val emoji: String)


val categories = listOf(
    Category("Paneer", "🧀"),
    Category("Vegi", "🥦"),
    Category("Fruits", "🍊"),
    Category("Breads", "🍞")
)



val deliverySlots = listOf(
    DeliverySlot("Bread", "By 12:15pm", Browen, "🍞"),
    DeliverySlot("Dairy", "By 12:15pm", MilkWhite, "🥛"),
    DeliverySlot("Vegetable", "By 1:30pm", PrimaryLight, "🥕"),
    DeliverySlot("Wholesale", "By 1:30pm", PinkCard, "🧅")
)