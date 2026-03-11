package org.example

class Player(var currentRoomId: String, val inventory: MutableList<Item> = mutableListOf()) {
    fun takeItem(item: Item) {
        inventory.add(item)
    }

    fun dropItem(itemId: String) : Item? {
        val item = inventory.find { it.id == itemId }
        return if (item != null) {
            inventory.remove(item)
            item
        } else {
            null
        }
    }

    fun hasItem(itemId: String) :Boolean {
        return inventory.any { it.id == itemId }
    }
}