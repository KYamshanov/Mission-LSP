package ru.mission.heart.component.factory

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import ru.mission.heart.component.LoginComponent
import ru.mission.heart.component.impl.AndroidLoginComponentImpl

internal class AndroidLoginComponentFactoryImpl : LoginComponentFactory, KoinComponent {

    override fun create(
        componentContext: ComponentContext,
        onSussessSingIn: () -> Unit,
        onFailedSingIn: () -> Unit
    ): LoginComponent =
        AndroidLoginComponentImpl(
            componentContext = componentContext,
            generator = get(),
            networkConfig = get(),
            mainContext = get(named("main")),
            sessionInteractor = get(),
            onSussessSingIn = onSussessSingIn,
            onFailedSingIn = onFailedSingIn
        )
}