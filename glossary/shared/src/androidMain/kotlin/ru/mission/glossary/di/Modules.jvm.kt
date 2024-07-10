package ru.mission.glossary.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.mission.glossary.AndroidSpaViewModel
import ru.mission.glossary.AndroidSpaViewModelImpl
import ru.mission.glossary.Database
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.WebViewCallback
import ru.mission.glossary.WebViewParser

/**
 * Factory method to build Koin Module
 * for declare platform dependencies
 */
internal actual fun platformModule(): Module = module {
    factory<SingleAppParser> { WebViewParser(get(), get()) }

    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, get(), "database.db") }
    single<Database> { Database(get()) }
    single<HttpClient> { HttpClient() }

    single { AndroidSpaViewModelImpl() } binds arrayOf(AndroidSpaViewModel::class, WebViewCallback::class)
}