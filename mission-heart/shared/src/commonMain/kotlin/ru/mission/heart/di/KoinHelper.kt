package ru.mission.heart.di

import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin


fun initKoin(platformConfiguation: KoinApplication.() -> Unit) {
    startKoin {
        platformConfiguation()
        modules(
            commonModule,
            platformModule(),
        )
    }.koin
}
