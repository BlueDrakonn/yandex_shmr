package com.example.bankapp.ui.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bankapp.R
import com.example.bankapp.domain.viewmodel.MainViewModel
import com.example.bankapp.ui.screen.AccountsScreen
import com.example.bankapp.ui.screen.CategoriesScreen
import com.example.bankapp.ui.screen.ExpensesScreen
import com.example.bankapp.ui.screen.IncomeScreen
import com.example.bankapp.ui.screen.SettingsScreen
import com.example.bankapp.ui.theme.PrimaryGreen

enum class Screen(
    val route: String,
    val titleRes: Int,
    val buttonTitleRes: Int,
    val iconId: Int,
) {
    EXPENSES(
        "EXPENSES",
        R.string.expenses_title,
        R.string.expenses_button,
        R.drawable.expenses),
    INCOME(
        "INCOME",
        R.string.income_title,
        R.string.income_button,
        R.drawable.income),
    ACCOUNTS(
        "ACCOUNTS",
        R.string.accounts_title,
        R.string.accounts_button,
        R.drawable.accounts),
    ARTICLES(
        "ARTICLES",
        R.string.articles_title,
        R.string.articles_button,
        R.drawable.articles),
    SETTINGS(
        "SETTINGS",
        R.string.settings_title,
        R.string.settings_button,
        R.drawable.settings);
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: MainViewModel) {

    val navController = rememberNavController()

    val currentScreen = Screen.valueOf(
        navController.currentBackStackEntryAsState().value?.destination?.route ?: "EXPENSES"
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(currentScreen.titleRes),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                ),

                actions = { when (currentScreen) {
                    Screen.EXPENSES -> IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.history),
                            contentDescription = "history"
                        ) }
                    Screen.INCOME -> IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.history),
                            contentDescription = "history"
                        )
                    }
                    Screen.ACCOUNTS -> IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.edit),
                            contentDescription = "edit"
                        )
                    }
                    else -> Unit
                }

                }
            )

        },
        floatingActionButton = {when (currentScreen) {
            Screen.EXPENSES -> AddButton(onClick = {})
            Screen.INCOME -> AddButton(onClick = {})
            Screen.ACCOUNTS -> AddButton(onClick = {})
            else -> Unit
        }}
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.EXPENSES.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.EXPENSES.route) { ExpensesScreen(viewModel=viewModel) }
            composable(Screen.INCOME.route) { IncomeScreen(viewModel=viewModel) }
            composable(Screen.ACCOUNTS.route) { AccountsScreen(viewModel=viewModel) }
            composable(Screen.ARTICLES.route) { CategoriesScreen(viewModel=viewModel) }
            composable(Screen.SETTINGS.route) { SettingsScreen() }
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        RoundedPlusIcon()
    }
}

@Composable
fun RoundedPlusIcon() {
    Canvas(modifier = Modifier.size(15.56.dp)) {
        val strokeWidth = 2.dp.toPx()
        val cornerRadius = 2.dp.toPx()


        drawRoundRect(
            color = Color.White,
            topLeft = Offset(0f, size.height / 2 - strokeWidth / 2),
            size = Size(size.width, strokeWidth),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )


        drawRoundRect(
            color = Color.White,
            topLeft = Offset(size.width / 2 - strokeWidth / 2, 0f),
            size = Size(strokeWidth, size.height),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.EXPENSES,
        Screen.INCOME,
        Screen.ACCOUNTS,
        Screen.ARTICLES,
        Screen.SETTINGS
    )

    BottomAppBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "EXPENSES"

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            items.forEach { screen ->

                BottomNavigationItem(
                    iconId = screen.iconId,
                    buttonTitleRes = screen.buttonTitleRes,
                    selected = currentRoute == screen.route,
                    onClick = { navController.navigate(screen.route) }
                )
            }

        }

    }
}


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
                indication = null) {onClick()},
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
                        size =  size
                    ) },
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