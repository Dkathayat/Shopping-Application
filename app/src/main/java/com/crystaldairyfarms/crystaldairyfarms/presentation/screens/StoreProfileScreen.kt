package com.crystaldairyfarms.crystaldairyfarms.presentation.screens



import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crystaldairyfarms.crystaldairyfarms.data.Category
import com.crystaldairyfarms.crystaldairyfarms.data.Product
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.BackgroundCream
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.CardWhite
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.DeliveryBlue
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.DividerColor
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.OrangeBadge
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.PurpleDeep
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.PurpleMedium
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextMuted
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextPrimary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextSecondary

// ─── Store Profile Screen ─────────────────────────────────────────────────────

@Composable
fun StoreProfileScreen(onBack: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }

    val storeCategories = listOf(
        Category("Meets", "🥩"),
        Category("Veggies", "🥦"),
        Category("Fruits", "🍊"),
        Category("Breads", "🍞"),
        Category("Corn", "🌽")
    )

    val bestSelling = remember {
        mutableStateListOf(
            Product("Beetroot", "Local shop", "500 gm.", "17.29", "🫚"),
            Product("Italian Avocado", "Local shop", "450 gm.", "14.29", "🥑"),
            Product("Deshi Gajor\n(Local Carrot)", "Local shop", "300 gm.", "27.29", "🥕")
        )
    }

    Scaffold(
        topBar = { StoreTopBar(onBack) },
        containerColor = BackgroundCream
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Store Info Card ──
            StoreInfoCard()

            Spacer(Modifier.height(16.dp))

            // ── Delivery / Pickup Toggle ──
            DeliveryPickupTabs(selectedTab) { selectedTab = it }

            Spacer(Modifier.height(16.dp))

            // ── Promo Banner ──
            PromoBanner()

            Spacer(Modifier.height(16.dp))

            // ── Search inside store ──
            StoreSearchBar()

            Spacer(Modifier.height(16.dp))

            // ── Category Row ──
            StoreCategoryRow(storeCategories)

            Spacer(Modifier.height(20.dp))

            // ── Best Selling ──
           // SectionHeader("Best selling", "View all")

            Spacer(Modifier.height(12.dp))

            ProductRow(bestSelling){

            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ─── Store Top Bar ────────────────────────────────────────────────────────────

@Composable
fun StoreTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(PurpleDeep, PurpleMedium)
                )
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Back
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Title
        Text(
            "Store profile",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        // Cart
        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(OrangeBadge)
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp),
                contentAlignment = Alignment.Center
            ) {
                Text("2", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ─── Store Info Card ──────────────────────────────────────────────────────────

@Composable
fun StoreInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Store Logo
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF1A7B4B), Color(0xFF2D9E6A)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("T&T", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    Text("大統華", color = Color(0xFFFFD700), fontSize = 7.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "T&T Food Market",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    "Vegetable • Superfood • Grocery",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    "Tap for hours, info, and more",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─── Delivery / Pickup Tabs ───────────────────────────────────────────────────

@Composable
fun DeliveryPickupTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("Delivery", "Pickup").forEachIndexed { index, label ->
            val selected = selectedTab == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selected) PurpleMedium else CardWhite)
                    .border(1.dp, if (selected) PurpleMedium else DividerColor, RoundedCornerShape(20.dp))
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    color = if (selected) Color.White else TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }

        // ETA badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFFFF3CD))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⚡", fontSize = 12.sp)
            Spacer(Modifier.width(4.dp))
            Text(
                "In 12 minute",
                fontSize = 12.sp,
                color = Color(0xFF92400E),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─── Promo Banner ─────────────────────────────────────────────────────────────

@Composable
fun PromoBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DeliveryBlue)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Get 10% off groceries with Plus+ T&C Apply",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Spend \$30.00 to get 5%",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// ─── Store Search Bar ─────────────────────────────────────────────────────────

@Composable
fun StoreSearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(CardWhite)
                .border(1.dp, DividerColor, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Search for \"Grocery\"", color = TextMuted, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardWhite)
                .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = TextPrimary, modifier = Modifier.size(20.dp))
        }
    }
}

// ─── Store Category Row ───────────────────────────────────────────────────────

@Composable
fun StoreCategoryRow(categories: List<Category>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { cat ->
            StoreCategoryItem(cat)
        }
    }
}

@Composable
fun StoreCategoryItem(category: Category) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFF9E6), Color(0xFFFFE082))
                    )
                )
                .border(1.5.dp, Color(0xFFFFCC02), CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Text(category.emoji, fontSize = 24.sp)
        }
        Spacer(Modifier.height(5.dp))
        Text(
            category.name,
            fontSize = 11.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    StoreProfileScreen {

    }
}