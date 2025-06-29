package com.example.bankapp.features.categories


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.Constants.Delays
import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.features.categories.store.models.CategoryIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categoryState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    private var _allCategories = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    val categoryState = _categoryState
        .onStart {
            loadCategories()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(Delays.STOP_TIMEOUT_MILES),
            ResultState.Loading
        )

    private fun loadCategories() {

        viewModelScope.launch {
            val result = categoryRepository.loadCategories()
            _categoryState.value = result
            _allCategories.value = result
        }
    }

    fun handleIntent(intent: CategoryIntent) {
        when (intent) {
            is CategoryIntent.OnSearchQuery -> {
                sortCategories(intent.text)
            }
        }
    }

    private fun sortCategories(prefix: String) {
        val currentState = _allCategories.value

        if (currentState is ResultState.Success) {

            val filtered = currentState.data.filter { category ->
                category.name.startsWith(prefix, true)
            }
            _categoryState.value = ResultState.Success(data = filtered)
        }

    }

}