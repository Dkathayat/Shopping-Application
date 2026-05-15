package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.uicomp.CartIconButton
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel

// ── Colors ────────────────────────────────────────────────────────────────────
private val CatDarkGreen   = Color(0xFF1B3F32)
private val CatPageBg      = Color(0xFFF8F8F8)
private val CatCardWhite   = Color(0xFFFFFFFF)
private val CatOrangeBg    = Color(0xFFD95F2B)
private val CatTextMain    = Color(0xFF1A1A1A)
private val CatTextSub     = Color(0xFF9CA3AF)


// ── Data Model ────────────────────────────────────────────────────────────────
data class GroceryCategory(
    val name: String,
    val subtitle: String,
    val emoji: String,
    val bgColor: Color
)

private val categoryList = listOf(
    GroceryCategory("Meets",         "Frozen Meal",    "🥩", Color(0xFFFFF5F5)),
    GroceryCategory("Vegetable",     "Markets",        "🥦", Color(0xFFF0FFF4)),
    GroceryCategory("Fruits",        "Comical free",   "🍊", Color(0xFFFFF9F0)),
    GroceryCategory("Breads",        "Burnt",          "🍞", Color(0xFFFFF8F0)),
    GroceryCategory("Snacks",        "Evening",        "🍕", Color(0xFFF5FFF5)),
    GroceryCategory("Bakery",        "Meal and Flour", "🎂", Color(0xFFFFFBF0)),
    GroceryCategory("Dairy & Sweet", "In store",       "🧁", Color(0xFFFFF0F5)),
    GroceryCategory("Chicken",       "Frozen Meal",    "🍗", Color(0xFFFFF5F0)),
)

// ── Screen ────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onCategoryClick: (GroceryCategory) -> Unit = {},
    onCheckout: () -> Unit = {},
    onBack: () -> Unit = {},
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartState by cartViewModel.uiState.collectAsState()
    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    Scaffold(
        containerColor = CatPageBg,
        topBar = {
            CategoryTopBar(
                cartCount = cartState.totalItems,
                onCartClick = { cartViewModel.showCart() },
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            PromoBannerCard()
            Spacer(Modifier.height(16.dp))
            Text(
                text = "All categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CatTextMain,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categoryList) { cat ->
                    CategoryCard(cat) { onCategoryClick(cat) }
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
                onAddOne = { item -> cartViewModel.addItem(item) },
                onDelete = { cartViewModel.deleteItem(it) },
                onCheckout = { cartViewModel.hideCart(); onCheckout() }
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────────────
@Composable
fun CategoryTopBar(
    cartCount: Int = 0,
    onCartClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CatDarkGreen)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "Categories",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        CartIconButton(cartCount, onCartClick)
    }
}

// ── Promo Banner ──────────────────────────────────────────────────────────────
@Composable
fun PromoBannerCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(CatOrangeBg)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = "Get 10% off groceries with Plus+ T&C Apply",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("i", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "Spend \$30.00 to get 5%",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ── Category Card ─────────────────────────────────────────────────────────────
@Composable
fun CategoryCard(category: GroceryCategory, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CatCardWhite)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CatTextMain
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = category.subtitle,
                    fontSize = 11.sp,
                    color = CatTextSub
                )
            }
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(category.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(category.emoji, fontSize = 26.sp)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CategoryScreen()
}