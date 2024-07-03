package ru.mission.glossary.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.factory.RootComponentFactory
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

    factory { RootComponentFactory(get()) }
}