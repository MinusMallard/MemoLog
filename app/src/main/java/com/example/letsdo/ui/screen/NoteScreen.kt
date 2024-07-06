package com.example.letsdo.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letsdo.R
import com.example.letsdo.data.Note
import com.example.letsdo.ui.AppViewModelProvider
import com.example.letsdo.ui.navigation.NavDestination
import com.example.letsdo.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val crimsonFontFamily = FontFamily(
    Font(R.font.crimsontext_regular,FontWeight.Normal),
    Font(R.font.crimsontext_semibold,FontWeight.SemiBold),
    Font(R.font.crimson_bold,FontWeight.Bold)
)

object NoteDestination: NavDestination {
    override val route: String = "note"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController,
    selectedItemIndex: Int = 1
) {

    BackHandler(
        onBack = {

            navController.popBackStack()
        }
    )
    val coroutineScope = rememberCoroutineScope()
    val noteUiState by viewModel.noteUiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,

                ),
                title = {
                    Text(text = "All Notes",
                        fontWeight = FontWeight.Medium,
                        fontSize = 36.sp,
                        fontFamily = crimsonFontFamily
                    )
                },
                scrollBehavior = scrollBehavior
                )
        },
        floatingActionButton = {
            FloatingActionButton(

                onClick = {
                    val it = -1
                    navController.navigate("${NoteEntryDestination.route}/${it}") },
                shape = RoundedCornerShape(56.dp)
            )
            {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_button),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = item.title)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.primary,
                            disabledIconColor = MaterialTheme.colorScheme.error,
                            disabledTextColor = MaterialTheme.colorScheme.error
                        )
                    )
                }

            }
        }
    ) { innerPadding ->
        var showLoading by rememberSaveable { mutableStateOf(true) }
        if (showLoading) {
            LaunchedEffect(key1 = showLoading) {
                if (showLoading) {
                    delay(250) // Delay for 2 seconds
                    showLoading = false
                }
            }
            FullScreenLoadingIndicator(showLoading)
        } else if (noteUiState.copy().noteList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_notes),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                ,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = crimsonFontFamily,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp
            )
        } else {
            LazyVerticalStaggeredGrid(

                modifier = Modifier.padding(innerPadding),
                columns = StaggeredGridCells.Adaptive(120.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                userScrollEnabled = true
            )
            {
                items (noteUiState.copy().noteList) { item->
                    print(item.title)
                    Box(modifier = Modifier) {
                        NotesCard(
                            note = item,
                            onDelete = {
                                coroutineScope.launch {
                                    viewModel.deleteNote(it)
                                }
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotesCard(
    note: Note,
    modifier: Modifier = Modifier,
    onDelete:(Note) -> Unit,
    navController: NavHostController
    ) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
       modifier = Modifier.clickable(
           onClick = {
                navController.navigate("${NoteEntryDestination.route}/${note.id}")
           }
       )
    ) {

        Box(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (note.title != "" && note.title.isNotBlank() && note.title.isNotEmpty()) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier
                            .padding(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = crimsonFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                } else {
                    Text(
                        text = note.description,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(4.dp)
                        ,
                        maxLines = 10,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = crimsonFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                }
                IconButton(onClick = { onDelete(note) }) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        tint = Color.Red,
                        contentDescription = null)
                }
            }
        }
    }
}





//    var expanded by rememberSaveable { mutableStateOf(false) }
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(4.dp)
//            .clickable(
//                onClick = {
//                    expanded = !expanded
//                }
//            )
//    ) {
//        Row(
//            modifier = modifier
//                .padding(4.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            if (note.title != "" && note.title.isNotBlank() && note.title.isNotEmpty()) {
//                Text (
//                    text = note.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = modifier
//                        .padding(4.dp)
//                        .weight(1f)
//                )
//            } else {
//                Text (
//                    text = note.description,
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .weight(1f),
//                    maxLines = 4,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//
//            IconButton(onClick = { onDelete(note) },) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = null
//                )
//            }
//        }
//        if (note.description.isNotBlank() && note.title.isNotBlank()) {
//            AnimatedVisibility(visible = expanded) {
//                Column(
//                    modifier = Modifier.padding(start = 40.dp)
//                ) {
//                    Text(text = note.description,
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = modifier.padding(16.dp)
//                    )
//                }
//            }
//        }
//        Divider(Modifier.fillMaxWidth())
//    }