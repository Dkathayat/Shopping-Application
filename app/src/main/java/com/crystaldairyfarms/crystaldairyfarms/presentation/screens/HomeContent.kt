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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.Category
import com.crystaldairyfarms.crystaldairyfarms.data.DeliverySlot
import com.crystaldairyfarms.crystaldairyfarms.data.FirebaseProduct
import com.crystaldairyfarms.crystaldairyfarms.data.categories
import com.crystaldairyfarms.crystaldairyfarms.data.deliverySlots
import com.crystaldairyfarms.crystaldairyfarms.data.toCartItem
import com.crystaldairyfarms.crystaldairyfarms.presentation.uicomp.CartIconButton
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartUiState
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.ProductState
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.ProductViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.localFallbackProducts
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.BackgroundCream
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.CardWhite
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.DividerColor
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextMuted
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextPrimary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    onStoreClick: () -> Unit,
    onDrawerClicked: () -> Unit,
    cat: () -> Unit,
    onItemClick: () -> Unit,
    onProductClick: (FirebaseProduct) -> Unit = {},
    onCartItemClick: (CartItem) -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onDeliverySlotClick: (String) -> Unit = {},
    onCheckout: () -> Unit = {},
    cartViewModel: CartViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel(),
    locationViewModel: com.crystaldairyfarms.crystaldairyfarms.presentation.vm.LocationViewModel = hiltViewModel()
) {
    val cartState by cartViewModel.uiState.collectAsState()
    val productState by productViewModel.state.collectAsState()
    val locationText by locationViewModel.locationText.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // Request location permission and fetch on grant
    val locationPermLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            locationViewModel.fetchLocation()
        }
    }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        locationPermLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        locationViewModel.fetchLocation()
    }

    // Set white status bar icons so they're visible on the dark Primary background.
    // Restored when leaving this screen.
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(Unit) {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            onDispose { controller.isAppearanceLightStatusBars = true }
        }
    }

    // Only apply bottom padding — top is handled by HomeTopBar's statusBarsPadding()
    // so the Primary background visually fills behind the status bar.
    Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HomeTopBar(
                    cartItem = cartState.totalItems,
                    locationText = locationText,
                    onDrawerClicked = { onDrawerClicked.invoke() },
                    onCartClick = { cartViewModel.showCart() }
                )
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
                CategoryRow(categories) { name -> onCategoryClick(name) }
            }

            Spacer(Modifier.height(20.dp))
            DeliverySlotRow(deliverySlots) { label -> onDeliverySlotClick(label) }

            Spacer(Modifier.height(20.dp))
            SectionHeader("You might need", "See more") { cat.invoke() }
            Spacer(Modifier.height(12.dp))

            // Products — always shows local dairy items until Firebase responds with real data
            val products = (productState as? ProductState.Success)?.products
                ?: localFallbackProducts
            FirebaseProductRow(
                products = products,
                cartState = cartState.items.associate { it.id to it.quantity },
                onAdd = { cartViewModel.addItem(it.toCartItem()) },
                onRemove = { cartViewModel.removeItem(it.id) },
                onItemClick = { product -> onProductClick(product) }
            )

            Spacer(Modifier.height(20.dp))
            FeaturedHeader(onStoreClick)
            Spacer(Modifier.height(80.dp))
        }
    }

    // Cart bottom sheet
    if (cartState.isCartVisible) {
        ModalBottomSheet(
            onDismissRequest = { cartViewModel.hideCart() },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            CartBottomSheetContent(
                cartState = cartState,
                onRemoveOne = { cartViewModel.removeItem(it) },
                onAddOne = { item -> cartViewModel.addItem(item) },
                onDelete = { cartViewModel.deleteItem(it) },
                onCheckout = { cartViewModel.hideCart(); onCheckout() },
                onItemClick = { item ->
                    cartViewModel.hideCart()
                    onCartItemClick(item)
                }
            )
        }
    }
}

// ─── Cart Bottom Sheet ────────────────────────────────────────────────────────

@Composable
fun CartBottomSheetContent(
    cartState: CartUiState,
    onRemoveOne: (String) -> Unit,
    onAddOne: (CartItem) -> Unit,
    onDelete: (String) -> Unit,
    onCheckout: () -> Unit,
    onItemClick: (CartItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            "Your Cart",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (cartState.isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Your cart is empty", color = TextSecondary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartState.items, key = { it.id }) { item ->
                    CartItemRow(
                        item = item,
                        onRemoveOne = { onRemoveOne(item.id) },
                        onAddOne = { onAddOne(item) },
                        onDelete = { onDelete(item.id) },
                        onTap = { onItemClick(item) }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "$${"%.2f".format(cartState.totalPrice)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Primary
                )
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Proceed to Checkout",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onRemoveOne: () -> Unit,
    onAddOne: () -> Unit,
    onDelete: () -> Unit,
    onTap: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(BackgroundCream),
            contentAlignment = Alignment.Center
        ) {
            Text(item.emoji.ifEmpty { "📦" }, fontSize = 24.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(item.weight, fontSize = 12.sp, color = TextMuted)
            Text("$${"%.2f".format(item.price)}", fontSize = 13.sp, color = Primary, fontWeight = FontWeight.Medium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            QuantityButton(Icons.Default.Remove, onClick = onRemoveOne, bg = Color(0xFFE8F5E9))
            Text(
                item.quantity.toString(),
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            QuantityButton(Icons.Default.Add, onClick = onAddOne, bg = Primary, tint = Color.White)
        }
        Spacer(Modifier.width(4.dp))
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
        }
    }
}

// ─── Firebase Product Row ─────────────────────────────────────────────────────

@Composable
fun FirebaseProductRow(
    products: List<FirebaseProduct>,
    cartState: Map<String, Int>,
    onAdd: (FirebaseProduct) -> Unit,
    onRemove: (FirebaseProduct) -> Unit,
    onItemClick: (FirebaseProduct) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products, key = { it.id }) { product ->
            FirebaseProductCard(
                product = product,
                quantity = cartState[product.id] ?: 0,
                onIncrement = { onAdd(product) },
                onDecrement = { onRemove(product) },
                onItemClick = { onItemClick(product) }
            )
        }
    }
}

