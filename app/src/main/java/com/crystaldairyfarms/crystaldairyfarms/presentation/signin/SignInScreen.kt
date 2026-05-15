package com.crystaldairyfarms.crystaldairyfarms.presentation.signin

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crystaldairyfarms.crystaladmin.MainActivityAdmin
import com.crystaldairyfarms.crystaldairyfarms.R
import com.crystaldairyfarms.crystaldairyfarms.presentation.Space
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.AuthViewModel
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.UserState
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSignedIn: () -> Unit,
    onGuestContinue: () -> Unit,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val userState by authViewModel.userState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isLoading = userState is UserState.Loading

    // Request POST_NOTIFICATIONS permission on Android 13+
    val notifPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notifPermLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // React to auth state
    LaunchedEffect(userState) {
        when (val state = userState) {
            is UserState.SignedIn -> onSignedIn()
            is UserState.Guest -> onGuestContinue()
            is UserState.Error -> scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                authViewModel.clearError()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
                    .clickable {
                        context.startActivity(Intent(context, MainActivityAdmin::class.java))
                    },
                text = "Login Admin",
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.app_logo1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 50.dp)
                        .clip(CircleShape)
                )
                Space(20.dp)
                Text(
                    text = "Dairy & Bakery",
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingFromBaseline(20.dp),
                    textAlign = TextAlign.Center,
                    color = Primary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Space(8.dp)
                Text(
                    text = "Fresh from farm to your door",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 40.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Google Sign-In
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isLoading) Primary.copy(alpha = 0.7f) else Primary)
                        .clickable(enabled = !isLoading) {
                            authViewModel.signInWithGoogle(context)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.google_logo),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "Continue with Google",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.size(28.dp))
                        }
                    }
                }

                // Continue as Guest
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFEEF7EE))
                        .clickable(enabled = !isLoading) {
                            authViewModel.continueAsGuest()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue as Guest",
                        color = Primary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "Guest accounts can browse but need to sign in to place orders.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    // Preview with a stub — authViewModel cannot be injected in preview
}