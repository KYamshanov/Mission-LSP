package ru.mission.glossary.components

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    // It's possible to pop multiple screens at a time on iOS
    fun onBackClicked(toIndex: Int)

    // Defines all possible child components
    sealed class Child {
        class ListChild(val component: ListComponent) : Child()

        class DetailsChild(val component: DetailsComponent) : Child()

        class LoadDictionaryChild(val component: LoadDictionaryComponent) : Child()

        class CollectionsChild(val component: CollectionsComponent) : Child()

        class EditDictionaryChild(val component: EditDictionaryComponent) : Child()

    }
}
