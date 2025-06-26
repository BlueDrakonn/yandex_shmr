package com.example.bankapp.data.repository

import android.util.Log
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.viewmodel.ResultState
import com.example.bankapp.utils.safeApiCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryRepositoryImpl(private  val apiService: ApiService):CategoryRepository {

    private val _categoryState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    override val categoryState: StateFlow<ResultState<List<Category>>> = _categoryState

    override suspend fun loadCategories(){

        when (_categoryState.value) {
            ResultState.Loading -> {


                var result = safeApiCall(
                    mapper = { it },
                    block = { apiService.getCategories()  }
                )

                if(result == ResultState.Error(message = "no internet connection")) {
                    delay(7000)
                    _categoryState.value = result
                    result = safeApiCall(
                        mapper = { it },
                        block = { apiService.getCategories()  }
                    )

                }

                when(result) {

                    is ResultState.Success -> {
                        //result = result.data.map { it.toAccount() }
                        _categoryState.value = ResultState.Success(result.data)

                    }
                    else -> _categoryState.value = result
                }
            }
            else -> {}
        }
    }
}