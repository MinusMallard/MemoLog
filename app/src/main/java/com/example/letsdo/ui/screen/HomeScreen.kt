package com.example.letsdo.ui.screen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
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
import com.example.letsdo.R
import com.example.letsdo.data.Entry
import com.example.letsdo.ui.AppViewModelProvider
import com.example.letsdo.ui.navigation.NavDestination
import com.example.letsdo.ui.theme.LetsDoTheme
import com.example.letsdo.ui.viewmodel.HomeUiState
import com.example.letsdo.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch


object HomeDestination: NavDestination {
    override val route: String = "home"
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    navigateToUpdateEntry: (Int) -> Unit,
    navigateToEnterEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
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
                        )},
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ },

                        ){
                        Icon(imageVector = Icons.Rounded.Search, contentDescription ="search screen")
                    }
                },

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
    ) { innerPadding ->
        if (homeUiState.copy().entryList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                ,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn(modifier = Modifier) {
                item { Spacer(modifier = Modifier.padding(innerPadding)) }
                items (homeUiState.copy().entryList) { item->
                    Box(modifier = Modifier) {
                        Entry(
                            entry = item,
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
                    }
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
                        if (expanded == true){
                            expanded = !expanded
                        }
                    }
                )
                if (entry.title != "" && entry.title.isNotBlank() && entry.title.isNotEmpty()) {
                    if (!cardSelect) {
                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier
                                .padding(4.dp)
                                .weight(1f)
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
                                .weight(1f)

                        )
                    }
                } else {
                    if (!cardSelect) {
                        Text(
                            text = entry.description,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier
                                .padding(4.dp)
                                .weight(1f),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
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
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = { onDelete(entry) },) {
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
                            modifier = modifier.padding(16.dp)
                        )
                    }
                }
            }

            Divider(Modifier.fillMaxWidth())
    }
}


//@Composable
//fun SearchAction(
//    modifier: Modifier = Modifier
//) {
//    var newEntry by rememberSavable {
//        mutableStateOf("")
//    }
//    Box {
//        OutlinedTextField(
//            maxLines = 1,
//            value = newEntry,
//            onValueChange = {
//                newEntry = it
//            },
//            modifier = modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            shape = RoundedCornerShape(10.dp),
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Outlined.Search,
//                    tint = MaterialTheme.colorScheme.primary,
//                    contentDescription = null
//                )
//            },
//            placeholder = {
//                Text(text = "search",color = MaterialTheme.colorScheme.primary)
//            },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                unfocusedBorderColor = MaterialTheme.colorScheme.primary
//            )
//        )
//    }
//}


@Preview(showBackground = true)
@Composable
fun EntryCardPreview(){
    LetsDoTheme {
        Entry(entry = Entry(id = 0,title = "do this", description =  "nothing", marked = false),
            onMarked = {}, onDelete = {}
        )
    }
}



@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    LetsDoTheme {
        HomeScreen(navigateToEnterEntry = {}, navigateToUpdateEntry = {})
    }
}






