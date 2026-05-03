package com.crystaldairyfarms.crystaldairyfarms.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary


@Composable
fun Space(size: Dp) {
    Spacer(modifier = Modifier.size(size))
}

@Composable
fun LoadingDialog(showDialog: Boolean) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { /* Prevent dismiss while loading */ },
            confirmButton = {}, // No confirm button while loading
            text = {
                CommonProgress()
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CommonProgress() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Loading please wait..",
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)
        Space(20.dp)
        LinearWavyProgressIndicator(
            color = Primary
        )
    }
    // })
}

@Composable
fun ErrorDialog(
    onRetry: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Error") },
        text = { Text("Oops something went wrong..") },
        confirmButton = {
            Button(onClick = onRetry) { Text("Retry") }
        }
    )
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Logout",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = "Are you sure you want to logout?",
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    text = "Yes",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = "No",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
