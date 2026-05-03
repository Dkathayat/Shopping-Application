package com.crystaldairyfarms.crystaldairyfarms.presentation.uicomp

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CartIconButton(
    cartCount: Int,
    cartItem:() -> Unit
) {
    BadgedBox(
        modifier = Modifier.wrapContentSize(),
        badge = {
            Badge {
                Text(cartCount.toString())
            }
        }
    ) {
        IconButton(onClick = { cartItem.invoke() }) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}