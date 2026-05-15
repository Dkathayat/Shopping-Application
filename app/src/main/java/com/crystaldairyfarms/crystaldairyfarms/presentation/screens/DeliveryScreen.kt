package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.Order
import com.crystaldairyfarms.crystaldairyfarms.data.OrderItem
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.OrderViewModel
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DeliveryScreen(
    bottomPadding: Dp = 0.dp,
    onBack: () -> Unit = {},
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val orders by orderViewModel.orders.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()

    val dummyOrder = remember {
        Order(
            orderId = "CRDA-00001",
            userName = "Deepak Singh Kathayat",
            userPhone = "+919999677471",
            items = listOf(
                OrderItem("1", "Fresh Milk",     "🥛", 2.49, 2),
                OrderItem("2", "Indian Paneer",  "🧀", 4.19, 1),
                OrderItem("3", "Brown Bread",    "🍞", 1.99, 1),
                OrderItem("4", "Strawberries",   "🍓", 3.59, 1)
            ),
            total = 14.75,
            paymentMethod = "Cash on Delivery",
            status = "delivered",
            timestamp = System.currentTimeMillis() - 86_400_000L,
            deliveryAddress = "Hno 123, Dwarka Mor, New Delhi - 110059"
        )
    }
    val displayOrders = if (!isLoading && orders.isEmpty()) listOf(dummyOrder) else orders

    Scaffold(
        modifier = Modifier.padding(bottom = bottomPadding),
        containerColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1B3F32))
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
                    text = "My Orders",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
           // DeliveryActions()
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                    )
                }

                displayOrders.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🛍️",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No orders yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                        Text(
                            text = "Place your first order to see it here",
                            fontSize = 14.sp,
                            color = Color.LightGray
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "My Orders",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }

                        items(displayOrders, key = { it.orderId }) { order ->
                            OrderCard(order = order)
                        }

                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    var expanded by remember { mutableStateOf(false) }
    var showTrackSheet by remember { mutableStateOf(false) }

    val dateFormatted = remember(order.timestamp) {
        if (order.timestamp > 0L) {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date(order.timestamp))
        } else {
            "—"
        }
    }

    val statusColor = when (order.status.lowercase()) {
        "delivered" -> Color(0xFF4CAF50)
        "cancelled" -> Color(0xFFF44336)
        "out for delivery" -> Color(0xFF2196F3)
        else -> Color(0xFFFF9800)
    }

    val previewEmojis = order.items.take(10).joinToString(" ") { it.emoji }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header row: order ID + status chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.orderId.takeLast(6).uppercase()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = order.status.replaceFirstChar { it.uppercase() },
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Date
            Text(
                text = dateFormatted,
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Emoji preview of first 10 items
            if (previewEmojis.isNotEmpty()) {
                Text(
                    text = previewEmojis,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "$${"%.2f".format(order.total)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Expandable items section
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(
                        text = "Items",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = item.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.name,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                text = "x${item.quantity}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Track Order button
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { showTrackSheet = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "🚚  Track Order",
                    color = Primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Expand/collapse hint
            Text(
                text = if (expanded) "▲ Show less" else "▼ Show items",
                fontSize = 12.sp,
                color = Primary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { expanded = !expanded }
            )
        }
    }

    if (showTrackSheet) {
        TrackOrderSheet(order = order, onDismiss = { showTrackSheet = false })
    }
}

@Preview
@Composable
private fun Preview() {
    DeliveryScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderSheet(order: Order, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val steps = listOf("Order Placed", "Order Packed", "Out for Delivery", "Delivered")
    val currentStep = when (order.status.lowercase()) {
        "placed" -> 0
        "packed" -> 1
        "out for delivery" -> 2
        "delivered" -> 3
        else -> 0
    }

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
        ) {
            Text("Track Order", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Order #${order.orderId.takeLast(6).uppercase()}",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))

            steps.forEachIndexed { index, step ->
                val isCompleted = index < currentStep
                val isActive = index == currentStep
                val dotColor = if (isCompleted || isActive) Color(0xFF1B3F32) else Color(0xFFE0E0E0)
                val lineColor = if (isCompleted) Color(0xFF1B3F32) else Color(0xFFE0E0E0)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(dotColor),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Text(
                                    text = "✓",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            } else if (isActive) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(48.dp)
                                    .background(lineColor)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.padding(top = 1.dp)) {
                        Text(
                            text = step,
                            fontSize = 15.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCompleted || isActive) Color(0xFF1A1A1A) else Color.Gray
                        )
                        if (isActive) {
                            Text("In progress", fontSize = 12.sp, color = Color(0xFF1B3F32))
                        } else if (isCompleted) {
                            Text("Done", fontSize = 12.sp, color = Color(0xFF4CAF50))
                        }
                        if (index < steps.size - 1) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919983605466"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3F32))
            ) {
                Text(
                    text = "📞  Call Delivery Agent",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}