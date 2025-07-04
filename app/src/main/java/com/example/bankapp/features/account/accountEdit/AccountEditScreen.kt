package com.example.bankapp.features.account.accountEdit

import ListItem
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.ResultState
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.features.account.accountEdit.models.AccountEditIntent
import com.example.bankapp.features.common.ui.CurrencyBottomSheet
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent
import com.example.bankapp.navigation.TopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AccountEditScreen(
    viewModel: AccountEditViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by viewModel.accountState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }

    var initialized by remember { mutableStateOf(false) }
    var editableName by remember { mutableStateOf("") }
    var editableBalance by remember { mutableStateOf("") }
    var editableCurrency by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "back"
                        )
                    }
                },
                title = stringResource(Screen.ACCOUNTS_EDIT.titleRes)
            ) {
                IconButton(onClick = {


                    coroutineScope.launch(Dispatchers.IO) {
                        val result = viewModel.handleIntent(
                            AccountEditIntent.OnAccountUpdate(
                                name = editableName,
                                balance = editableBalance,
                                currency = editableCurrency
                            )
                        )
                        when (result) {
                            is ResultState.Success -> {
                                navController.popBackStack()
                            }

                            is ResultState.Error -> {
                                showToast(context, result.message)

                            }

                            else -> Unit
                        }
                    }


                }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "confirm"
                    )
                }
            }
        }) { padding ->
        ResultStateHandler(
            state = state,
            onSuccess = { data ->

                if (!initialized) {
                    editableName = data.firstOrNull()?.name ?: ""
                    editableBalance = data.firstOrNull()?.balance ?: ""
                    editableCurrency = data.firstOrNull()?.currency ?: ""
                    initialized = true
                }

                if (showBottomSheet) {
                    CurrencyBottomSheet(
                        { editableCurrency = it }
                    ) { showBottomSheet = false }
                }

                AccountEditForm(
                    editableName = editableName,
                    onNameChange = { editableName = it },
                    editableBalance = editableBalance,
                    onBalanceChange = { editableBalance = it },
                    editableCurrency = editableCurrency,
                    onCurrencyClick = { showBottomSheet = true },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )

            }, modifier = Modifier.padding(top = padding.calculateTopPadding())

        )
    }
}

@Composable
fun AccountEditForm(
    editableName: String,
    onNameChange: (String) -> Unit,
    editableBalance: String,
    onBalanceChange: (String) -> Unit,
    editableCurrency: String,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = editableName,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.account_edit_account_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editableBalance,
            onValueChange = onBalanceChange,
            label = { Text(stringResource(R.string.account_edit_balance)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        ListItem(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .clickable { onCurrencyClick() },
            content = {
                Text(
                    text = stringResource(R.string.currency),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                TrailingContent(
                    content = {
                        Text(
                            text = when (editableCurrency) {
                                "RUB" -> "\u20BD" // ₽
                                "USD" -> "\u0024" // $
                                "EUR" -> "\u20AC" // €
                                else -> editableCurrency
                            },
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.drillin),
                            contentDescription = null,
                        )
                    },
                )
            }
        )
    }
}

fun showToast(context: Context, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
