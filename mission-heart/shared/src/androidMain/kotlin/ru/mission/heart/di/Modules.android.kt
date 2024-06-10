package ru.mission.heart.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mission.heart.AndroidGeneratorImpl
import ru.mission.heart.AndroidPreferencesImpl
import ru.mission.heart.Generator
import ru.mission.heart.component.factory.AndroidLoginComponentFactoryImpl
import ru.mission.heart.component.factory.LoginComponentFactory
import ru.mission.heart.Preferences

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal actual fun platformModule(): Module = module {
    single<Generator> { AndroidGeneratorImpl() }
    single<Preferences> { AndroidPreferencesImpl(get()) }
    factory<LoginComponentFactory> { AndroidLoginComponentFactoryImpl() }
}