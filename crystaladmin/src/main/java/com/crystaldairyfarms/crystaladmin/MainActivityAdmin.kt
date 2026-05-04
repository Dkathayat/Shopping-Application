package com.crystaldairyfarms.crystaladmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crystaldairyfarms.crystaladmin.data.FirebaseProduct
import com.crystaldairyfarms.crystaladmin.data.Order
import com.crystaldairyfarms.crystaladmin.ui.theme.CrystalDairyfarmsTheme
import com.crystaldairyfarms.crystaladmin.vm.AdminOrderViewModel
import com.crystaldairyfarms.crystaladmin.vm.AdminViewModel
import com.crystaldairyfarms.crystaladmin.vm.UploadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val AdminGreen = Color(0xFF2D6A4F)
private val AdminGreenLight = Color(0xFFE8F5E9)
private val AdminBg = Color(0xFFF5F5F5)

private val ADMIN_CREDENTIALS = mapOf(
    "9999677471" to "deepak",
    "9876543210" to "admin1",
    "8765432109" to "admin2",
    "7654321098" to "admin3",
    "6543210987" to "admin4",
    "5432109876" to "admin5"
)

@AndroidEntryPoint
class MainActivityAdmin : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrystalDairyfarmsTheme {
                AdminNavHost()
            }
        }
    }
}

@Composable
fun AdminNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "AdminLogin") {
        composable("AdminLogin") {
            AdminLoginScreen(
                onLoginSuccess = {
                    navController.navigate("AdminDashboard") {
                        popUpTo("AdminLogin") { inclusive = true }
                    }
                }
            )
        }
        composable("AdminDashboard") {
            AdminDashboard(
                onLogout = {
                    navController.navigate("AdminLogin") {
                        popUpTo("AdminDashboard") { inclusive = true }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(onLoginSuccess: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AdminBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🥛", fontSize = 64.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                "Crystal Admin",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AdminGreen
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Sign in to manage your store",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val expected = ADMIN_CREDENTIALS[phone.trim()]
                    if (expected != null && expected == password.trim()) {
                        onLoginSuccess()
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Invalid credentials") }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminGreen)
            ) {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    onLogout: () -> Unit,
    orderViewModel: AdminOrderViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val newOrderCount by orderViewModel.newOrderCount.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(newOrderCount) {
        if (newOrderCount > 0) {
            scope.launch {
                snackbarHostState.showSnackbar("🔔 New order received!")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Crystal Admin", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminGreen,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AdminBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = AdminGreen,
                contentColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Products") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        orderViewModel.clearNewOrderNotification()
                    },
                    text = {
                        if (newOrderCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge { Text(newOrderCount.toString()) }
                                }
                            ) {
                                Text("Orders")
                            }
                        } else {
                            Text("Orders")
                        }
                    }
                )
            }

            when (selectedTab) {
                0 -> AdminScreen()
                1 -> AdminOrdersScreen(viewModel = orderViewModel)
            }
        }
    }
}

@Composable
fun AdminOrdersScreen(viewModel: AdminOrderViewModel = hiltViewModel()) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AdminGreen
            )
            orders.isEmpty() -> Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("📦", fontSize = 64.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "No orders yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Orders from customers will appear here",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders, key = { it.orderId }) { order ->
                    AdminOrderCard(
                        order = order,
                        onStatusChange = { newStatus ->
                            viewModel.updateOrderStatus(order.orderId, newStatus)
                        }
                    )
                }
            }
        }
    }
}

