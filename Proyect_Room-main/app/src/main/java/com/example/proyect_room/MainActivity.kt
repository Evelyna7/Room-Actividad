package com.example.proyect_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.proyect_room.ui.theme.Proyect_RoomTheme
import com.example.proyect_room.ui.user.UserScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity:
 * - Es el "host" de nuestra UI en Compose.
 * - Está anotada con @AndroidEntryPoint para que Hilt pueda inyectar
 *   dependencias en esta Activity (o en los composables que cuelgan de ella).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Proyect_RoomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserScreen()
                }
            }
        }
    }
}
