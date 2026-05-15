package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.Address
import com.crystaldairyfarms.crystaldairyfarms.data.CartItem
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.AddressViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CartViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.CheckoutViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.OrderState
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlin.random.Random

private val DarkGreen = Color(0xFF1B3F32)
private val LightBg = Color(0xFFF8F8F8)
private val CardWhite = Color(0xFFFFFFFF)

enum class PaymentOption(
    val label: String,
    val emoji: String,
    val packageName: String?,
    val color: Color
) {
    GPAY("Google Pay", "G", "com.google.android.apps.nbu.paisa.user", Color(0xFF4285F4)),
    PHONEPE("PhonePe", "P", "com.phonepe.app", Color(0xFF5F259F)),
    PAYTM("Paytm", "P", "net.one97.paytm", Color(0xFF00BAF2)),
    BHIM("BHIM UPI", "B", "in.org.npci.upiapp", Color(0xFF008800)),
    AMAZON_PAY("Amazon Pay", "A", "in.amazon.mShop.android.shopping", Color(0xFFFF9900)),
    COD("Cash on Delivery", "₹", null, Color(0xFF388E3C))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit = {},
    onOrderSuccess: (String) -> Unit = {},
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    addressViewModel: AddressViewModel = hiltViewModel()
) {
    val cartState by cartViewModel.uiState.collectAsState()
    val orderState by checkoutViewModel.orderState.collectAsState()
    val addresses by addressViewModel.addresses.collectAsState()
    val defaultAddress = addresses.find { it.isDefault } ?: addresses.firstOrNull()

    val hasAddress = defaultAddress != null
    val hasPhone = defaultAddress?.phone?.isNotBlank() == true

    var selectedPayment by remember { mutableStateOf<PaymentOption?>(null) }
    var showUpiConfirmDialog by remember { mutableStateOf(false) }
    var pendingPaymentMethod by remember { mutableStateOf("") }
    val context = LocalContext.current

    val upiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        showUpiConfirmDialog = true
    }

    if (orderState is OrderState.Success) {
        val orderId = (orderState as OrderState.Success).orderId
        OrderSuccessScreen(
            orderId = orderId,
            onDone = {
                checkoutViewModel.reset()
                onOrderSuccess(orderId)
            }
        )
        return
    }

    Scaffold(
        containerColor = LightBg,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGreen)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
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
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Text(
                    "Checkout",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = CardWhite) {
                Button(
                    onClick = {
                        val payment = selectedPayment ?: return@Button
                        if (payment == PaymentOption.COD) {
                            checkoutViewModel.placeOrder(
                                items = cartState.items,
                                paymentMethod = "Cash on Delivery",
                                deliveryAddress = defaultAddress?.displayText ?: "No address"
                            )
                        } else {
                            pendingPaymentMethod = payment.label
                            val orderId = "CRDA-${(10000..99999).random()}"
                            val upiUri = Uri.Builder()
                                .scheme("upi").authority("pay")
                                .appendQueryParameter("pa", "crystaldairy@upi")
                                .appendQueryParameter("pn", "Crystal Dairy Farms")
                                .appendQueryParameter("tr", orderId)
                                .appendQueryParameter("tn", "Payment for $orderId")
                                .appendQueryParameter("am", "%.2f".format(cartState.totalPrice))
                                .appendQueryParameter("cu", "INR")
                                .build()
                            val intent = Intent(Intent.ACTION_VIEW, upiUri).apply {
                                payment.packageName?.let { setPackage(it) }
                            }
                            runCatching { upiLauncher.launch(intent) }
                                .onFailure { showUpiConfirmDialog = true }
                        }
                    },
                    enabled = selectedPayment != null && cartState.items.isNotEmpty() &&
                        hasAddress && hasPhone && orderState !is OrderState.Loading,
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    if (orderState is OrderState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(
                            "Place Order  •  $${"%.2f".format(cartState.totalPrice)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                CheckoutSection(title = "Order Summary (${cartState.totalItems} items)") {
                    if (cartState.isEmpty) {
                        Text("Your cart is empty", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                    } else {
                        cartState.items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Emoji + name
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(item.emoji, fontSize = 22.sp)
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                                        Text("$${"%.2f".format(item.price)} each", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                                // Quantity controls + price
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Minus or Delete
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (item.quantity == 1) Color(0xFFFFEBEB)
                                                else Color(0xFFE8F5E9)
                                            )
                                            .clickable {
                                                if (item.quantity == 1) cartViewModel.deleteItem(item.id)
                                                else cartViewModel.removeItem(item.id)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                                            contentDescription = if (item.quantity == 1) "Delete" else "Decrease",
                                            tint = if (item.quantity == 1) Color(0xFFD32F2F) else Primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = "${item.quantity}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.width(20.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    // Plus
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Primary)
                                            .clickable { cartViewModel.addItem(item) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "$${"%.2f".format(item.price * item.quantity)}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = Primary
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("$${"%.2f".format(cartState.totalPrice)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Primary)
                        }
                    }
                }
            }

            item {
                CheckoutSection(title = "Delivery Address") {
                    if (defaultAddress != null) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(defaultAddress.displayText, fontSize = 13.sp, color = Color(0xFF444444))
                                if (defaultAddress.phone.isNotBlank()) {
                                    Text("📞 ${defaultAddress.phone}", fontSize = 12.sp, color = Color.Gray)
                                }
                                Text("Default address", fontSize = 11.sp, color = Primary)
                            }
                        }
                        if (!hasPhone) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "⚠️ No contact phone on this address. Edit it in Profile to place orders.",
                                fontSize = 12.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                    } else {
                        Text(
                            "⚠️ No address saved. Add one in Profile to place orders.",
                            fontSize = 13.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }
            }

            item {
                CheckoutSection(title = "Payment Method") {
                    PaymentOption.entries.forEach { option ->
                        val isSelected = selectedPayment == option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Primary else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(if (isSelected) Color(0xFFE8F5E9) else Color.White)
                                .clickable { selectedPayment = option }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(CircleShape).background(option.color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(option.emoji, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(option.label, modifier = Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            if (orderState is OrderState.Error) {
                item {
                    Text(
                        (orderState as OrderState.Error).message,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }

    if (showUpiConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showUpiConfirmDialog = false },
            shape = RoundedCornerShape(16.dp),
            title = { Text("Confirm Payment", fontWeight = FontWeight.Bold) },
            text = {
                Text("Did you complete the payment of $${"%.2f".format(cartState.totalPrice)} via $pendingPaymentMethod?")
            },
            confirmButton = {
                TextButton(onClick = {
                    showUpiConfirmDialog = false
                    checkoutViewModel.placeOrder(
                        items = cartState.items,
                        paymentMethod = pendingPaymentMethod,
                        deliveryAddress = defaultAddress?.displayText ?: "No address"
                    )
                }) {
                    Text("Yes, Paid", color = Primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUpiConfirmDialog = false }) { Text("No, Cancel") }
            }
        )
    }
}

@Composable
private fun CheckoutSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun OrderSuccessScreen(orderId: String, onDone: () -> Unit) {
    // Auto-navigate to Orders screen after confetti finishes
    LaunchedEffect(Unit) {
        delay(2800)
        onDone()
    }

    var animStarted by remember { mutableStateOf(false) }
    val confettiProgress by animateFloatAsState(
        targetValue = if (animStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 2500, easing = LinearEasing),
        label = "confetti"
    )
    LaunchedEffect(Unit) { animStarted = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B3F32))
    ) {
        // Confetti canvas layer
        ConfettiCanvas(progress = confettiProgress)

        // Central content
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Yay! Order Placed!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(orderId, color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp)
            Spacer(Modifier.height(6.dp))
            Text("We'll deliver it shortly 🚚", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
        }
    }
}

// Particle data — computed once per composition
private data class ConfettiParticle(
    val xFraction: Float,   // 0..1 relative to canvas width
    val startY: Float,      // negative offset so particles begin above screen
    val color: Color,
    val widthPx: Float,
    val heightPx: Float,
    val vSpeed: Float,      // vertical speed multiplier
    val rotationSeed: Float // base rotation angle
)

@Composable
private fun ConfettiCanvas(progress: Float, modifier: Modifier = Modifier) {
    val confettiColors = remember {
        listOf(
            Color(0xFFFF6B6B), Color(0xFFFFD93D), Color(0xFF6BCB77),
            Color(0xFF4D96FF), Color(0xFFFF6EAD), Color(0xFFFFB347),
            Color(0xFFE0AAFF), Color(0xFF00F5FF)
        )
    }
    val particles = remember {
        List(100) { i ->
            ConfettiParticle(
                xFraction  = Random.nextFloat(),
                startY     = -Random.nextFloat() * 0.6f,
                color      = confettiColors[i % confettiColors.size],
                widthPx    = 8f + Random.nextFloat() * 12f,
                heightPx   = 5f + Random.nextFloat() * 8f,
                vSpeed     = 0.6f + Random.nextFloat() * 0.8f,
                rotationSeed = Random.nextFloat() * 360f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { p ->
            val y = (p.startY + progress * p.vSpeed) * size.height
            if (y < size.height) {
                val x = p.xFraction * size.width
                val rotation = p.rotationSeed + progress * 540f
                withTransform({
                    rotate(rotation, pivot = Offset(x, y))
                }) {
                    drawRect(
                        color     = p.color,
                        topLeft   = Offset(x - p.widthPx / 2, y - p.heightPx / 2),
                        size      = Size(p.widthPx, p.heightPx)
                    )
                }
            }
        }
    }
}