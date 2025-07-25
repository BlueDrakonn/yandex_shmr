package com.example.bankapp.features.settings.utils

import android.content.Context
import com.example.bankapp.BuildConfig
import com.example.bankapp.features.settings.models.AppInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getAppInfo(context: Context): AppInfo {
    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = packageManager.getPackageInfo(packageName, 0)

    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val updateDate = formatter.format(Date(packageInfo.lastUpdateTime))

    return AppInfo(
        versionName = BuildConfig.VERSION_NAME,
        versionCode = BuildConfig.VERSION_CODE,
        lastUpdate = updateDate
    )
}