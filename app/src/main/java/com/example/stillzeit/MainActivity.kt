package com.example.stillzeit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.stillzeit.data.AppDatabase
import com.example.stillzeit.ui.AddMealScreen
import com.example.stillzeit.ui.AddMealViewModel
import com.example.stillzeit.ui.Destination
import com.example.stillzeit.ui.MealDetailScreen
import com.example.stillzeit.ui.MealDetailViewModel
import com.example.stillzeit.ui.ProtocolScreen
import com.example.stillzeit.ui.ProtocolViewModel
import com.example.stillzeit.ui.theme.StillzeitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val startDestination = Destination.ADD_MEALS

    val currentDestination = Destination.entries.find { it.route == currentRoute }
        ?: startDestination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = Color.Unspecified
                ),
                title = {
                    Text(
                        text = stringResource(currentDestination.titleRes),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                Destination.entries.filter { it.showInNavigationBar }.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(route = destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(
                                text = stringResource(destination.label),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        icon = {
                            destination.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = destination.contentDescription
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(navController, startDestination, modifier.padding(innerPadding))
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
        Destination.entries.filter { it.showInNavigationBar }.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.ADD_MEALS -> AddMealScreen(
                        viewModel = viewModel(factory = AddMealViewModel.Factory),
                        modifier = modifier
                    )
                    Destination.PROTOCOL -> ProtocolScreen(
                        viewModel = viewModel(factory = ProtocolViewModel.Factory),
                        modifier = modifier,
                        onMealClick = { mealId -> navController.navigate("meal_detail/$mealId") }
                    )
                    // Hier sollte wahrscheinlich noch eine Error-Ausgabe oder dergleichen hin.
                    else -> { }
                }
            }
        }
        composable(
            route = Destination.MEAL_DETAILS.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) {
                backStackEntry ->
            val mealId = requireNotNull(backStackEntry.arguments?.getString("mealId"))
            MealDetailScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel(factory = MealDetailViewModel.Factory),
                modifier = modifier
            )
        }
    }
}
