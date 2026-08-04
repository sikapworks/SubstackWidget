package uk.ac.tees.mad.substackwidget.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SubstackOrange,
    onPrimary = Color.White,
    background = SubstackCream,
    surface = SubstackCream,
    onBackground = SubstackDarkText,
    onSurface = SubstackDarkText,
    secondary = SubstackGrayText
)

private val DarkColorScheme = darkColorScheme(
    primary = SubstackOrangeDark,
    onPrimary = SubstackDarkBackground,
    background = SubstackDarkBackground,
    surface = SubstackDarkSurface,
    onBackground = SubstackLightText,
    onSurface = SubstackLightText,
    secondary = SubstackGrayTextDark
)

@Composable
fun SubstackWidgetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}