@Composable
fun FirebaseProductCard(
    product: FirebaseProduct,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onItemClick: (FirebaseProduct) -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(250.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onItemClick(product) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
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
                Text(product.emoji.ifEmpty { "📦" }, fontSize = 38.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                product.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp,
                modifier = Modifier.fillMaxWidth().height(36.dp)
            )
            Text(product.weight, fontSize = 11.sp, color = TextMuted)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "${"%.2f".format(product.price)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text("$", fontSize = 10.sp, color = TextMuted, modifier = Modifier.padding(bottom = 2.dp))
            }
            Spacer(Modifier.height(8.dp))
            if (quantity == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Primary)
                        .clickable { onIncrement() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuantityButton(Icons.Default.Remove, onClick = onDecrement, bg = Color(0xFFE8F5E9))
                    Text(quantity.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    QuantityButton(Icons.Default.Add, onClick = onIncrement, bg = Primary, tint = Color.White)
                }
            }
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@Composable
fun HomeTopBar(
    cartItem: Int,
    locationText: String = "Locating...",
    onDrawerClicked: () -> Unit,
    onCartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "More Options",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickable { onDrawerClicked.invoke() }
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Current Location", color = TextMuted, fontSize = 11.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = locationText,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        CartIconButton(cartItem, onCartClick)
    }
}

@Composable
fun LocationBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Current Location", color = TextMuted, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("New Delhi, IND", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(10.dp))
        }
    }
}

@Composable
fun CategoryRow(categories: List<Category>, onCategoryClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { cat ->
            CategoryItem(cat) { onCategoryClick(cat.name) }
        }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color(0xFFFFF3CD), Color(0xFFFFE08A))))
                .border(2.dp, Color(0xFFFFD54F), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(category.emoji, fontSize = 28.sp)
        }
        Spacer(Modifier.height(6.dp))
        Text(category.name, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun SectionHeader(title: String, action: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(
            modifier = Modifier.clickable { onClick.invoke() },
            text = action,
            fontSize = 13.sp,
            color = Primary,
            fontWeight = FontWeight.SemiBold
        )
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

@Composable
fun DeliverySlotRow(slots: List<DeliverySlot>, onSlotClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(slots) { slot ->
            DeliverySlotCard(slot) { onSlotClick(slot.label) }
        }
    }
}

@Composable
fun DeliverySlotCard(slot: DeliverySlot, modifier: Modifier = Modifier, onSlotClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(120.dp)
            .width(240.dp)
            .clickable { onSlotClick() },
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
                Text(slot.label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(slot.time, fontSize = 11.sp, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text("Free delivery", fontSize = 10.sp, color = Primary, fontWeight = FontWeight.SemiBold)
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

private data class StoreData(
    val name: String,
    val category: String,
    val deliveryTime: String,
    val rating: String,
    val imageRes: Int
)

private val featuredStores = listOf(
    StoreData("T&T Food Market", "Organic • Fresh", "5 min", "4.8", com.crystaldairyfarms.crystaldairyfarms.R.drawable.food_store_organic),
    StoreData("D&D Food Market", "Dairy • Bakery", "15 min", "4.5", com.crystaldairyfarms.crystaldairyfarms.R.drawable.food_store)
)

@Composable
fun FeaturedHeader(onStoreClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Featured Stores", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(
            text = "See all",
            fontSize = 13.sp,
            color = Primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onStoreClick() }
        )
    }
    Spacer(Modifier.height(12.dp))
    FeaturedStore()
}

@Composable
fun FeaturedStore() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(featuredStores, key = { it.name }) { store ->
            StoreCard(store)
        }
    }
}

@Composable
private fun StoreCard(store: StoreData) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(store.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                androidx.compose.material3.Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1B3F32).copy(alpha = 0.85f)
                ) {
                    Text(
                        text = "⭐ ${store.rating}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = store.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = store.category,
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "⚡ ${store.deliveryTime}",
                            color = Primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    androidx.compose.material3.Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF0F0F0)
                    ) {
                        Text(
                            text = "Free delivery",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}