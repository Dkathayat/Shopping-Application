package com.crystaldairyfarms.crystaldairyfarms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.NavigationGraph
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.CrystalDairyfarmsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrystalDairyfarmsTheme {
                NavigationGraph()
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        CrystalDairyfarmsTheme {
           // NavigationGraph()
        }
    }
}