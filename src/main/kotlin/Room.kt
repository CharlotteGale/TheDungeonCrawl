package org.example

data class Room(
    val id: String,
    val description: String,
    val exits: Map<String, String>,
    val items: MutableList<Item>
)
