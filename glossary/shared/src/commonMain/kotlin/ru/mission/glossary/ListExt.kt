package ru.mission.glossary

inline fun <reified T> List<T>.setLastItem(transform: (T) -> T): List<T> {
    val old = get(lastIndex)
    val new = transform(old)
    return toMutableList().apply { set(lastIndex, new) }
}

inline fun <reified T> List<T>.setFirstItem(transform: (T) -> T): List<T> {
    val old = get(0)
    val new = transform(old)
    return toMutableList().apply { set(0, new) }
}