package ru.mission.glossary.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mission.glossary.*
import ru.mission.glossary.JsoupParser
import ru.mission.glossary.ShareCollectionDesktop
import ru.mission.glossary.SingleAppParser

/**
 * Factory method to build Koin Module
 * for deplare platform dependencies
 */
internal actual fun platformModule(): Module = module {
    factory<SingleAppParser> { JsoupParser() }

    single<SqlDriver> { JdbcSqliteDriver("jdbc:sqlite:database.db", schema = Database.Schema) }
    single<Database> { Database(get()) }
    single<ShareCollection> { ShareCollectionDesktop() }
    single<LoadSharedCollection> { LoadSharedCollectionDesktop(get()) }
}