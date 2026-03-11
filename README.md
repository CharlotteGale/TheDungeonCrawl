
## User Stories
### 🗺️ World & Navigation
**US-01**       
As a player, I want to read a description of my current location, so I know where I am and what surrounds me.      
**US-02**       
As a player, I want to move between rooms by typing directions (e.g. go north, go east) so I can explore the world.       
**US-03**       
As a player, I want to see which exits are available from my current room, so I know where I can go.       
**US-04**       
As a player, I want the world to have interconnected rooms so exploration feels meaningful and continuous.        

### 🎒 Inventory & Items
**US-05**       
As a player, I want to pick up items in a room by typing take [item] so I can carry useful objects.       
**US-06**       
As a player, I want to drop items from my inventory by typing drop [item] so I can manage what I carry.       
**US-07**       
As a player, I want to type inventory (or i) to see what I'm currently carrying.      
**US-08**       
As a player, I want to examine [item] to read a detailed description of an object.        

### ⚔️ Combat & Enemies
**US-09**        
As a player, I want to encounter enemies in certain rooms so the game feels dangerous and exciting.       
**US-10**       
As a player, I want to attack [enemy] to engage in combat so I can defeat threats.        
**US-11**       
As a player, I want to see health points for myself and enemies during combat so I can make strategic decisions.      
**US-12**       
As a player, I want enemies to fight back so combat feels like a real challenge.      

### 💾 Player Progression
**US-13**       
As a player, I want a health stat, so I know how close I am to losing.     
**US-14**       
As a player, I want to find health items (e.g. potions) and use them with use [item] to restore my health.               
**US-15**       
As a player, I want to gain experience or score for defeating enemies and completing objectives so progress feels rewarding.      

### 🧩 Puzzles & Interactions
**US-16**       
As a player, I want to find locked doors that require a key item, so I have goals to work toward.      
**US-17**       
As a player, I want to talk to NPCs to receive hints, lore, or quests so the world feels alive.       
**US-18**       
As a player, I want some items to be hidden or conditionally revealed so exploration is rewarding.        

### 🖥️ Core Game Loop & UX
**US-19**      
As a player, I want a help command that lists available commands, so I'm never stuck wondering what to type.       
**US-20**       
As a player, I want a look command to re-read the current room description at any time.       
**US-21**       
As a player, I want clear feedback for invalid commands, so I'm not confused by silent failures.          
**US-22**       
As a player, I want a win condition (e.g. reaching a final room or defeating a boss) so the game has a satisfying end.        
**US-23**       
As a player, I want a game over state when my health reaches zero so failure feels meaningful.        
**US-24**       
As a player, I want to save and load my game so I can continue a session later.     

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

