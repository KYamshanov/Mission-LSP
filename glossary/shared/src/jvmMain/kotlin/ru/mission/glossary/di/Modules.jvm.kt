package ru.mission.glossary.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mission.glossary.JsoupParser
import ru.mission.glossary.SingleAppParser

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal actual fun platformModule(): Module = module {
    factory<SingleAppParser> { JsoupParser() }
}