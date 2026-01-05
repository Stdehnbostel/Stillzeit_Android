package com.example.stillzeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.stillzeit.data.AppDatabase
import com.example.stillzeit.ui.AddMealScreen
import com.example.stillzeit.ui.theme.StillzeitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        setContent {
            StillzeitTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AddMealScreen(
                            viewModel = viewModel(),
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}