package ru.mission.glossary.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.mission.glossary.Dictionary
import ru.mission.glossary.components.factory.RootComponentFactory
import ru.mission.glossary.components.factory.RootComponentFactoryImpl
import ru.mission.glossary.data.SqDictionary
import kotlin.coroutines.CoroutineContext
import kotlin.math.sin

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal expect fun platformModule(): Module

internal val commonModule = module {
    single<CoroutineContext>(named("main")) { Dispatchers.Main }
    single<CoroutineContext>(named("io")) { Dispatchers.IO }
    single<CoroutineContext>(named("default")) { Dispatchers.Default }

    factory<RootComponentFactory> {
        RootComponentFactoryImpl(
            singleAppParser = get(),
            mainContext = get(named("main")),
            defaultContext = get(named("default")),
            dictionary = get(),
            shareCollection = get(),
            loadSharedCollection = get()
        )
    }

    factory<Dictionary> { SqDictionary(get(), get(named("io"))) }
}