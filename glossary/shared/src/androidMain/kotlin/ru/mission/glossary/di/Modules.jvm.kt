package ru.mission.glossary.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mission.glossary.Database
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.WebViewParser

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal actual fun platformModule(): Module = module {
    factory<SingleAppParser> { WebViewParser() }

    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, get(), "database.db")  }
    single<Database> { Database(get()) }
}