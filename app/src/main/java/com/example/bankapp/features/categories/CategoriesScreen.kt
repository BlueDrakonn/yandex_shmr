package com.example.bankapp.features.categories

import ListItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.categories.store.models.CategoryIntent
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.LeadIcon
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.SearchInput
import com.example.bankapp.navigation.TopAppBar


@Composable
fun CategoriesScreen(

) {
    val viewModel: CategoriesViewModel = viewModel(factory = LocalViewModelFactory.current)

    val state by viewModel.categoryState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Screen.ARTICLES.titleRes)
            )
        }
    ) { padding ->
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
            },
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        )
    }


}


