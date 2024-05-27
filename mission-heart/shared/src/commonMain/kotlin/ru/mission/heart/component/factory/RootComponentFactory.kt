package ru.mission.heart.component.factory

import com.arkivanov.decompose.ComponentContext
import ru.mission.heart.RootComponent
import ru.mission.heart.api.MissionAuthApiImpl
import ru.mission.heart.impl.RootComponentImpl
import ru.mission.heart.network.NetworkConfig
import ru.mission.heart.network.RequestFactoryImpl
import ru.mission.heart.session.JwtSessionInteractor

/**
 * Factory for instantiate RootComponent
 */
class RootComponentFactory {

    operator fun invoke(componentContext: ComponentContext): RootComponent =
        RootComponentImpl(
            componentContext, JwtSessionInteractor(
                "", "",
                preferences(),
                MissionAuthApiImpl(RequestFactoryImpl(NetworkConfig(""),))
            )
}