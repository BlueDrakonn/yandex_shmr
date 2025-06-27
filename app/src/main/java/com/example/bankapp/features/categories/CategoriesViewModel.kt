package com.example.bankapp.features.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _categoryState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    val categoryState = _categoryState
        .onStart {
            loadCategories()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ResultState.Loading
        )

    private fun loadCategories() {

        viewModelScope.launch {
            _categoryState.value = categoryRepository.loadCategories()
        }
    }

}