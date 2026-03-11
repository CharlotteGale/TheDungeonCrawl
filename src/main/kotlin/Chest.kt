package org.example

data class Chest(
    val id: String,
    val description: String,
    val isLocked: Boolean,
    val isOpen: Boolean,
    val contents: MutableList<Item> = mutableListOf()
)
