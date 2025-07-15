package com.example.bankapp.features.analysis.models

import com.example.bankapp.domain.model.Category
import com.example.bankapp.features.history.models.DateMode

sealed class AnalysisIntent {

    object onDismiss : AnalysisIntent()

    data class onUpdateDate(
        val mode: DateMode,
        val date: String
    ) : AnalysisIntent()
    data class setAnalysisType(
        val analysisType: Boolean
    ) : AnalysisIntent()



}