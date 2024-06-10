package ru.mission.heart.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mission.heart.DesktopGeneratorImpl
import ru.mission.heart.DesktopPreferencesImpl
import ru.mission.heart.Generator
import ru.mission.heart.Preferences
import ru.mission.heart.component.factory.DesktopLoginComponentFactoryImpl
import ru.mission.heart.component.factory.LoginComponentFactory

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal actual fun platformModule(): Module = module {
    single<Generator> { DesktopGeneratorImpl() }
    single<Preferences> { DesktopPreferencesImpl() }
    factory<LoginComponentFactory> { DesktopLoginComponentFactoryImpl() }
}