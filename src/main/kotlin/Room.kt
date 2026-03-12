package org.example

data class Room(
    val id: String,
    val description: String,
    val exits: Map<String, String>,
    val isLocked: Boolean = false,
    val items: MutableList<Item> = mutableListOf(),
    val chests: MutableList<Chest> = mutableListOf(),
    val lootables: MutableList<Lootable> = mutableListOf()
)
