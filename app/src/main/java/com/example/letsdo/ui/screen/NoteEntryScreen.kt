package com.example.letsdo.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.letsdo.data.Note
import com.example.letsdo.ui.AppViewModelProvider
import com.example.letsdo.ui.navigation.NavDestination
import com.example.letsdo.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


object NoteEntryDestination: NavDestination {
    override val route = "note_entry"
}
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
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .imePadding(),

    ) {

        Column {
//        Row(modifier = modifier
//            .padding(4.dp)
//            .fillMaxWidth(),
//            horizontalArrangement = Arrangement.End
//        ) {
//            IconButton(
//                onClick = { navigateBack() },
//                colors = IconButtonDefaults.iconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            ){
//                Icon(imageVector = Icons.Default.Close, contentDescription = null)
//            }
//            IconButton(onClick = {
//                                 coroutineScope.launch {
//                                     viewModel.addEntry()
//                                     navigateBack()
//                                 }
//                },
//                colors = IconButtonDefaults.iconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                ),
//                enabled = viewModel.validateInput(viewModel.uiState)
//                ) {
//                Icon(imageVector = Icons.Default.Check, contentDescription = null)
//            }
//        }
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
                    fontFamily = crimsonFontFamily
                    ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
                    .height(120.dp)

                    .align(Alignment.CenterHorizontally),
                placeholder = {
                    Text(
                        text = "title",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),

                keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary
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
                    ) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        navigateBack()
                        if (viewModel.validateInput()) {
                            viewModel.addNote()
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