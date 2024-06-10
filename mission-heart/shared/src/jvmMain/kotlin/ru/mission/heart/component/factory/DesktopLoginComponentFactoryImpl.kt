package ru.mission.heart.component.factory

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import ru.mission.heart.component.LoginComponent
import ru.mission.heart.component.impl.DesktopLoginComponentImpl

internal class DesktopLoginComponentFactoryImpl : LoginComponentFactory, KoinComponent {

    override fun create(
        componentContext: ComponentContext,
        onSussessSingIn: () -> Unit,
        onFailedSingIn: () -> Unit
    ): LoginComponent =
        DesktopLoginComponentImpl(
            componentContext = componentContext,
            generator = get(),
            networkConfig = get(),
            mainContext = get(named("main")),
            ioContext = get(named("io")),
            sessionInteractor = get(),
            onSussessSingIn = onSussessSingIn,
            onFailedSingIn = onFailedSingIn
        )
}