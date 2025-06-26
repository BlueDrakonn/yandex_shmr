package com.example.bankapp.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun BottomNavigationItem(
    iconId: Int,
    buttonTitleRes: Int ,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondary
    }

    Column(
        modifier = Modifier
            .size(width = 72.8.dp, height = 80.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        if (selected) {

            val secondaryColor = MaterialTheme.colorScheme.secondary
            Box(
                modifier = Modifier
                    .size(width = 64.dp, height = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .drawBehind {
                        drawRect(
                            color = secondaryColor,
                            size = size
                        )
                    },
                contentAlignment = Alignment.Center


            ) {
                Icon(
                    painter = painterResource(iconId),
                    contentDescription = stringResource(buttonTitleRes),
                    modifier = Modifier
                        .size(width = 32.dp, height = 32.dp),
                    tint = MaterialTheme.colorScheme.primary

                )
            }

        } else {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(buttonTitleRes),
                modifier = Modifier
                    .size(width = 32.dp, height = 32.dp),
            )
        }

        Text(
            text = stringResource(buttonTitleRes),
            color = textColor,
            style = MaterialTheme.typography.labelMedium
        )

    }

}