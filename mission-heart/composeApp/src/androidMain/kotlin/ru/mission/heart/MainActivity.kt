package ru.mission.heart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import ru.mission.heart.component.factory.RootComponentFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //base configuration
        val root =
            RootComponentFactory().invoke(
                componentContext = defaultComponentContext(),
            )

        setContent {
            App(root)
        }
    }
}