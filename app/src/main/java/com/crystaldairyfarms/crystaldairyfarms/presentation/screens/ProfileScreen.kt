package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Support
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.crystaldairyfarms.crystaldairyfarms.data.Address
import com.crystaldairyfarms.crystaldairyfarms.presentation.LogoutDialog
import com.crystaldairyfarms.crystaldairyfarms.presentation.Space
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.AddressViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.AuthViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.UserState
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.PrimaryLight

@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onSignOut: () -> Unit = {},
    addressViewModel: AddressViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current as ComponentActivity
    val authViewModel: AuthViewModel = viewModel(viewModelStoreOwner = activity)
    val context = LocalContext.current

    val userState by authViewModel.userState.collectAsState()
    val addresses by addressViewModel.addresses.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<Address?>(null) }
    var addressToDelete by remember { mutableStateOf<Address?>(null) }

    val isGuest = userState is UserState.Guest
    val googleUser = (userState as? UserState.SignedIn)?.user
    val displayName = googleUser?.displayName ?: "Guest"
    val displayInitial = displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "G"
    val photoUrl = googleUser?.photoUrl?.toString()
    val displaySub = when {
        !isGuest && googleUser?.phoneNumber?.isNotBlank() == true -> googleUser.phoneNumber!!
        !isGuest && googleUser?.email?.isNotBlank() == true -> googleUser.email!!
        isGuest -> "Browsing as guest"
        else -> ""
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Primary
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // ── User Header ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar: photo from Google or initial letter
                if (!photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Surface(
                        modifier = Modifier.size(54.dp),
                        shape = CircleShape,
                        color = Primary.copy(alpha = 0.15f)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = displayInitial,
                                color = Primary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        text = displayName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    if (displaySub.isNotEmpty()) {
                        Space(4.dp)
                        Text(text = displaySub, color = PrimaryLight, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.size(20.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.size(20.dp))

            // ── Address Section ──────────────────────────────────────────────────
            Text(text = "Address", fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
            Spacer(modifier = Modifier.size(12.dp))

            if (addresses.isEmpty()) {
                Text(
                    text = "No saved addresses. Add one to place orders.",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                addresses.forEach { address ->
                    AddressRow(
                        address = address,
                        onEdit = { editingAddress = address; showAddressDialog = true },
                        onDelete = { addressToDelete = address },
                        onSetDefault = { addressViewModel.setDefault(address.id) }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }

            if (addresses.size < 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { editingAddress = null; showAddressDialog = true }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Primary)
                    Space(5.dp)
                    Text(text = "Add New Address", color = PrimaryLight, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.size(20.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.size(20.dp))

            // ── Support Section ──────────────────────────────────────────────────
            Text(text = "Support", fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
            Spacer(modifier = Modifier.size(20.dp))

            SupportRow("Share the app", Icons.Outlined.Share)
            Spacer(modifier = Modifier.size(20.dp))
            SupportRow("Support", Icons.Outlined.Support)
            Spacer(modifier = Modifier.size(20.dp))
            SupportRow("About App", Icons.Outlined.Info)

            Spacer(modifier = Modifier.size(20.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.size(20.dp))

            // ── Logout (only shown for signed-in users, hidden for guests) ─────────
            if (userState is UserState.SignedIn) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(modifier = Modifier.weight(1f), text = "Logout", color = Color.DarkGray, fontSize = 18.sp)
                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.size(24.dp))
        }
    }

    // ── Dialogs ──────────────────────────────────────────────────────────────────
    if (showAddressDialog) {
        AddressFormDialog(
            initial = editingAddress,
            onDismiss = { showAddressDialog = false },
            onSave = { houseNo, area, city, pincode, phone ->
                val editing = editingAddress
                if (editing != null) {
                    addressViewModel.updateAddress(
                        editing.copy(houseNo = houseNo, area = area, city = city, pincode = pincode, phone = phone)
                    )
                } else {
                    addressViewModel.addAddress(houseNo, area, city, pincode, phone)
                }
                showAddressDialog = false
            }
        )
    }

    if (addressToDelete != null) {
        AlertDialog(
            onDismissRequest = { addressToDelete = null },
            title = { Text("Delete Address") },
            text = { Text("Remove \"${addressToDelete!!.displayText}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    addressViewModel.deleteAddress(addressToDelete!!.id)
                    addressToDelete = null
                }) { Text("Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { addressToDelete = null }) { Text("Cancel") }
            }
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                authViewModel.signOut(context)
                onSignOut()
            }
        )
    }
}

@Composable
private fun SupportRow(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.size(20.dp))
        Text(modifier = Modifier.weight(1f), text = label, color = Color.DarkGray, fontSize = 18.sp)
        Spacer(modifier = Modifier.size(20.dp))
        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
    }
}

// ── Address Row ────────────────────────────────────────────────────────────────
@Composable
fun AddressRow(
    address: Address,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationCity,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )
            Space(6.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = address.displayText, color = PrimaryLight, fontSize = 13.sp)
                if (address.phone.isNotBlank()) {
                    Text(text = "📞 ${address.phone}", color = Color.Gray, fontSize = 12.sp)
                }
                if (address.isDefault) {
                    Text(text = "Default", color = Primary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            if (!address.isDefault) {
                TextButton(
                    onClick = onSetDefault,
                    colors = ButtonDefaults.textButtonColors(contentColor = Primary)
                ) { Text("Set Default", fontSize = 11.sp) }
            }
            IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
        }
    }
}

// ── Add / Edit Address Dialog ─────────────────────────────────────────────────
@Composable
fun AddressFormDialog(
    initial: Address?,
    onDismiss: () -> Unit,
    onSave: (houseNo: String, area: String, city: String, pincode: String, phone: String) -> Unit
) {
    var houseNo by remember { mutableStateOf(initial?.houseNo ?: "") }
    var area    by remember { mutableStateOf(initial?.area    ?: "") }
    var city    by remember { mutableStateOf(initial?.city    ?: "") }
    var pincode by remember { mutableStateOf(initial?.pincode ?: "") }
    var phone   by remember { mutableStateOf(initial?.phone   ?: "") }

    val isValid = houseNo.isNotBlank() && area.isNotBlank() &&
        city.isNotBlank() && pincode.isNotBlank() && phone.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = { Text(if (initial != null) "Edit Address" else "Add Address", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = houseNo, onValueChange = { houseNo = it },
                    label = { Text("House / Flat No.") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = area, onValueChange = { area = it },
                    label = { Text("Area / Street") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = city, onValueChange = { city = it },
                    label = { Text("City") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pincode, onValueChange = { pincode = it },
                    label = { Text("Pincode") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("Contact Phone") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(houseNo, area, city, pincode, phone) },
                enabled = isValid
            ) {
                Text(
                    "Save",
                    color = if (isValid) Primary else Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}