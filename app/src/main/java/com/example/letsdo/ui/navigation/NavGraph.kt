package com.example.letsdo.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.letsdo.ui.screen.EntryDestination
import com.example.letsdo.ui.screen.EntryScreen
import com.example.letsdo.ui.screen.HomeDestination
import com.example.letsdo.ui.screen.HomeScreen
import com.example.letsdo.ui.screen.NoteDestination
import com.example.letsdo.ui.screen.NoteEntryDestination
import com.example.letsdo.ui.screen.NoteEntryScree
import com.example.letsdo.ui.screen.NoteScreen


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MemoLogApp(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//    Scaffold(
//        topBar = {
//            MediumTopAppBar(
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//                title = {
//                    Text(text = "All Notes",
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 36.sp,
//                    )}
//            )
//        }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = HomeDestination.route,
//            modifier = modifier.padding(innerPadding)
//        ) {
//            composable(route = HomeDestination.route) {
//                HomeScreen(
//                    navigateToEnterEntry = { navController.navigate(EntryDestination.route)},
//                    navigateToUpdateEntry =  {
//                        navController.navigate("${EntryDestination.route}/${it.toString()}")
//                    },
//                )
//            }
//            composable(
//                route = EntryDestination.route,
//            ) {
//                EntryScreen(
//                    navigateBack = { navController.navigate(HomeDestination.route) }
//                )
//            }
//        }
//    }
//}

data class BottomNavigationItem (
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    navController = navController
                )
            }
            composable(
                route = EntryDestination.route,
            ) {
                EntryScreen(navigateBack = {navController.navigate(HomeDestination.route)})
            }
            composable(
                route = NoteDestination.route
            ) {
                NoteScreen(
                    navigateToUpdateNotes = {},
                    navigateToWriteNote = {navController.navigate(NoteEntryDestination.route)},
                    modifier = Modifier,
                    navController = navController
                )
            }
            composable(
                route = "${NoteEntryDestination.route}/{it}",
                arguments = listOf(navArgument("it"){type = NavType.IntType})
            ) { backStackEntry ->
                val  noteId = backStackEntry.arguments?.getInt("it") ?: -1
                NoteEntryScree(
                    navigateBack = {navController.navigate(NoteDestination.route)},
                    modifier = Modifier,
                    id = noteId
                )
            }
        }


}


