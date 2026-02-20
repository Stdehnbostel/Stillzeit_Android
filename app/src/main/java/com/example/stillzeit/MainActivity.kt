package com.example.stillzeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.Room
import com.example.stillzeit.data.AppDatabase
import com.example.stillzeit.ui.AddMealScreen
import com.example.stillzeit.ui.Destination
import com.example.stillzeit.ui.ProtocolScreen
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
                    NavigationTabs()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTabs(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    var startDestination = Destination.ADD_MEALS
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        PrimaryTabRow(selectedTabIndex = selectedDestination, modifier = Modifier.padding(innerPadding)) {
            Destination.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedDestination == index,
                    onClick = {
                        navController.navigate(route = destination.route)
                        selectedDestination = index
                    },
                    text = {
                        Text(
                            text = destination.label,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        AppNavHost(navController, startDestination)
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.ADD_MEALS -> AddMealScreen(
                        viewModel = viewModel(),
                        modifier = modifier
                    )
                    Destination.PROTOCOL -> ProtocolScreen()
                }
            }
        }
    }
}
