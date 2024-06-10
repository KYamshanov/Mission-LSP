package ru.mission.heart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mission.heart.component.LoginComponent

/**
 * Login context
 *
 * at android it will be as WebView with login form
 */
@Composable
expect fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier)
