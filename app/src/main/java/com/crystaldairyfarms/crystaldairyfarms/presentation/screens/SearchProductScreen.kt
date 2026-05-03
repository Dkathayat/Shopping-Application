package com.crystaldairyfarms.crystaldairyfarms.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import kotlinx.coroutines.delay

// ── Colors ────────────────────────────────────────────────────────────────────
private val PageBg       = Color(0xFFF5F5F5)
private val CardBg       = Color(0xFFFFFFFF)
private val SearchBg     = Color(0xFFFFFFFF)
private val TextPrimary  = Color(0xFF1A1A1A)
private val TextMuted    = Color(0xFF9CA3AF)
private val GreenBtn     = Color(0xFF8DC98D)
private val GreenBtnBg   = Color(0xFFEEF7EE)
private val PriceColor   = Color(0xFF1A1A1A)
private val FilterBg     = Color(0xFFFFFFFF)

// ── Data ──────────────────────────────────────────────────────────────────────
data class SearchProduct(
    val id: String,
    val name: String,
    val shop: String,
    val weight: String,
    val price: String,
    val emoji: String
)

private val allProducts = listOf(
    SearchProduct("1", "Beetroot",          "Local shop",     "500 gm.", "14.29", "🫚"),
    SearchProduct("2", "Italian Avocado",   "Local shop",     "450 gm.", "14.29", "🥑"),
    SearchProduct("3", "Beef Mixed", "Local shop","1000 gm.", "14.29", "🥩"),
    SearchProduct("4", "Plant Hunter",      "Frozen pack",    "250 gm.", "14.29", "🌿"),
    SearchProduct("5", "Sprite",            "Can & Bottle",   "250 gm.", "14.29", "🥤"),
    SearchProduct("6", "Szam amm",          "Process food",   "300 gm.", "14.29", "🍱"),
)

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun SearchProductScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    onFilterClick: () -> Unit = {},
    onSelect: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    // ✅ FocusRequester — used to programmatically focus the search field
    val focusRequester = remember { FocusRequester() }

    // ✅ KeyboardController — used to show keyboard programmatically
    val keyboardController = LocalSoftwareKeyboardController.current

    // ✅ Auto-focus + open keyboard as soon as the screen enters composition
    LaunchedEffect(Unit) {
        // Small delay ensures the composable is fully laid out before requesting focus
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    val filteredProducts = remember(query) {
        if (query.isBlank()) allProducts
        else allProducts.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.shop.contains(query, ignoreCase = true)
        }
    }

    val cartState = cartViewModel.uiState.collectAsState()?.value

    Scaffold(
        containerColor = PageBg,
        topBar = {
            SearchTopBar(
                query = query,
                onQueryChange = { query = it },
                focusRequester = focusRequester,
                keyboardController = keyboardController,
                onFilterClick = onFilterClick
            )
        }
    ) { padding ->
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔍", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No results for \"$query\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Try a different keyword", fontSize = 13.sp, color = TextMuted)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    val qty = cartViewModel.getQuantity(product.id) ?: 0
                    ProductGridCard(
                        product = product,
                        quantity = qty,
                        onSelect = {onSelect.invoke()},
                        onAdd = {
                            cartViewModel.addItem(
                                CartItem(
                                    id = product.id,
                                    name = product.name,
                                    shop = product.shop,
                                    weight = product.weight,
                                    price = product.price.toDouble(),
                                    emoji = product.emoji
                                )
                            )
                        },
                        onRemove = { cartViewModel.removeItem(product.id) }
                    )
                }
            }
        }
    }
}

// ── Search Top Bar ────────────────────────────────────────────────────────────
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PageBg)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── Search field ──────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(SearchBg),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )

                Box(modifier = Modifier.weight(1f)) {
                    // Placeholder
                    if (query.isEmpty()) {
                        Text(
                            text = "Search for \"Grocery\"",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }

                    // ✅ BasicTextField with focusRequester attached
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester), // ✅ attach focus here
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            color = TextPrimary
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(Color(0xFF1B3F32)),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = { keyboardController?.hide() }
                        )
                    )
                }
            }
        }

        // ── Filter button ─────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(FilterBg)
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filter",
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Product Grid Card ─────────────────────────────────────────────────────────
@Composable
fun ProductGridCard(
    product: SearchProduct,
    quantity: Int,
    onSelect: () -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect.invoke() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Product image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(Color(0xFFF9F9F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(product.emoji, fontSize = 48.sp)
            }

            // Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.name,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
                Text(
                    text = product.shop,
                    fontSize = 10.sp,
                    color = TextMuted
                )
                Text(
                    text = product.weight,
                    fontSize = 10.sp,
                    color = TextMuted
                )
                Spacer(Modifier.height(4.dp))

                // Price
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = product.price.substringBefore("."),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PriceColor
                    )
                    Text(
                        text = ".${product.price.substringAfter(".")}$",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PriceColor,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
            }

            // Add / Qty button
            if (quantity == 0) {
                // ── Simple + button ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(GreenBtnBg)
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = GreenBtn,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                // ── Qty controls ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(GreenBtnBg),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onRemove() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("−", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary, textAlign = TextAlign.Center)
                    }
                    Text(
                        text = quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(GreenBtn)
                            .clickable { onAdd() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SearchProductScreen( onSelect = {})
}