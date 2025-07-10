package com.example.bankapp.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.bankapp.R

enum class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    @StringRes val buttonTitleRes: Int? = null,
    @DrawableRes val iconId: Int? = null,
) {
    EXPENSES(
        "EXPENSES",
        R.string.expenses_title,
        R.string.expenses_button,
        R.drawable.expenses
    ),
    INCOME(
        "INCOME",
        R.string.income_title,
        R.string.income_button,
        R.drawable.income
    ),
    ACCOUNTS(
        "ACCOUNTS",
        R.string.accounts_title,
        R.string.accounts_button,
        R.drawable.accounts
    ),
    ACCOUNTS_EDIT(
        "ACCOUNTS_EDIT",
        R.string.accounts_title,
    ),
    ARTICLES(
        "ARTICLES",
        R.string.articles_title,
        R.string.articles_button,
        R.drawable.articles
    ),
    SETTINGS(
        "SETTINGS",
        R.string.settings_title,
        R.string.settings_button,
        R.drawable.settings
    ),
    HISTORY_EXPENSES(
        "HISTORY_EXPENSES",
        R.string.history_title,
    ),
    HISTORY_INCOME(
        "HISTORY_INCOME",
        R.string.history_title,
    ),
    TRANSACTION_ADD(
        "TRANSACTION_ADD",
        R.string.expenses_title
    ),
    TRANSACTION_EDIT(
        "TRANSACTION_EDIT",
        R.string.expenses_title
    ),
    ;

    companion object {
        val all = listOf(
            EXPENSES,
            INCOME,
            ACCOUNTS,
            ARTICLES,
            SETTINGS
        )

    }

}