package com.example.bankapp.features.categories

import ListItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bankapp.features.categories.store.models.CategoryIntent
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.LeadIcon
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.SearchInput


@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel()
) {

    val state by viewModel.categoryState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    ResultStateHandler(
        state = state,
        onSuccess = { data ->
            LazyList(
                topItem = {
                    SearchInput(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearchClick = {
                            viewModel.handleIntent(CategoryIntent.OnSearchQuery(searchQuery))
                        }
                    )
                },
                itemsList = data,
                itemTemplate = { item ->
                    ListItem(
                        modifier = Modifier.height(68.dp),
                        lead = { LeadIcon(label = item.emoji) },
                        content = {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )

                            }
                        }
                    )
                }
            )
        }
    )

}


