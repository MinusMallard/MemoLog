package com.example.letsdo.ui.screen

import android.os.Build
import android.view.WindowInsets
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letsdo.ui.AppViewModelProvider
import com.example.letsdo.ui.navigation.NavDestination
import com.example.letsdo.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.launch


object NoteEntryDestination: NavDestination {
    override val route = "note_entry"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEntryScree(
    viewModel: NoteViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack:()-> Unit,
    modifier: Modifier,
    id : Int = -1,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    if (id != -1) {
        LaunchedEffect(key1 = id) {
            viewModel.loadNote(id)
        }
    }

    BackHandler(onBack = {
        if (viewModel.validateInput() && id == -1) {
            viewModel.addNote()
        } else {
            coroutineScope.launch {
                viewModel.updateNote(viewModel.uiState)
            }
        }
        navController.popBackStack()
        }
    )
    Box(
        modifier = Modifier
            .padding(windowInsets.asPaddingValues())
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .imePadding(),

    ) {
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
                    viewModel.updateUiStateTitle(
                        it
                    )
                },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = crimsonFontFamily,
                    color = MaterialTheme.colorScheme.primary
                    ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .height(120.dp)
                    .align(Alignment.CenterHorizontally),
                placeholder = {
                    Text(
                        text = "title",
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
                onValueChange = {
                    viewModel.updateUiStateDescription(it)
                },
                modifier = modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = "Start writing something......",
                        fontFamily = crimsonFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    ) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        navigateBack()
                        if (viewModel.validateInput() && id == -1) {
                            viewModel.addNote()
                        } else {
                            coroutineScope.launch {
                                viewModel.updateNote(viewModel.uiState)
                            }
                        }
                    }
                ),
                textStyle = TextStyle(
                    fontFamily = crimsonFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
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