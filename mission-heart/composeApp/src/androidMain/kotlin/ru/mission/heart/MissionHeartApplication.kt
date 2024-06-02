package ru.mission.heart

import android.app.Application
import android.content.Context
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.dsl.module
import ru.mission.heart.di.initKoin

class MissionHeartApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        initKoin {
            modules(
                module {
                    single<Context> { this@MissionHeartApplication }
                }
            )
        }
    }
}