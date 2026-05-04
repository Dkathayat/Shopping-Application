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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.toCartItem
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartUiState
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.SelectedProductViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.localFallbackProducts

// ── Colors ────────────────────────────────────────────────────────────────────
private val DarkGreen      = Color(0xFF1B3F32)
private val AddToCartGreen = Color(0xFF2D6A4F)
private val PageBg         = Color(0xFFF8F8F8)
private val CardBg         = Color(0xFFFFFFFF)
private val TextMain       = Color(0xFF1A1A1A)
private val TextSub        = Color(0xFF9CA3AF)
private val TextGreen      = Color(0xFF2D7A4F)
private val OrangeBadge    = Color(0xFFF97316)
private val StarYellow     = Color(0xFFF59E0B)
private val DeliveryGreen  = Color(0xFF4CAF50)
private val VariantColors  = listOf(Color(0xFF8B2B2B), Color(0xFFD97706), Color(0xFF1E40AF))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    selectedProductViewModel: SelectedProductViewModel,
    onBack: () -> Unit = {},
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val selectedProduct by selectedProductViewModel.product.collectAsState()
    val cartState by cartViewModel.uiState.collectAsState()

    // Fall back to a default product if none was selected (e.g. direct nav)
    val product = selectedProduct ?: localFallbackProducts.first()

    var quantity by remember(product.id) { mutableIntStateOf(1) }
    var isFavourite by remember(product.id) { mutableStateOf(false) }

    val cartCount = cartState.totalItems
    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    Scaffold(
        containerColor = PageBg,
        topBar = {
            ProductTopBar(
                cartCount = cartCount,
                onBack = onBack,
                onCartClick = { cartViewModel.showCart() }
            )
        },
        bottomBar = {
            ProductBottomBar(
                quantity = quantity,
                onDecrement = { if (quantity > 1) quantity-- },
                onIncrement = { quantity++ },
                onAddToCart = {
                    cartViewModel.addItems(product.toCartItem(), quantity)
                    quantity = 1
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero ─────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(CardBg),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                )
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(product.emoji.ifEmpty { "📦" }, fontSize = 100.sp)
                }
            }

            // ── Detail Card ───────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(CardBg)
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
                                text = product.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain,
                                lineHeight = 26.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = product.weight,
                                fontSize = 13.sp,
                                color = TextSub
                            )
                            Text(
                                text = product.shop,
                                fontSize = 12.sp,
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

                    // Price + badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$${"%.2f".format(product.price)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
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
                                text = "Fast delivery available",
                                fontSize = 11.sp,
                                color = DeliveryGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Category + rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = product.category,
                                fontSize = 12.sp,
                                color = AddToCartGreen,
                                fontWeight = FontWeight.SemiBold
                            )
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
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("100% satisfaction guarantee. Fresh, quality-checked ${product.name} delivered directly from ${product.shop}. If you experience any issues — missing item, late arrival — we'll make it right. ")
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

    if (cartState.isCartVisible) {
        ModalBottomSheet(
            onDismissRequest = { cartViewModel.hideCart() },
            sheetState = cartSheetState,
            containerColor = Color.White
        ) {
            CartBottomSheetContent(
                cartState = cartState,
                onRemoveOne = { cartViewModel.removeItem(it) },
                onAddOne = { cartViewModel.addItem(it) },
                onDelete = { cartViewModel.deleteItem(it) },
                onCheckout = { cartViewModel.hideCart() }
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────────────
@Composable
fun ProductTopBar(
    cartCount: Int,
    onBack: () -> Unit,
    onCartClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkGreen)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
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

        Text(
            text = "Product Details",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // Live cart badge
        BadgedBox(
            badge = {
                if (cartCount > 0) {
                    Badge(containerColor = OrangeBadge) {
                        Text(cartCount.toString(), color = Color.White, fontSize = 9.sp)
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onCartClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ── Bottom Bar ────────────────────────────────────────────────────────────────
@Composable
fun ProductBottomBar(
    quantity: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
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

        // Add to cart button — fully wired
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(AddToCartGreen)
                .clickable { onAddToCart() },
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
private fun Preview() {
    val vm = SelectedProductViewModel()
    vm.select(localFallbackProducts.first())
    ProductDetailScreen(selectedProductViewModel = vm)
}