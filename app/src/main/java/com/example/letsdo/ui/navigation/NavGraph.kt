package com.example.letsdo.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.letsdo.ui.screen.EntryDestination
import com.example.letsdo.ui.screen.EntryScreen
import com.example.letsdo.ui.screen.HomeDestination
import com.example.letsdo.ui.screen.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoLogApp(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "All Notes",
                        fontWeight = FontWeight.Medium,
                        fontSize = 36.sp,
                    )}
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = HomeDestination.route) {
                HomeScreen(
                    navigateToEnterEntry = { navController.navigate(EntryDestination.route)},
                    navigateToUpdateEntry =  {
                        navController.navigate("${EntryDestination.route}/${it.toString()}")
                    },
                )
            }
            composable(
                route = EntryDestination.route,
            ) {
                EntryScreen(
                    navigateBack = { navController.navigate(HomeDestination.route) }
                )
            }
        }
    }
}
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToEnterEntry = { navController.navigate(EntryDestination.route)},
                navigateToUpdateEntry =  {
                    navController.navigate("${EntryDestination.route}/${it.toString()}")
                },
            )
        }
        composable(
            route = EntryDestination.route,
        ) {
            EntryScreen(navigateBack = {navController.navigate(HomeDestination.route)})
        }
    }
}
