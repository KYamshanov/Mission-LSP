package ru.mission.heart.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mission.heart.api.MissionAuthApi
import ru.mission.heart.api.MissionAuthApiImpl
import ru.mission.heart.network.LocalNetworkConfig
import ru.mission.heart.network.NetworkConfig
import ru.mission.heart.network.RequestFactory
import ru.mission.heart.network.RequestFactoryImpl
import ru.mission.heart.session.JwtSessionInteractor
import ru.mission.heart.session.SessionInteractor

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal expect fun platformModule(): Module

internal val commonModule = module {
    single<NetworkConfig> { LocalNetworkConfig() }
    single<RequestFactory> { RequestFactoryImpl(get(), lazy { get() }) }
    single<SessionInteractor>(createdAtStart = true) {
        JwtSessionInteractor(
            accessTokenKey = "",
            refreshTokenKey = "",
            preferences = get(),
            missionAuthApi = get()
        )
    }
    factory<MissionAuthApi> { MissionAuthApiImpl(get()) }
}

