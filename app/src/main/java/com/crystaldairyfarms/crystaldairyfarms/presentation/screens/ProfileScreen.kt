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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Support
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crystaldairyfarms.crystaldairyfarms.presentation.Space
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.PrimaryLight
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    logOut: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    //  val googleAuthHelper = remember { GoogleAuthUiHelper(context) }

    var showLogoutDialog by remember { mutableStateOf(false) }

    // val initial =
    //    Firebase.auth.currentUser?.displayName?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Column(
        modifier = Modifier
            .padding(20.dp)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
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
        Text(
            text = "Address",
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.LocationCity,
                contentDescription = "Location image",
                tint = Primary
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Hno 123 Dwarka Mor New Delhi 110059",
                color = PrimaryLight,
                fontSize = 14.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Location image",
                tint = Primary
            )
            Space(5.dp)
            Text(
                modifier = Modifier.weight(1f),
                text = "Add New Address",
                color = PrimaryLight,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        HorizontalDivider(color = Color.LightGray)
        Spacer(modifier = Modifier.size(20.dp))
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
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "support image")
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Share the app",
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.size(20.dp))
            Icon(

                imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                contentDescription = "Logout image"
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.Support, contentDescription = "support image")
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Support",
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.size(20.dp))
            Icon(

                imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                contentDescription = "Logout image"
            )
        }

        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = "About image")
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "About App",
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.size(20.dp))
            Icon(

                imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                contentDescription = "Arrow"
            )
        }

        Spacer(modifier = Modifier.size(20.dp))
        HorizontalDivider(color = Color.LightGray)
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth().clickable { showLogoutDialog = true },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = "Arrow"
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "Logout",
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.size(20.dp))
            Icon(

                imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                contentDescription = "Arrow"
            )
        }

        if (showLogoutDialog) {
            LogoutDialog(
                onDismiss = { showLogoutDialog = false },
                onConfirm = {
                    showLogoutDialog = false
                    scope.launch {
//                        val signOut = googleAuthHelper.signOut(context)
//                        if (signOut) {
//                            SharedPrefManager.clear()
//                            logOut.invoke(true)
//                        }
                    }
                })
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ProfileScreen { b -> }
}