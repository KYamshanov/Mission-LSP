package ru.mission.heart

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.heart.component.LoginComponent

/**
 * Login context
 *
 * at android it will be as WebView with login form
 */
@Composable
expect fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier)
