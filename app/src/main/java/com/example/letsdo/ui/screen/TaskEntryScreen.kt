package com.example.letsdo.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letsdo.ui.AppViewModelProvider
import com.example.letsdo.ui.navigation.NavDestination
import com.example.letsdo.ui.theme.LetsDoTheme
import com.example.letsdo.ui.viewmodel.EntryScreenViewModel



object EntryDestination : NavDestination {
    override val route = "entry_details"
}

@Composable
fun EntryScreen(
    navigateBack:() -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    BackHandler(onBack = { navigateBack()
        if (viewModel.validateInput()) {
            viewModel.addEntry()
        }
        }
    )
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primaryContainer)) {
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
                    viewModel.updateUiStateTitle(it)
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,

                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(120.dp)

                    .align(Alignment.CenterHorizontally),
                placeholder = {
                    Text(text = "title",
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
                onValueChange = { viewModel.updateUistateDesc(it)},
                modifier = modifier
                    .fillMaxSize().focusRequester(focusRequester),
                placeholder = {Text(text = "Start writing something......")},
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




@Preview (showBackground =  true)
@Composable
fun EntryScreenPreview() {
    LetsDoTheme {
        EntryScreen(navigateBack =  {})
    }
}