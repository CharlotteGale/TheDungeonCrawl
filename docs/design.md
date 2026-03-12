## Class Interfaces
### Data Classes
```kotlin
data class Item(
    val id: String,
    val name: String,
    val description: String
)
```

```kotlin
data class Room(
    val id: String,
    val description: String,
    val exits: Map<String, String>,
    val items: MutableList<Item>
)
```

### Classes
```kotlin
class Player(var currentRoomId: String, val inventory: MutableList<Item>){
    fun takeItem(item: Item) {
        // no return value
        // adds item to inventory
    }
    
    fun dropItem(itemId: String): Item? {
        // returns dropped item
        // pops item from inventory
    }
    
    fun hasItem(itemId: String): Boolean {
        // returns that item is in inventory
    }
}
```

```kotlin
class GameEngine(player: Player, rooms: Map<String, Room>) {
    fun start() {}
    
    fun gameLoop() {}
    
    fun handleGo(direction: String) {}
    
    fun handleTake(itemName: String) {}
    
    fun handleDrop(itemName: String) {}
    
    fun handleLook() {}
    
    fun handleInventory() {}
    
    fun printHelp() {}
    
    fun currentRoom(): Room {}
}
```

```kotlin
sealed class Command {
    data class Go(val direction: String) : Command()
    data class Take(val itemName: String) : Command()
    data class Drop(val itemName: String) : Command()
    object Look : Command()
    object Inventory : Command()
    object Help : Command()
    object Quit : Command()
    object Unknown : Command()
}
```

### Top Level Function
```kotlin
// CommandParser
fun parse(input: String): Command {}

```