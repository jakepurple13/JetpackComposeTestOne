package com.programmersbox.jetpackcomposetestone.ui

import androidx.compose.animation.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
        primary = purple200,
        primaryVariant = purple700,
        secondary = teal200
)

private val LightColorPalette = lightColors(
        primary = purple500,
        primaryVariant = purple700,
        secondary = teal200

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun JetpackComposeTestOneTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    /*val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
    )*/

    val uiMode = remember { mutableStateOf(if (darkTheme) UiMode.Dark else UiMode.Default) }
    Providers(UiModeAmbient provides uiMode) {
        val colors = when (UiModeAmbient.current.value) {
            UiMode.Default -> LightColorPalette
            UiMode.Dark -> DarkColorPalette
        }

        MaterialTheme(
            colors = animate(colors),
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

enum class UiMode {
    Default,
    Dark;

    fun toggle(): UiMode = when (this) {
        Default -> Dark
        Dark -> Default
    }
}

val UiModeAmbient = staticAmbientOf<MutableState<UiMode>>()

/*@Composable
fun AppTheme(content: @Composable () -> Unit) {
    // val isSystemDark = isSystemInDarkTheme()
    val isSystemDark = false
    val uiMode = remember {
        mutableStateOf(if (isSystemDark) UiMode.Dark else UiMode.Default)
    }
    Providers(UiModeAmbient provides uiMode) {
        val colors = when (UiModeAmbient.current.value) {
            UiMode.Default -> LightColorPalette
            UiMode.Dark -> DarkColorPalette
        }

        MaterialTheme(
            colors = animate(colors),
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}*/

@Composable
private fun animate(colors: Colors): Colors {
    val animSpec = remember { spring<Color>(stiffness = 500f) }

    @Composable
    fun animateColor(color: Color): Color = animate(target = color, animSpec = animSpec)

    return Colors(
        primary = animateColor(colors.primary),
        primaryVariant = animateColor(colors.primaryVariant),
        secondary = animateColor(colors.secondary),
        secondaryVariant = animateColor(colors.secondaryVariant),
        background = animateColor(colors.background),
        surface = animateColor(colors.surface),
        error = animateColor(colors.error),
        onPrimary = animateColor(colors.onPrimary),
        onSecondary = animateColor(colors.onSecondary),
        onBackground = animateColor(colors.onBackground),
        onSurface = animateColor(colors.onSurface),
        onError = animateColor(colors.onError),
        isLight = colors.isLight,
    )
}