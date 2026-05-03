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
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.FirebaseProduct
import com.crystaldairyfarms.crystaldairyfarms.data.toCartItem
import com.crystaldairyfarms.crystaldairyfarms.data.toFirebaseProduct
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.ProductState
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.ProductViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.SelectedProductViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.localFallbackProducts
import kotlinx.coroutines.delay

// ── Colors ────────────────────────────────────────────────────────────────────
private val SPageBg      = Color(0xFFF5F5F5)
private val SCardBg      = Color(0xFFFFFFFF)
private val SSearchBg    = Color(0xFFFFFFFF)
private val STextPrimary = Color(0xFF1A1A1A)
private val STextMuted   = Color(0xFF9CA3AF)
private val SGreenBtn    = Color(0xFF2D6A4F)
private val SGreenBtnBg  = Color(0xFFEEF7EE)
private val SDarkGreen   = Color(0xFF1B3F32)
private val SOrangeBadge = Color(0xFFF97316)

private val filterCategories = listOf("All", "Dairy", "Vegetables", "Fruits", "Breads", "Sweets")

// ── Screen ────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductScreen(
    selectedProductViewModel: SelectedProductViewModel,
    initialCategory: String = "All",
    cartViewModel: CartViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel(),
    onSelect: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    val productState by productViewModel.state.collectAsState()
    val products = (productState as? ProductState.Success)?.products ?: localFallbackProducts

    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = cartState.totalItems

    val filteredProducts = remember(query, selectedCategory, products) {
        products
            .filter { p ->
                if (selectedCategory == "All") true
                else p.category.equals(selectedCategory, ignoreCase = true)
            }
            .filter { p ->
                query.isBlank() ||
                    p.name.contains(query, ignoreCase = true) ||
                    p.shop.contains(query, ignoreCase = true)
            }
    }

    Scaffold(
        containerColor = SPageBg,
        topBar = {
            SearchTopBar(
                query = query,
                onQueryChange = { query = it },
                focusRequester = focusRequester,
                keyboardController = keyboardController,
                cartCount = cartCount,
                onFilterClick = { showFilterSheet = true },
                onCartClick = { cartViewModel.showCart() }
            )
        }
    ) { padding ->
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔍", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = if (query.isBlank()) "No products in this category"
                               else "No results for \"$query\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = STextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Try a different keyword or filter", fontSize = 13.sp, color = STextMuted)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    val qty = cartState.items.find { it.id == product.id }?.quantity ?: 0
                    SearchProductCard(
                        product = product,
                        quantity = qty,
                        onSelect = {
                            selectedProductViewModel.select(product)
                            onSelect()
                        },
                        onAdd = { cartViewModel.addItem(product.toCartItem()) },
                        onRemove = { cartViewModel.removeItem(product.id) }
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = it },
            onDismiss = { showFilterSheet = false }
        )
    }

    if (cartState.isCartVisible) {
        ModalBottomSheet(
            onDismissRequest = { cartViewModel.hideCart() },
            sheetState = cartSheetState,
            containerColor = Color.White
        ) {
            CartBottomSheetContent(
                cartState = cartState,
                onRemoveOne = { cartViewModel.removeItem(it) },
                onAddOne = { item -> cartViewModel.addItem(item) },
                onDelete = { cartViewModel.deleteItem(it) },
                onCheckout = { cartViewModel.hideCart() },
                onItemClick = { item ->
                    cartViewModel.hideCart()
                    selectedProductViewModel.select(item.toFirebaseProduct())
                    onSelect()
                }
            )
        }
    }
}

// ── Filter Bottom Sheet ───────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Filter by Category",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            filterCategories.forEach { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) SGreenBtnBg else Color(0xFFF5F5F5))
                        .clickable {
                            onCategorySelect(category)
                            onDismiss()
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SGreenBtn else STextPrimary
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(SGreenBtn),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓", fontSize = 11.sp, color = Color.White, textAlign = TextAlign.Center)
                            }
                        }
                    }
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
    cartCount: Int,
    onFilterClick: () -> Unit,
    onCartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SDarkGreen)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(SSearchBg),
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
                    tint = STextMuted,
                    modifier = Modifier.size(18.dp)
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(text = "Search products...", color = STextMuted, fontSize = 13.sp)
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(fontSize = 13.sp, color = STextPrimary),
                        singleLine = true,
                        cursorBrush = SolidColor(SDarkGreen),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() })
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filter",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        BadgedBox(
            badge = {
                if (cartCount > 0) {
                    Badge(containerColor = SOrangeBadge) {
                        Text(cartCount.toString(), color = Color.White, fontSize = 9.sp)
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onCartClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ── Product Grid Card ─────────────────────────────────────────────────────────
@Composable
fun SearchProductCard(
    product: FirebaseProduct,
    quantity: Int,
    onSelect: () -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(Color(0xFFF9F9F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(product.emoji.ifEmpty { "📦" }, fontSize = 48.sp)
            }

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
                    color = STextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp,
                    modifier = Modifier.fillMaxWidth().height(32.dp)
                )
                Text(text = product.shop, fontSize = 10.sp, color = STextMuted)
                Text(text = product.weight, fontSize = 10.sp, color = STextMuted)
                Spacer(Modifier.height(4.dp))

                val priceStr = "%.2f".format(product.price)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = priceStr.substringBefore("."),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = STextPrimary
                    )
                    Text(
                        text = ".${priceStr.substringAfter(".")}$",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = STextPrimary,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
            }

            if (quantity == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(SGreenBtnBg)
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = SGreenBtn,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(SGreenBtnBg),
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
                        Text(
                            text = "−",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = STextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = STextPrimary
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SGreenBtn)
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