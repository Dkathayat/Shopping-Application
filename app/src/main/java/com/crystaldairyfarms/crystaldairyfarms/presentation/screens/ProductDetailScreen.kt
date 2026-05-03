package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Colors ────────────────────────────────────────────────────────────────────
private val DarkGreen        = Color(0xFF1B3F32)
private val LightGreen       = Color(0xFFB5E0C8)
private val AddToCartGreen   = Color(0xFF7DC67E)
private val PageBg           = Color(0xFFF8F8F8)
private val CardWhite        = Color(0xFFFFFFFF)
private val TextMain         = Color(0xFF1A1A1A)
private val TextSub          = Color(0xFF9CA3AF)
private val TextGreen        = Color(0xFF2D7A4F)
private val OrangeBadge      = Color(0xFFF97316)
private val StarYellow       = Color(0xFFF59E0B)
private val DeliveryGreen    = Color(0xFF4CAF50)
private val VariantColors    = listOf(
    Color(0xFF8B2B2B), // dark red
    Color(0xFFD97706), // amber
    Color(0xFF1E40AF)  // blue
)

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun ProductDetailScreen(onBack: () -> Unit = {}) {
    var quantity by remember { mutableStateOf(1) }
    var isFavourite by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = PageBg,
        topBar = { ProductTopBar(onBack) },
        bottomBar = { ProductBottomBar(quantity, onDecrement = { if (quantity > 1) quantity-- }, onIncrement = { quantity++ }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero image card ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(CardWhite),
                contentAlignment = Alignment.Center
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                )

                // Product image placeholder (replace with AsyncImage / painterResource)
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🥛", fontSize = 100.sp)
                }
            }

            // ── Detail card ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(CardWhite)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    // Title + favourite
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Fresh cow Milk\nIn 50 gm",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain,
                                lineHeight = 26.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "1000 gm",
                                fontSize = 13.sp,
                                color = TextSub
                            )
                        }
                        IconButton(onClick = { isFavourite = !isFavourite }) {
                            Icon(
                                imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favourite",
                                tint = if (isFavourite) Color.Red else TextSub,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Price + delivery badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Price
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "23.",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            Text(
                                text = "46",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "₹",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        // Fast delivery badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = DeliveryGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Available on fast delivery",
                                fontSize = 11.sp,
                                color = DeliveryGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Variant dots + rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            VariantColors.forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(2.dp, Color.White, CircleShape)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = StarYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "4.5 Rating",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMain
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)

                    Spacer(Modifier.height(14.dp))

                    // Description
                    val descText = "100% satisfaction guarantee. If you experience any of the following issues, missing, poor item, late arrival, unprofessional servic..."
                    Text(
                        text = buildAnnotatedString {
                            append(descText)
                            append(" ")
                            withStyle(SpanStyle(color = TextGreen, fontWeight = FontWeight.SemiBold)) {
                                append("Read more")
                            }
                        },
                        fontSize = 13.sp,
                        color = TextSub,
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────────────
@Composable
fun ProductTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkGreen)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Back button
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        // Title
        Text(
            text = "Product Details",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // Cart
        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(OrangeBadge)
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
            )
        }
    }
}

// ── Bottom Bar ────────────────────────────────────────────────────────────────
@Composable
fun ProductBottomBar(quantity: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardWhite)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quantity selector
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .border(1.5.dp, Color(0xFFE0E0E0), RoundedCornerShape(50.dp))
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .clickable { onDecrement() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Remove", tint = TextMain, modifier = Modifier.size(16.dp))
            }
            Text(
                text = quantity.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .clickable { onIncrement() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = TextMain, modifier = Modifier.size(16.dp))
            }
        }

        // Add to cart button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(AddToCartGreen)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text(
                    text = "Add to cart",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview(){
    ProductDetailScreen({ })
}