package ru.mission.heart.component.factory

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import ru.mission.heart.component.RootComponent
import ru.mission.heart.component.impl.RootComponentImpl

/**
 * Factory for instantiate RootComponent
 */
class RootComponentFactory : KoinComponent {

    operator fun invoke(componentContext: ComponentContext): RootComponent {
        return RootComponentImpl(
            componentContext, get(), get(named("main")), get()
        )
    }
}