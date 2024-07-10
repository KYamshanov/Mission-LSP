package ru.mission.glossary

import android.app.Application
import android.content.Context
import org.koin.dsl.module
import ru.mission.glossary.di.initKoin

class GlossaryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
       // Napier.base(DebugAntilog())
        initKoin {
            modules(
                module {
                    single<Context> { this@GlossaryApplication }
                }
            )
        }
    }
}