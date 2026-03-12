package org.example

data class Chest(
    val id: String,
    val isLocked: Boolean = false,
    var isOpen: Boolean = false,
    var isExamined: Boolean = false,
    val contents: MutableList<Item> = mutableListOf()
)
