package com.steadycash.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.steadycash.app.navigation.SteadyCashNavHost
import com.steadycash.app.ui.theme.SteadyCashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SteadyCashTheme {
                SteadyCashNavHost()
            }
        }
    }
}
