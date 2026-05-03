package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.Category
import com.crystaldairyfarms.crystaldairyfarms.data.DeliverySlot
import com.crystaldairyfarms.crystaldairyfarms.data.Product
import com.crystaldairyfarms.crystaldairyfarms.data.categories
import com.crystaldairyfarms.crystaldairyfarms.data.deliverySlots
import com.crystaldairyfarms.crystaldairyfarms.presentation.uicomp.CartIconButton
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.BackgroundCream
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.CardWhite
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.DividerColor
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextMuted
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextPrimary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextSecondary

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    onStoreClick: () -> Unit,
    onDrawerClicked: () -> Unit,
    cat: () -> Unit,
    onItemClick: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val products = remember {
        mutableStateListOf(
            Product("Beetroot", "Local shop", "500 gm.", "17.29", "🫚"),
            Product("Italian Avocado", "Local shop", "450 gm.", "14.29", "🥑"),
            Product("Deshi Gajor", "Local shop", "1000 gm.", "27.29", "🥕"),
            Product("Fresh Apple", "Local shop", "1 kg", "5.99", "🍎"),
            Product("Banana", "Local shop", "1 dozen", "2.49", "🍌"),
            Product("Mango (Alphonso)", "Local shop", "1 kg", "9.99", "🥭"),
            Product("Orange", "Local shop", "1 kg", "4.29", "🍊"),
            Product("Pineapple", "Local shop", "1 piece", "3.49", "🍍"),
            Product("Strawberry", "Local shop", "250 gm", "6.29", "🍓"),
            Product("Watermelon", "Local shop", "1 piece", "7.49", "🍉"),
            Product("Green Grapes", "Local shop", "500 gm", "4.79", "🍇"),
            Product("Kiwi", "Local shop", "4 pcs", "5.59", "🥝"),

            // Dairy
            Product("Indian Paneer", "Amul Dairy", "200 gm", "3.49", "🧀"),
            Product("Fresh Paneer", "Local Dairy", "250 gm", "4.19", "🧀"),
            Product("Organic Milk", "Local Dairy", "1 liter", "2.19", "🥛"),
            Product("Greek Yogurt", "Local Dairy", "400 gm", "3.99", "🥣")
        )
    }

    Box(
        modifier = Modifier.padding(paddingValues)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(color = Primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HomeTopBar(uiState.totalItems, { onDrawerClicked.invoke() })
                LocationBar()
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Primary,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 50.dp,
                            bottomEnd = 50.dp
                        )
                    )
            ) {
                CategoryRow(categories) {
                    onStoreClick()
                }
            }

            // ── Delivery Slots ──
            Spacer(Modifier.height(20.dp))
            DeliverySlotRow(deliverySlots) {
                onItemClick.invoke()
            }
            // ── You Might Need ──
            Spacer(Modifier.height(20.dp))
            SectionHeader("You might need", "See more", {
                cat.invoke()
            })

            Spacer(Modifier.height(12.dp))
            ProductRow(products) {
                onItemClick.invoke()
            }

            Spacer(Modifier.height(20.dp))

            // ── Featured ──
            FeaturedHeader(onStoreClick)

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun HomeTopBar(
    cartItem: Int,
    onDrawerClicked: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "More Options",
            tint = Color.White,
            modifier = Modifier
                .padding(5.dp)
                .clickable { onDrawerClicked.invoke() })
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    "Search for Grocery", color = TextMuted,
                    fontSize = 14.sp
                )
            },
            // Use CircleShape for pill-shaped ends
            shape = CircleShape,
            modifier = Modifier
                .padding(10.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedTextColor = Primary,
                cursorColor = Primary
            )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            CartIconButton(cartItem, { })
        }
    }
}

@Composable
fun LocationBar() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Current Location",
            color = TextMuted,
            fontSize = 12.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "New Delhi, IND",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(10.dp))
        }
    }
}

@Composable
fun CategoryRow(categories: List<Category>, onStoreClick: () -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { cat ->
            CategoryItem(cat) {
                onStoreClick()
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, onStoreClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFF3CD), Color(0xFFFFE08A))
                    )
                )
                .border(2.dp, Color(0xFFFFD54F), CircleShape)
                .clickable { onStoreClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(category.emoji, fontSize = 28.sp)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            category.name,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(10.dp))
    }
}

// ─── Section Header ───────────────────────────────────────────────────────────

@Composable
fun SectionHeader(title: String, action: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            modifier = Modifier
                .clickable { onClick.invoke() },
            text = action,
            fontSize = 13.sp,
            color = Primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─── Product Row ──────────────────────────────────────────────────────────────

@Composable
fun ProductRow(products: MutableList<Product>, onStoreClick: () -> Unit) {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products.size) { index ->
            ProductCard(
                product = products[index],
                onIncrement = {

                    products[index] = products[index].copy(quantity = products[index].quantity + 1)
                },
                onDecrement = {
                    if (products[index].quantity > 0) products[index] =
                        products[index].copy(quantity = products[index].quantity - 1)
                }, {
                    onStoreClick.invoke()
                }
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(250.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onItemClick.invoke() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(BackgroundCream),
                contentAlignment = Alignment.Center
            ) {
                Text(product.emoji, fontSize = 38.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                product.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                product.weight,
                fontSize = 11.sp,
                color = TextMuted
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    product.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    "$",
                    fontSize = 10.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            if (product.quantity == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Primary)
                        .clickable { onIncrement() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuantityButton(
                        Icons.Default.Remove,
                        onClick = onDecrement,
                        bg = Color(0xFFE8F5E9)
                    )
                    Text(
                        product.quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    QuantityButton(
                        Icons.Default.Add,
                        onClick = onIncrement,
                        bg = Primary,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun QuantityButton(
    icon: ImageVector,
    onClick: () -> Unit,
    bg: Color = Primary,
    tint: Color = Primary
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(bg)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
    }
}

// ─── Delivery Slot Row ────────────────────────────────────────────────────────

@Composable
fun DeliverySlotRow(slots: List<DeliverySlot>, onStoreClick: () -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(slots) { index ->
            DeliverySlotCard(index) { onStoreClick.invoke() }
        }
    }
}

@Composable
fun DeliverySlotCard(
    slot: DeliverySlot,
    modifier: Modifier = Modifier,
    onStoreClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .width(240.dp)
            .clickable { onStoreClick.invoke() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = slot.color),
        border = BorderStroke(1.dp, DividerColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 14.dp, top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    slot.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    slot.time,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Free delivery",
                    fontSize = 10.sp,
                    color = Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                slot.emoji,
                fontSize = 40.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 6.dp)
            )
        }
    }
}

// ─── Featured Header ──────────────────────────────────────────────────────────

@Composable
fun FeaturedHeader(onStoreClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Featured Stores",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
    FeaturedStore()
}

@Composable
fun FeaturedStore() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(com.crystaldairyfarms.crystaldairyfarms.R.drawable.food_store_organic),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(250.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    )
                    Text(
                        modifier = Modifier.padding(all = 5.dp),
                        text = "T&T Food Market",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                        text = "Deliver in 5 min ⚡ ",
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(com.crystaldairyfarms.crystaldairyfarms.R.drawable.food_store),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(250.dp)
                            .height(150.dp).clip(RoundedCornerShape(10.dp))
                    )
                    Text(
                        modifier = Modifier.padding(all = 5.dp),
                        text = "D&D Food Market",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                        text = "Deliver in 15 min ⚡ ",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HomeContent(PaddingValues(top = 56.dp, bottom = 80.dp) , {}, {}, {}, {}
    )
}