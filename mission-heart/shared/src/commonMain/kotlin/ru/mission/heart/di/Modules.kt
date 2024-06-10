package ru.mission.heart.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.mission.heart.api.MissionAuthApi
import ru.mission.heart.api.MissionAuthApiImpl
import ru.mission.heart.network.LocalNetworkConfig
import ru.mission.heart.network.NetworkConfig
import ru.mission.heart.network.RequestFactory
import ru.mission.heart.network.RequestFactoryImpl
import ru.mission.heart.session.JwtSessionInteractor
import ru.mission.heart.session.SessionInteractor
import kotlin.coroutines.CoroutineContext

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal expect fun platformModule(): Module

internal val commonModule = module {
    single<CoroutineContext>(named("main")) { Dispatchers.Main }
    single<CoroutineContext>(named("io")) { Dispatchers.IO }
    single<CoroutineContext>(named("default")) { Dispatchers.Default }

    single<NetworkConfig> { LocalNetworkConfig() }
    single<RequestFactory> { RequestFactoryImpl(get(), lazy { get() }) }
    single<SessionInteractor>(createdAtStart = true) {
        JwtSessionInteractor(
            accessTokenKey = "access_token",
            refreshTokenKey = "refresh_token",
            preferences = get(),
            missionAuthApi = get()
        )
    }
    factory<MissionAuthApi> { MissionAuthApiImpl(get()) }
}

