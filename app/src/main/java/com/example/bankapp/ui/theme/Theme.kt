package com.example.bankapp.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkGreenColorScheme = darkColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,

    secondary = Color(0xFF66BB6A),
    onSecondary = Color.Black,

    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),

    surface = Color(0xFF1B1F1B),
    onSurface = Color(0xFFBDBDBD),

    error = Color(0xFFCF6679),
    onError = Color.Black
)


private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = ContainerHighGreen,
    background = SurfaceWhite,
    surface = SurfaceContainerGrey,
    onPrimary = TextBlack,
    onSecondary = TextGray,
    onBackground = TextBlack,
    onSurface = TextGray,
    error = Error,
    outlineVariant = DividerGrey,
    surfaceContainer = ContainerHighGrey,


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun BankAppTheme(
    darkTheme: Boolean,
    primaryColor: Int,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {


    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkGreenColorScheme
        else -> LightColorScheme
    }




    MaterialTheme(
        colorScheme = colorScheme.copy(primary =  Color(primaryColor)),
        typography = Typography,
        content = content
    )
}