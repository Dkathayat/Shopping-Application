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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.crystaldairyfarms.crystaldairyfarms.data.Category
import com.crystaldairyfarms.crystaldairyfarms.data.DeliverySlot
import com.crystaldairyfarms.crystaldairyfarms.data.Product
import com.crystaldairyfarms.crystaldairyfarms.data.categories
import com.crystaldairyfarms.crystaldairyfarms.data.deliverySlots
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.BottomNavGraph
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.CategoryBottomNav
import com.crystaldairyfarms.crystaldairyfarms.presentation.uicomp.CartIconButton
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.BackgroundCream
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.CardWhite
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.DividerColor
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextMuted
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextPrimary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.TextSecondary



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf("ProductDetail", "SearchProduct","Wishlist","Delivery")
    Scaffold(
        bottomBar = { if (!showBottomBar){ CategoryBottomNav(bottomNavController) }},
        containerColor = BackgroundCream,
    ) { padding ->
        BottomNavGraph(bottomNavController, innerPadding = padding)
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    HomeScreen()
}