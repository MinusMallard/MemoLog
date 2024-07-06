package com.example.letsdo.ui.screen


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.letsdo.R
import com.example.letsdo.data.Entry
import com.example.letsdo.ui.AppViewModelProvider
import com.example.letsdo.ui.navigation.BottomNavigationItem
import com.example.letsdo.ui.navigation.NavDestination
import com.example.letsdo.ui.theme.LetsDoTheme
import com.example.letsdo.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay


object HomeDestination: NavDestination {
    override val route: String = "home"
}

val items = listOf(
    BottomNavigationItem(
        title = "Tasks",
        selectedIcon = Icons.Filled.DateRange,
        unselectedIcon = Icons.Outlined.DateRange,
        route = HomeDestination.route
    ),
    BottomNavigationItem(
        title = "Notes",
        selectedIcon = Icons.Filled.Create,
        unselectedIcon = Icons.Outlined.Create,
        route = NoteDestination.route
    )
)



@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    navigateToEnterEntry: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
    selectedItemIndex: Int = 0
) {

    BackHandler(
        onBack = {

            navController.popBackStack()
        }
    )
    val coroutineScope = rememberCoroutineScope()
    val homeUiState by viewModel.homeUiState.collectAsState()
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
                    Text(text = "All Tasks",
                        fontFamily = crimsonFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 32.sp,
                        )},
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEnterEntry() },
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
        }else if (homeUiState.copy().entryList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
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
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                //item { Spacer(modifier = Modifier) }
                items (homeUiState.copy().entryList) { item->
                    Box(modifier = Modifier) {
                        Entry(
                            entry = item,
                            navigateToEdit = {
                                navController.navigate("${EntryDestination.route}/${it}")},
                            onMarked = { coroutineScope.launch {
                                viewModel.updateEntry(it)
                            }},
                            onDelete = {
                                coroutineScope.launch {
                                    viewModel.deleteEntry(it)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FullScreenLoadingIndicator(showLoading: Boolean) {
    if (showLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0f))
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

//@Composable
//fun EntriesList(
//    entryList: List<Entry>,
//    onRadioButtonClicked:(Entry) -> Unit,
//    onDeleteButton:(Entry) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    LazyColumn {
//        items(entryList) {it ->
//            Entry(entry = it,
//                onMarked = onRadioButtonClicked,
//                onDelete = onDeleteButton,
//                modifier = modifier
//            )
//        }
//    }
//}

//@Composable
//fun EntryCard(
//    entry: Entry,
//    onMarked:(Entry) -> Unit,
//    modifier: Modifier = Modifier,
//    onDelete:(Entry) -> Unit,
//) {
//    var expanded by rememberSavable { mutableStateOf(false) }
//    var cardSelect by rememberSavable { mutableStateOf(entry.marked)}
//    Card(
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        modifier = modifier
//            .fillMaxWidth()
//
//            .padding(8.dp)
//            .clickable(
//                enabled = !cardSelect,
//                onClick = {
//                    expanded = !expanded
//                }
//            )
//            .padding(4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (!cardSelect) {
//                MaterialTheme.colorScheme.primaryContainer
//            } else {
//                MaterialTheme.colorScheme.surfaceVariant
//            },
//            contentColor = if (!cardSelect) {
//                MaterialTheme.colorScheme.onPrimaryContainer
//            } else {
//                MaterialTheme.colorScheme.onSurfaceVariant
//            }
//            //containerColor = MaterialTheme.colorScheme.primaryContainer,
//            //contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//        )
//    ) {
//        Column {
//            Row(
//                modifier = modifier
//                    .padding(4.dp)
//                    ,
//                verticalAlignment = Alignment.CenterVertically,
//
//                ) {
//                RadioButton(
//                    selected = cardSelect,
//                    onClick = {
//                        cardSelect = !cardSelect
//                        onMarked(
//                            entry.copy(marked = cardSelect)
//                        )
//                        if (expanded == true){
//                            expanded = !expanded
//                        }
//                    }
//                )
//                if (!cardSelect) {
//                    Text(
//                        text = entry.title,
//                        style = MaterialTheme.typography.titleMedium,
//                        modifier = modifier
//                            .padding(4.dp)
//                            .weight(1f)
//                    )
//                } else (
//                        Text(
//                            text = buildAnnotatedString {
//                                withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
//                                    append(entry.title)
//                                }
//                            },
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = modifier
//                                .padding(4.dp)
//                                .weight(1f)
//
//                        )
//                )
//                IconButton(onClick = { onDelete(entry) },) {
//                    Icon(
//                        imageVector = Icons.Default.Delete,
//                        contentDescription = null
//                    )
//                }
//            }
//            AnimatedVisibility(visible = expanded) {
//                Column {
//                    Divider()
//                    Text(text = entry.description,
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = modifier.padding(16.dp)
//                    )
//                }
//            }
//        }
//
//    }
//}


@Composable
fun Entry(
    entry: Entry,
    onMarked:(Entry) -> Unit,
    modifier: Modifier = Modifier,
    onDelete:(Entry) -> Unit,
    navigateToEdit: (Int) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var cardSelect by rememberSaveable { mutableStateOf(entry.marked)}
    Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable(
                    enabled = !cardSelect,
                    onClick = {
                        expanded = !expanded
                    },
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                )
        ) {
            Row(
                modifier = modifier
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = cardSelect,
                    onClick = {
                        cardSelect = !cardSelect
                        onMarked(
                            entry.copy(marked = cardSelect)
                        )
                        if (expanded){
                            expanded = !expanded
                        }
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                if (entry.title != "" && entry.title.isNotBlank() && entry.title.isNotEmpty()) {
                    if (!cardSelect) {
                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier
                                .padding(4.dp)
                                .weight(1f),
                            fontSize = 18.sp,
                            fontFamily = crimsonFontFamily,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                                    append(entry.title)
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier
                                .padding(4.dp)
                                .weight(1f),
                            fontFamily = crimsonFontFamily,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp

                        )
                    }
                } else {
                    if (!cardSelect) {
                        Text(
                            text = entry.description,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = crimsonFontFamily,
                            modifier = modifier
                                .padding(4.dp)
                                .weight(1f),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    } else {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                                    append(entry.description)
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = crimsonFontFamily,
                            modifier = modifier
                                .padding(4.dp)
                                .weight(1f),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }
                }
                IconButton(
                    onClick = { navigateToEdit(entry.id) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                    ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { onDelete(entry) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                    )
                {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
            if (entry.description.isNotBlank() && entry.title.isNotBlank()) {
                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier.padding(start = 40.dp)
                    ) {
                        Text(text = entry.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            ThinWavyLine()
    }
}

@Composable
fun ThinWavyLine() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache {
                val path = Path()
                val amplitude = 10f // height of the wave
                val frequency = 0.1f // how many waves in the canvas width

                // Start at the middle-left of the canvas
                path.moveTo(0f, size.height / 2)

                // Draw the wavy line
                for (x in 0..size.width.toInt()) {
                    val y = kotlin.math.sin(x * frequency) * amplitude + size.height / 2
                    path.lineTo(x.toFloat(), y.toFloat())
                }
                onDrawBehind {
                    drawPath(path, Color.Gray, style = Stroke(width = 2f))
                }
            }
            .fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun EntryCardPreview(){
    LetsDoTheme {
        Entry(entry = Entry(id = 0,title = "do this", description =  "nothing", marked = false),
            onMarked = {}, onDelete = {}, navigateToEdit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    LetsDoTheme {
        HomeScreen(navigateToEnterEntry = {}, navController = rememberNavController())
    }
}






