package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.foundation.layout.Spacer
import com.crystaldairyfarms.crystaldairyfarms.presentation.LogoutDialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.Address
import com.crystaldairyfarms.crystaldairyfarms.presentation.Space
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.AddressViewModel
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.PrimaryLight
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    logOut: (Boolean) -> Unit,
    onBack: () -> Unit = {},
    addressViewModel: AddressViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val addresses by addressViewModel.addresses.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<Address?>(null) }
    var addressToDelete by remember { mutableStateOf<Address?>(null) }

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
            // ── User Header ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = Color.LightGray,
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "DK",
                            color = Color.DarkGray,
                            fontSize = (50 / 2).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))
                Column {
                    Text(
                        text = "Deepak Singh Kathayat",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                    Space(5.dp)
                    Text(
                        text = "+919999677471",
                        color = PrimaryLight
                    )
                }
            }

            Spacer(modifier = Modifier.size(20.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.size(20.dp))

            // ── Address Section ────────────────────────────────────────────────
            Text(
                text = "Address",
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.size(12.dp))

            addresses.forEach { address ->
                AddressRow(
                    address = address,
                    onEdit = {
                        editingAddress = address
                        showAddressDialog = true
                    },
                    onDelete = { addressToDelete = address },
                    onSetDefault = { addressViewModel.setDefault(address.id) }
                )
                Spacer(modifier = Modifier.size(8.dp))
            }

            if (addresses.size < 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            editingAddress = null
                            showAddressDialog = true
                        }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add address",
                        tint = Primary
                    )
                    Space(5.dp)
                    Text(
                        text = "Add New Address",
                        color = PrimaryLight,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.size(20.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.size(20.dp))

            // ── Support Section ────────────────────────────────────────────────
            Text(
                text = "Support",
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.size(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.Share, contentDescription = "Share")
                Spacer(modifier = Modifier.size(20.dp))
                Text(modifier = Modifier.weight(1f), text = "Share the app", color = Color.DarkGray, fontSize = 18.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.Support, contentDescription = "Support")
                Spacer(modifier = Modifier.size(20.dp))
                Text(modifier = Modifier.weight(1f), text = "Support", color = Color.DarkGray, fontSize = 18.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "About")
                Spacer(modifier = Modifier.size(20.dp))
                Text(modifier = Modifier.weight(1f), text = "About App", color = Color.DarkGray, fontSize = 18.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
            }

            Spacer(modifier = Modifier.size(20.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.size(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLogoutDialog = true },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.size(20.dp))
                Text(modifier = Modifier.weight(1f), text = "Logout", color = Color.DarkGray, fontSize = 18.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }

    // ── Dialogs ────────────────────────────────────────────────────────────────

    if (showAddressDialog) {
        AddressFormDialog(
            initial = editingAddress,
            onDismiss = { showAddressDialog = false },
            onSave = { houseNo, area, city, pincode ->
                val editing = editingAddress
                if (editing != null) {
                    addressViewModel.updateAddress(
                        editing.copy(houseNo = houseNo, area = area, city = city, pincode = pincode)
                    )
                } else {
                    addressViewModel.addAddress(houseNo, area, city, pincode)
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
                }) {
                    Text("Delete", color = Color.Red)
                }
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
                scope.launch { }
            }
        )
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
                Text(
                    text = address.displayText,
                    color = PrimaryLight,
                    fontSize = 13.sp
                )
                if (address.isDefault) {
                    Text(
                        text = "Default",
                        color = Primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            if (!address.isDefault) {
                TextButton(
                    onClick = onSetDefault,
                    colors = ButtonDefaults.textButtonColors(contentColor = Primary)
                ) {
                    Text("Set Default", fontSize = 11.sp)
                }
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

// ── Add / Edit Address Dialog ──────────────────────────────────────────────────
@Composable
fun AddressFormDialog(
    initial: Address?,
    onDismiss: () -> Unit,
    onSave: (houseNo: String, area: String, city: String, pincode: String) -> Unit
) {
    var houseNo by remember { mutableStateOf(initial?.houseNo ?: "") }
    var area    by remember { mutableStateOf(initial?.area    ?: "") }
    var city    by remember { mutableStateOf(initial?.city    ?: "") }
    var pincode by remember { mutableStateOf(initial?.pincode ?: "") }

    val isValid = houseNo.isNotBlank() && area.isNotBlank() && city.isNotBlank() && pincode.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = { Text(if (initial != null) "Edit Address" else "Add Address", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = houseNo,
                    onValueChange = { houseNo = it },
                    label = { Text("House / Flat No.") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Area / Street") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pincode,
                    onValueChange = { pincode = it },
                    label = { Text("Pincode") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(houseNo, area, city, pincode) },
                enabled = isValid
            ) {
                Text("Save", color = if (isValid) Primary else Color.Gray, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    // ProfileScreen { b -> }
}