package ru.mission.heart.component.factory

import com.arkivanov.decompose.ComponentContext
import ru.mission.heart.component.RootComponent
import ru.mission.heart.api.MissionAuthApiImpl
import ru.mission.heart.impl.RootComponentImpl
import ru.mission.heart.network.NetworkConfig
import ru.mission.heart.network.RequestFactoryImpl
import ru.mission.heart.preferences
import ru.mission.heart.session.JwtSessionInteractor

/**
 * Factory for instantiate RootComponent
 */
class RootComponentFactory {

    operator fun invoke(componentContext: ComponentContext): RootComponent {
        var sessionInteractor: JwtSessionInteractor? = null

        sessionInteractor = JwtSessionInteractor(
            accessTokenKey = "",
            refreshTokenKey = "",
            preferences = preferences(),
            missionAuthApi = MissionAuthApiImpl(RequestFactoryImpl(NetworkConfig(""), lazy { sessionInteractor!! }))
        )

        return RootComponentImpl(
            componentContext, sessionInteractor
        )
    }
}