package com.crystaldairyfarms.crystaldairyfarms.presentation.signin

import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crystaldairyfarms.crystaladmin.MainActivity
import com.crystaldairyfarms.crystaldairyfarms.R
import com.crystaldairyfarms.crystaldairyfarms.presentation.Space
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onGoogleSignInClick: () -> Unit,
    onEmailSignInClick: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    //val googleAuthHelper = remember { GoogleAuthUiHelper(context) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
                .clickable {
                    context.startActivity(Intent(context, MainActivity::class.java))
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
                // fontFamily = FontFamily(Font(R.font.alan_sans)),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 50.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Primary)
                    .clickable {
                        onGoogleSignInClick.invoke()
//                        coroutineScope.launch {
//                            val result = googleAuthHelper.signInGoogle()
//                            result.onSuccess { user ->
//                                onGoogleSignInClick()
//                                Log.d("SignIn", "User signed in: $user")
//                                // navigate to home
//                            }.onFailure { e ->
//                                Toast.makeText(context, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
//                            }
//                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = "Google-Sign-In", color = Color.White)
                    Spacer(modifier = Modifier.size(0.dp))

                }
            }
            Text(
                text = "Sign in using email",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onEmailSignInClick.invoke() }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    SignInScreen(
        onGoogleSignInClick = {},
        onEmailSignInClick = {}
    )
}