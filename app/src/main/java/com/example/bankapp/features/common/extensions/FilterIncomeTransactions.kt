package com.example.bankapp.features.common.extensions

import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.IncomeInfo

fun<T: IncomeInfo> ResultState<List<T>>.filterIncome(): ResultState<List<T>> =

    when (this) {
        is ResultState.Success -> ResultState.Success(this.data.filter { it.isIncome })
        else -> this
    }