package com.pepivsky.todocompose.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pepivsky.todocompose.ui.theme.splashScreenBackground
import com.pepivsky.todocompose.R
import com.pepivsky.todocompose.ui.theme.LOGO_SIZE
import com.pepivsky.todocompose.ui.theme.ToDoComposeTheme

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.splashScreenBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(LOGO_SIZE),
            painter = painterResource(id = GetLogo()),
            contentDescription = stringResource(id = R.string.to_do_logo)
        )
    }
}

// devuelve el logo dependiendo de si el sistema tiene el modo oscuro o claro
@Composable
fun GetLogo(): Int {
    return if (isSystemInDarkTheme()) {
        R.drawable.logo_dark
    } else {
        R.drawable.logo_light
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

// preview darkTheme
@Preview
@Composable
fun SplashScreenDarkPreview() {
    ToDoComposeTheme(darkTheme = true) {
        SplashScreen()
    }
}