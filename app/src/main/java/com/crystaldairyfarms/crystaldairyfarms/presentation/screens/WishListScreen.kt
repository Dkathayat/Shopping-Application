package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem
import com.crystaldairyfarms.crystaldairyfarms.data.FirebaseProduct
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary

data class WishlistItem(
    val id: Int,
    val name: String,
    val price: Double,
    val icon: String
)

@Composable
fun WishListScreen(
    onProductClick: (FirebaseProduct) -> Unit = {},
    onCheckout: () -> Unit = {},
    bottomPadding: Dp = 0.dp,
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val cartState by cartViewModel.uiState.collectAsState()

    val items = remember {
        mutableStateListOf(
            WishlistItem(1, "Fresh Milk", 2.49, "🥛"),
            WishlistItem(2, "Indian Paneer", 4.19, "🧀"),
            WishlistItem(3, "Brown Bread", 1.99, "🍞"),
            WishlistItem(4, "Strawberries", 3.59, "🍓")
        )
    }

    Scaffold(
        modifier = Modifier.padding(bottom = bottomPadding),
        containerColor = Color.White,
        bottomBar = {
            CheckoutBar(
                total = if (cartState.isEmpty) items.sumOf { it.price } else cartState.totalPrice,
                onCheckout = onCheckout
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Cart summary (shown only when cart has items) ──────────────────
            if (cartState.items.isNotEmpty()) {
                item {
                    CartSummaryCard(
                        cartItems = cartState.items,
                        onAddOne = { cartViewModel.addItem(it) },
                        onRemoveOne = { cartViewModel.removeItem(it.id) },
                        onDelete = { cartViewModel.deleteItem(it.id) }
                    )
                }
            }

            item {
                Text(
                    text = "My Wishlist",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }

            items(items, key = { it.id }) { item ->

                WishlistCard(
                    item = item,
                    onRemove = { items.remove(item) },
                    onTap = {
                        onProductClick(
                            FirebaseProduct(
                                id = item.id.toString(),
                                name = item.name,
                                price = item.price,
                                emoji = item.icon
                            )
                        )
                    },
                    onAddToCart = {
                        cartViewModel.addItem(
                            CartItem(
                                id = item.id.toString(),
                                name = item.name,
                                price = item.price,
                                emoji = item.icon,
                                shop = "",
                                weight = ""
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun WishlistCard(
    item: WishlistItem,
    onRemove: () -> Unit,
    onTap: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .clickable { onTap() },
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

                TextButton(
                    onClick = onAddToCart,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Add to Cart",
                        color = Primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
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
fun CheckoutBar(
    total: Double,
    onCheckout: () -> Unit = {}
) {

    Surface(
        shadowElevation = 8.dp
    ) {

        Row(
            modifier = Modifier
                .background(color = Color.White)
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
                    containerColor = Primary,
                    contentColor = Color.White
                ),
                onClick = onCheckout,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Checkout")
            }
        }
    }
}

@Composable
fun CartSummaryCard(
    cartItems: List<CartItem>,
    onAddOne: (CartItem) -> Unit,
    onRemoveOne: (CartItem) -> Unit,
    onDelete: (CartItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FFF4)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Cart",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Primary
                )
                Text(
                    text = "${cartItems.sumOf { it.quantity }} items",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(10.dp))

            cartItems.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(
                        color = Color(0xFFDDEEDD),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.emoji, fontSize = 24.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "$${"%.2f".format(item.price)} each",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    // Quantity controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Primary.copy(alpha = 0.1f))
                                .clickable {
                                    if (item.quantity > 1) onRemoveOne(item)
                                    else onDelete(item)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.quantity > 1) Icons.Default.Remove else Icons.Default.Delete,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "${item.quantity}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.width(20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Primary)
                                .clickable { onAddOne(item) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "$${"%.2f".format(item.price * item.quantity)}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Primary
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFDDEEDD), modifier = Modifier.padding(top = 10.dp, bottom = 6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Cart Total", fontSize = 13.sp, color = Color.Gray)
                Text(
                    "$${"%.2f".format(cartItems.sumOf { it.price * it.quantity })}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    WishListScreen()
}