private val orderStatusOptions = listOf("pending", "confirmed", "out for delivery", "delivered")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderCard(order: Order, onStatusChange: (String) -> Unit) {
    var statusExpanded by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    val statusColor = when (order.status) {
        "confirmed" -> Color(0xFF1565C0)
        "out for delivery" -> Color(0xFFE65100)
        "delivered" -> Color(0xFF2E7D32)
        else -> Color(0xFF757575)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Order ID + status chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${order.orderId.takeLast(8).uppercase()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = AdminGreen
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(statusColor.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = order.status.replaceFirstChar { it.uppercase() },
                        fontSize = 12.sp,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Customer info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👤 ", fontSize = 14.sp)
                Text(
                    text = order.userName.ifEmpty { "Unknown" },
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = order.userPhone,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Payment method
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💳 ", fontSize = 14.sp)
                Text(
                    text = order.paymentMethod.ifEmpty { "N/A" },
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Item emojis
            if (order.items.isNotEmpty()) {
                val emojiString = order.items.take(10).joinToString("") { it.emoji.ifEmpty { "📦" } }
                Text(text = emojiString, fontSize = 22.sp)
                Text(
                    text = "${order.items.sumOf { it.quantity }} item(s)",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Total
            Text(
                text = "Total: $${"%.2f".format(order.total)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AdminGreen
            )

            // Timestamp
            if (order.timestamp > 0L) {
                Text(
                    text = dateFormat.format(Date(order.timestamp)),
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }

            // Delivery address
            if (order.deliveryAddress.isNotEmpty()) {
                Row(verticalAlignment = Alignment.Top) {
                    Text("📍 ", fontSize = 13.sp)
                    Text(
                        text = order.deliveryAddress,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Status dropdown
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = it }
            ) {
                OutlinedTextField(
                    value = order.status.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Update Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(statusExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    orderStatusOptions.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                onStatusChange(status)
                                statusExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()

    var showAddSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uploadState) {
        when (val s = uploadState) {
            is UploadState.Success -> {
                scope.launch { snackbarHostState.showSnackbar("Product added successfully!") }
                viewModel.resetUploadState()
            }
            is UploadState.Error -> {
                scope.launch { snackbarHostState.showSnackbar("Error: ${s.message}") }
                viewModel.resetUploadState()
            }
            else -> Unit
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = AdminGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AdminBg
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AdminGreen
                )
                products.isEmpty() -> EmptyProductState(modifier = Modifier.align(Alignment.Center))
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductAdminCard(product = product, onDelete = { viewModel.deleteProduct(it) })
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        AddProductSheet(
            isSubmitting = uploadState is UploadState.Loading,
            onDismiss = { showAddSheet = false },
            onAdd = { product ->
                viewModel.addProduct(product)
                showAddSheet = false
            }
        )
    }
}

@Composable
fun EmptyProductState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Inventory,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = Color.LightGray
        )
        Spacer(Modifier.height(16.dp))
        Text("No products yet", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        Text("Tap + to add your first product", fontSize = 14.sp, color = Color.LightGray)
    }
}

@Composable
fun ProductAdminCard(product: FirebaseProduct, onDelete: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AdminGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Text(product.emoji.ifEmpty { "📦" }, fontSize = 28.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(product.category, color = Color.Gray, fontSize = 12.sp)
                Spacer(Modifier.height(2.dp))
                Text(
                    "${product.weight}  •  $${"%.2f".format(product.price)}",
                    fontSize = 12.sp,
                    color = AdminGreen,
                    fontWeight = FontWeight.Medium
                )
                Text(product.shop, fontSize = 11.sp, color = Color.LightGray)
            }
            IconButton(onClick = { onDelete(product.id) }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete product",
                    tint = Color(0xFFD32F2F)
                )
            }
        }
    }
}

private val categoryOptions = listOf(
    "Vegetables", "Dairy", "Fruits", "Breads", "Snacks", "Bakery", "Chicken", "Beverages", "General"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductSheet(
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onAdd: (FirebaseProduct) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var shop by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(categoryOptions[0]) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Add New Product",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Product Name *") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError,
                supportingText = if (nameError) ({ Text("Required") }) else null,
                singleLine = true
            )

            OutlinedTextField(
                value = shop,
                onValueChange = { shop = it },
                label = { Text("Shop / Brand") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight / Unit") },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("e.g. 500 gm") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it; priceError = false },
                    label = { Text("Price ($) *") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = priceError,
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = emoji,
                onValueChange = { emoji = it.take(2) },
                label = { Text("Emoji") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 🥕") },
                singleLine = true
            )

            // Category dropdown
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(categoryExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categoryOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { category = option; categoryExpanded = false }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    nameError = name.isBlank()
                    priceError = priceText.toDoubleOrNull() == null
                    if (!nameError && !priceError) {
                        onAdd(
                            FirebaseProduct(
                                name = name.trim(),
                                shop = shop.trim().ifEmpty { "Local Shop" },
                                weight = weight.trim().ifEmpty { "1 unit" },
                                price = priceText.toDouble(),
                                emoji = emoji.trim().ifEmpty { "📦" },
                                category = category
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminGreen),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Upload Product", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}