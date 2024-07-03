package ru.mission.glossary.di

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