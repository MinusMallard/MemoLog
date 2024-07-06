package com.example.letsdo.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.windowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letsdo.ui.AppViewModelProvider
import com.example.letsdo.ui.navigation.NavDestination
import com.example.letsdo.ui.theme.LetsDoTheme
import com.example.letsdo.ui.viewmodel.EntryScreenViewModel



object EntryDestination : NavDestination {
    override val route = "entry_details"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    navigateBack:() -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    id: Int,
    navController: NavHostController
) {

    BackHandler(
        onBack = {
            if (viewModel.validateInput()) {
                viewModel.addEntry()
            }
            navController.popBackStack()
        }
    )
    if (id != -1) {
        LaunchedEffect(Unit) {
            viewModel.loadEntry(id)
        }
    } else {
        LaunchedEffect(Unit) {
            viewModel.updateUiStateId(id)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier
        .padding(windowInsets.asPaddingValues())
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primaryContainer)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            val focusRequester = remember {
                FocusRequester()
            }
            OutlinedTextField(
                value = viewModel.uiState.title,
                onValueChange = {
                    viewModel.updateUiStateTitle(it)
                },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = crimsonFontFamily,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = modifier
                    .fillMaxWidth().padding(bottom = 4.dp)
                    .height(120.dp)

                    .align(Alignment.CenterHorizontally),
                placeholder = {
                    Text(text = "title",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = crimsonFontFamily
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),

                keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                ),

            )
            OutlinedTextField(
                value = viewModel.uiState.description,
                onValueChange = { viewModel.updateUiStateDesc(it)},
                modifier = modifier
                    .fillMaxSize().focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = "Start writing something......",
                        fontFamily = crimsonFontFamily,
                        color = MaterialTheme.colorScheme.primary,
                    ) },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = crimsonFontFamily,
                    color = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        navigateBack()
                        if (viewModel.validateInput()) {
                            viewModel.addEntry()
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    }
}
