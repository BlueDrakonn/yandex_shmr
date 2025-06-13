package com.example.bankapp.ui.common


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TrailingContent(
    content: @Composable () -> Unit,
    icon: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
        Spacer(Modifier.width(16.dp))
        icon?.let {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                it()
            }
        }
    }
}