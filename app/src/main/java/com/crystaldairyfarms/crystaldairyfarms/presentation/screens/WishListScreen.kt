package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary

data class WishlistItem(
    val id: Int,
    val name: String,
    val price: Double,
    val icon: String
)
@Composable
fun WishListScreen() {

    val items = remember {
        mutableStateListOf(
            WishlistItem(1, "Fresh Milk", 2.49, "🥛"),
            WishlistItem(2, "Indian Paneer", 4.19, "🧀"),
            WishlistItem(3, "Brown Bread", 1.99, "🍞"),
            WishlistItem(4, "Strawberries", 3.59, "🍓")
        )
    }

    Scaffold(
        bottomBar = {
            CheckoutBar(total = items.sumOf { it.price })
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "My Wishlist",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            items(items, key = { it.id }) { item ->

                WishlistCard(
                    item = item,
                    onRemove = {
                        items.remove(item)
                    }
                )
            }
        }
    }
}
@Composable
fun WishlistCard(
    item: WishlistItem,
    onRemove: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = item.icon,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Text(
                    text = "$${item.price}",
                    color = Color.Gray
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove"
                )
            }
        }
    }
}
@Composable
fun CheckoutBar(total: Double) {

    Surface(
        shadowElevation = 8.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text("Total", fontSize = 14.sp, color = Color.Gray)
                Text(
                    "$${"%.2f".format(total)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary, // Background color
                    contentColor = Color.White    // Text/Icon color
                ),
                onClick = { },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Checkout")
            }
        }
    }
}
@Preview
@Composable
private fun Preview(){
    WishListScreen()
}