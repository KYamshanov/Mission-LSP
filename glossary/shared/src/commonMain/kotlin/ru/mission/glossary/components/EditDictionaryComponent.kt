package ru.mission.glossary.components

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface EditDictionaryComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed class Child {

        class ViewWordsChild(val component: ViewWordsComponent) : Child()

        class AddWordChild(val component: AddWordComponent) : Child()

        class EditWordChild(val component: EditWordComponent) : Child()
    }
}