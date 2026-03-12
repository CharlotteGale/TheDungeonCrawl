package org.example

data class Lootable(
    val id: String,
    val name: String,
    val description: String,
    var isLooted: Boolean = false,
    val items: MutableList<Item> = mutableListOf()
)
