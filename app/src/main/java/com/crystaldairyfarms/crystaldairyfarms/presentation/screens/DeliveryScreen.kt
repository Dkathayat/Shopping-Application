package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DeliveryItem(
    val name: String,
    val quantity: Int,
    val icon: String
)
@Composable
fun DeliveryScreen() {

    val items = remember {
        listOf(
            DeliveryItem("Fresh Milk", 2, "🥛"),
            DeliveryItem("Indian Paneer", 1, "🧀"),
            DeliveryItem("Brown Bread", 1, "🍞"),
            DeliveryItem("Strawberries", 1, "🍓")
        )
    }

    Scaffold(
        bottomBar = {
            DeliveryActions()
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Your Order is on the Way 🚚",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            DeliveryInfo()

            Spacer(modifier = Modifier.height(20.dp))

            AddressCard()

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Items",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(items) { item ->
                    DeliveryItemCard(item)
                }
            }
        }
    }
}
@Composable
fun DeliveryInfo() {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "⚡",
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {

            Text(
                text = "Fast Delivery",
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Arriving in under 15 minutes",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}
@Composable
fun AddressCard() {

    Card(
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("📍", fontSize = 24.sp)

            Spacer(modifier = Modifier.width(10.dp))

            Column {

                Text(
                    text = "Delivery Address",
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Hno 120 Dwarka Mor, New Delhi",
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DeliveryItemCard(item: DeliveryItem) {

    Card(
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = item.icon,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.name,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Qty: ${item.quantity}",
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DeliveryActions() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            modifier = Modifier.weight(1f),
            onClick = { }
        ) {
            Text("Track Delivery")
        }

        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = { }
        ) {
            Text("Call Agent")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DeliveryScreen()
}