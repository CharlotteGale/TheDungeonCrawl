package org.example

object CommandParser {
    fun parse(input: String): Command {
        val parts = input.trim().lowercase().split("\\s+".toRegex())
        val keyword = parts[0]
        val argument = parts.drop(1).joinToString(" ")

        return when (keyword) {
            "go", "move", "walk" -> Command.Go(argument)
            "look", "l" -> Command.Look
            "inventory", "inv", "i" -> Command.Inventory
            "take", "pick" -> Command.Take(argument)
            "drop" -> Command.Drop(argument)
            "open", "pry", "try" -> Command.Open(argument)
            "loot", "grab" -> Command.Loot(argument)
            "examine", "ex", "inspect", "x" -> Command.Examine(argument)
            "use" -> Command.Use(argument)
            "help", "h", "?" -> Command.Help
            "quit", "exit", "q" -> Command.Quit
            else -> Command.Unknown
        }
    }
}

sealed class Command {
    data class Go(val direction: String) : Command()
    data class Take(val itemName: String) : Command()
    data class Drop(val itemName: String) : Command()
    data class Open(val target: String) : Command()
    data class Loot(val target: String) : Command()
    data class Examine(val target: String) : Command()
    data class Use(val itemName: String) : Command()
    object Look : Command()
    object Inventory : Command()
    object Help : Command()
    object Quit : Command()
    object Unknown : Command()
}

class GameEngine {
    private val rooms: Map<String, Room> = mapOf(
        "entrance" to Room(
            id = "entrance",
            description = "You stand in a dimly lit stone chamber, the air stale and cold against your skin. " +
                    "\nTorch sconces line the walls, their flames long since guttered — all but one, which flickers weakly in a draft you cannot place. " +
                    "\nTo the north, a crumbling archway opens into a vaulted chamber beyond. " +
                    "\nTo the east, a narrow corridor disappears into shadow, the air from it heavy and damp. " +
                    "\nTo the west, a heavy wooden door sits firmly in its frame — the hinges are rusted solid, it isn't going anywhere without help. " +
                    "\nSlumped against the western wall is a skeleton, its bony fingers still curled around nothing. Whatever it was carrying is long gone. " +
                    "\nBeside it sits a wooden chest, its lid closed but unlocked. " +
                    "\nBehind you to the south, a sliver of daylight cuts through the gloom — the way out.",
            exits = mapOf("north" to "altar_room", "east" to "corridor", "south" to "exit"),
            items = mutableListOf(),
            chests = mutableListOf(
                Chest(
                    id = "wooden_chest",
                    name = "Ornate Chest",
                    isLocked = false,
                    isOpen = false,
                    contents = mutableListOf(
                        Item(
                            id = "scroll",
                            name = "Scroll",
                            description = "A rolled scroll tied with a leather cord. " +
                                    "The wax seal bears the same sigil as the altar to the north.")
                    )
                )
            ),
            lootables = mutableListOf(
                Lootable(
                    id = "skeleton",
                    name = "Skeleton",
                    description = "A long dead soldier, slumped against the wall. " +
                            "Whatever killed it, it didn't have time to reach for its weapon. " +
                            "A tarnished key hangs from its belt.",
                    items = mutableListOf(
                        Item(
                            id = "chest_2_key",
                            name = "Tarnished Key",
                            description = "A tarnished key, still attached to a leather fob bearing a faded insignia."
                        )
                    )
                )
            )
        ),
        "altar_room" to Room(
            id = "altar_room",
            description = "The chamber opens into a vaulted space, the ceiling lost in shadow above. " +
                    "\nAt the far end stands a broad stone altar, its surface etched with the sigil of Malachar, the God of War and Conquest. " +
                    "\nTwo guttering candles flank the altar, their weak flames barely holding back the dark. " +
                    "\nAncient weapons line the walls, rusted and ceremonial. A warrior's helm sits at the centre of the altar, dull and cracked. " +
                    "\nTo the south, the crumbling archway leads back to the entrance chamber. " +
                    "\nIn the alcove beside it, a torch sits unlit in its iron bracket.",
            exits = mapOf("south" to "entrance"),
            items = mutableListOf(
                Item(id = "unlit_torch", name = "Torch", description = "A torch, cold and unlit. It looks like it would burn well enough."),
                Item(id = "chest_1_key", name = "Small Key", description = "A small iron key, worn smooth with age.")
            )
        ),
        "corridor" to Room(
            id = "corridor",
            description = "A narrow stone corridor stretches ahead, torches burning steadily in their sconces at regular intervals. " +
                    "\nSomeone, or something, has kept them lit. " +
                    "\nThe air is stale but the flames don't flicker, which is somehow worse. " +
                    "\nThe passage feels longer than it should, the far end swallowed in a deceptive stillness. " +
                    "\nAs you walk, you notice a flagstone that sits fractionally higher than the rest. You place your foot carefully. The flagstone shifts slightly underfoot, and you freeze. " +
                    "\nIt settles. You breathe again.  " +
                    "\nThe walls either side are not entirely solid; narrow slits run at waist height, dark and hollow, angled inward. " +
                    "\nThey are dark and silent, but your imagination fills them with things you'd rather not think about. " +
                    "\nMidway along the corridor a rusted iron lever protrudes from the wall, worn smooth at the handle as though it has been gripped many times before. " +
                    "\nTo the west, the entrance chamber. Ahead to the east, you can see three doors.",
            exits = mapOf("west" to "entrance", "east" to "barracks"),
            items = mutableListOf()
        ),
        "barracks" to Room(
            id = "barracks",
            description = "Of the three doors at the corridor's end, only one swings open reluctantly, with a groan of rusted hinges.. " +
                    "\nFour soldier's cots line the walls, their blankets thrown back as though the occupants left in haste. " +
                    "\nYet a thick layer of dust coats every surface, undisturbed for what must be years. " +
                    "\nAt the foot of each cot sits a chest. Most hang open and empty, their lids thrown back and forgotten. " +
                    "\nTwo remain shut, their heavy iron clasps fastened tight; the lids don't budge when you test them. " +
                    "\nScattered across the floor lie the remnants of a soldier's life: a cracked leather boot, a dented tin cup, a torn piece of cloth bearing a faded insignia. " +
                    "\nA short sword leans against the far wall, its blade dulled but intact. " +
                    "\nTo the west, the corridor winds back toward the entrance.",
            exits = mapOf("west" to "corridor"),
            items = mutableListOf(
                Item(id = "short_sword", name = "Short Sword", description = "A short sword, its blade dulled but intact.")
            ),
            chests = mutableListOf(
                Chest(
                    id = "chest_1",
                    name = "Iron Chest",
                    isLocked = true,
                    contents = mutableListOf(
                        Item(
                            id = "mess_room_key",
                            name = "Rusted Key",
                            description = "A heavy rusted key. It looks like it would fit a large door."
                        )
                    )
                ),
                Chest(
                    id = "chest_2",
                    name = "Soldiers Chest",
                    isLocked = true,
                    contents = mutableListOf(
                        Item(
                            id = "gold_coins",
                            name = "Gold Coins",
                            description = "A small pouch of gold coins, still heavy. Someone left in a hurry."
                        ),
                        Item(
                            id = "journal",
                            name = "Soldiers Journal",
                            description = "A leather bound journal, its pages yellowed and brittle. " +
                                    "The last entry is dated, but the ink has run. " +
                                    "One word is still legible: 'below'."
                        )
                    )
                )
            )
        ),
        "mess_room" to Room(
            id = "mess_room",
            description = "You squeeze through the jammed door into what was once the dungeon's mess room. " +
                    "\nLong wooden tables run the length of the room, flanked by benches worn smooth from years of use. " +
                    "\nA stone hearth dominates the far wall, cold and long dead, though a faint smell of woodsmoke clings to the air as though it hasn't quite given up. " +
                    "\nIron candle holders are fixed to the walls at regular intervals, and unlike the rest of the dungeon, most still hold candles — enough that the room feels almost habitable. Almost. " +
                    "\nPinned to the wall beside the hearth is a rough map of the dungeon. The edges have been gnawed away — mice, you tell yourself. " +
                    "\nThe remaining portion shows the entrance chamber, this room, the altar room and the eastern corridor. Everything beyond is gone. " +
                    "\nYou look at the floor. There are no mouse droppings. " +
                    "\nTo the south, a door leads to what looks like a store room. " +
                    "\nTo the east, the jammed door back to the entrance chamber.",
            exits = mapOf("south" to "supply_store", "east" to "entrance"),
            items = mutableListOf()
        ),
        "supply_store" to Room(
            id = "supply_store",
            description = "The door creaks open into a low-ceilinged store room, the light from the mess room behind you barely reaching the back wall. " +
                    "\nMost of the shelving has been pulled down or collapsed, the contents long since scattered or taken. " +
                    "\nIn the dim light you can make out a few shelves still standing along the right wall. " +
                    "\nMost are bare, but a few hold what look like sealed clay pots and folded sacking — the kind of supplies that outlast everything else. " +
                    "\nOn the shelf nearest you, catching what little light there is, sit two apples. They look almost fresh. " +
                    "\nYou look at them for a long moment. " +
                    "\nThe dust on the floor is thick except for one spot near the back wall, where a single large footprint breaks the surface. " +
                    "\nYou can't make out what made it in the dark. " +
                    "\nTo the north, the mess room.",
            exits = mapOf("north" to "mess_room"),
            items = mutableListOf(
                Item(id = "apple", name = "Apple", description = "Two apples, fresher than they have any right to be.")
            )
        ),
        "exit" to Room(
            id = "exit",
            description = "Warm sunlight spills over you as you step outside. " +
                    "\nThe dungeon entrance looms behind you to the north. " +
                    "\nYou could leave... but something draws you back in.",
            exits = mapOf("north" to "entrance"),
            items = mutableListOf()
        )
    )

    private val player = Player(currentRoomId = "entrance", inventory = mutableListOf())

    fun start() {
        System.out.flush()
        System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))
        println("=================================")
        println("      THE DUNGEON CRAWL")
        println("=================================")
        handleLook()
        gameLoop()
    }

    fun gameLoop() {
        while (true) {
            print("\n> ")
            val input = readln()?.trim()?.lowercase() ?: continue

            if (input.isEmpty()) continue

            val command = CommandParser.parse(input)
            handleCommand(command)
        }

    }

    fun handleCommand(command: Command) {
        when (command) {
            is Command.Go -> handleGo(command.direction)
            is Command.Look -> handleLook()
            is Command.Help -> printHelp()
            is Command.Quit -> {
                println("You flee the dungeon... coward.")
                System.exit(0)
            }
            is Command.Open -> handleOpen(command.target)
            is Command.Take -> handleTake(command.itemName)
            is Command.Drop -> handleDrop(command.itemName)
            is Command.Loot -> handleLoot(command.target)
            is Command.Examine -> handleExamine(command.target)
            is Command.Use -> handleUse(command.itemName)
            is Command.Inventory -> handleInventory()
            is Command.Unknown -> println("You mutter to yourself. Nothing happens. \nType 'help' for a list of commands.")
        }
    }

    fun handleGo(direction: String) {
        if (currentRoom().id == "entrance" && direction == "west") {
            if (player.hasItem("mess_room_key")) {
                player.dropItem("mess_room_key")
                println("\nThe key grinds in the lock. The door swings open with a groan of rusty hinges.")
                player.currentRoomId = "mess_room"
                handleLook()
            } else {
                println("\nYou throw your shoulder against the door. The rusted hinges groan but don't give. " +
                        "\nIt isn't going anywhere without help.")
            }
            return
        }

        val exit = currentRoom().exits[direction]
        if (exit != null) {
            player.currentRoomId = exit
            handleLook()
        } else {
            println("\nYou can't go that way.")
        }
    }

    fun handleLook() {
        val room = currentRoom()
        println("\n${room.description}")

        if (room.id == "altar_room" && player.hasItem("lit_torch")) {
            println("\nThe torchlight catches something in the shadows of the alcove — a small key glints on the stone floor beside the bracket.")
        }

        if (room.chests.isNotEmpty()) {
            println("\nChests in this room:")
            room.chests.forEach { chest ->
                val status = when {
                    chest.isLocked -> "locked"
                    chest.isOpen -> "open"
                    else -> "unlocked"
                }
                println("  - ${chest.name} (${status})")
            }
        }

        println("\nExits: ${room.exits.keys.joinToString(", ")}")
    }

    fun handleTake(itemName: String) {
        if (itemName.isEmpty()) {
            println("\nTake what?")
            return
        }

        val room = currentRoom()
        val item = room.items.find { it.name.lowercase().contains(itemName.lowercase()) }

        if (item != null) {
            room.items.remove(item)
            player.takeItem(item)
            println("\nYou pick up the ${item.name}.")
        } else {
            println("\nThere's nothing to take.")
        }
    }

    fun handleDrop(itemName: String) {
        val item = player.inventory.find { it.name.lowercase().contains(itemName.lowercase()) }

        if (itemName.isEmpty()) {
            println("\nDrop what?")
            return
        }

        if (item != null) {
            player.dropItem(item.id)
            currentRoom().items.add(item)
            println("\nYou drop the ${item.name}.")
        } else {
            println("\nYou've not got a $itemName.")
        }
    }

    fun handleInventory() {
        if(player.inventory.isEmpty()) {
            println("You aren't carrying anything.")
        } else {
            println("\nYou are carrying:")
            player.inventory.forEach { println(" - ${it.name}") }
        }
    }

    fun handleOpen(target: String) {
        when {
            target == "door" -> {
                if (currentRoom().id == "entrance") {
                    println("\nTry heading west.")
                } else if (currentRoom().exits.containsValue("mess_room") ||
                    currentRoom().exits.containsValue("entrance")) {
                    println("\nThe door is jammed solid in its frame.")
                } else {
                    println("\nThere's no door to open here.")
                }
            }
            target.isEmpty() -> println("\nOpen what exactly?")
            else -> {
                val chest = currentRoom().chests.find {
                    it.name.lowercase().contains(target.lowercase())
                }
                when {
                    chest == null -> println("\nThere's no chest called '$target' here.")
                    chest.isOpen -> println("\nThe ${chest.name} is already open.")
                    chest.isLocked -> println("\nThe iron clasps on the ${chest.name} are fastened tight. The lid doesn't budge.")
                    else -> {
                        chest.isOpen = true
                        if (chest.contents.isEmpty()) {
                            println("\nYou open the ${chest.name}. It's empty.")
                        } else {
                            println("\nYou lift the lid of the ${chest.name}.")
                            println("\nType 'examine ${chest.name.lowercase()}' to inspect the contents.")
                        }
                    }
                }
            }
        }
    }

    fun handleExamine(target: String) {
        if (target.isEmpty()) {
            println("\nExamine what exactly?")
            return
        }

        // Check chests by name
        val chest = currentRoom().chests.find { it.name.lowercase().contains(target.lowercase()) }
        if (chest != null) {
            when {
                !chest.isOpen -> println("\nThe ${chest.name} is closed. Try opening it first.")
                chest.contents.isEmpty() -> println("\nThe ${chest.name} is empty.")
                else -> {
                    chest.isExamined = true
                    println("\nInside the ${chest.name} you find:")
                    chest.contents.forEach { println("  - ${it.name}: ${it.description}") }
                    println("\nType 'loot ${chest.name.lowercase()}' to take everything, or 'loot <item>' to take something specific.")
                }
            }
            return
        }

        // Check room items and inventory by name
        val roomItem = currentRoom().items.find { it.name.lowercase().contains(target.lowercase()) }
        val inventoryItem = player.inventory.find { it.name.lowercase().contains(target.lowercase()) }
        when {
            roomItem != null -> println("\n${roomItem.description}")
            inventoryItem != null -> println("\n${inventoryItem.description}")
            else -> println("\nYou can't examine that.")
        }
    }

    fun handleLoot(target: String) {
        if (target.isEmpty()) {
            println("\nLoot what exactly?")
            return
        }

        // Check lootables by name
        val lootable = currentRoom().lootables.find { it.name.lowercase().contains(target.lowercase()) }
        if (lootable != null) {
            when {
                lootable.isLooted -> println("\nThere's nothing left on the ${lootable.name}.")
                lootable.items.isEmpty() -> {
                    lootable.isLooted = true
                    println("\nYou search the ${lootable.name} but find nothing of use.")
                }
                else -> {
                    lootable.isLooted = true
                    println("\n${lootable.description}")
                    println("\nYou find:")
                    lootable.items.forEach { println("  - ${it.name}") }
                    lootable.items.forEach { currentRoom().items.add(it) }
                    lootable.items.clear()
                    println("\nThe items have been added to the room. Use 'take <item>' to pick them up.")
                }
            }
            return
        }

        // Check chests by name
        val chest = currentRoom().chests.find { it.name.lowercase().contains(target.lowercase()) }
        if (chest != null) {
            when {
                !chest.isOpen -> println("\nThe ${chest.name} is closed. Try opening it first.")
                chest.contents.isEmpty() -> println("\nThe ${chest.name} is empty.")
                !chest.isExamined -> println("\nYou haven't examined the contents yet. Try 'examine ${chest.name.lowercase()}' first.")
                else -> {
                    // Check if target is a specific item rather than the chest itself
                    val chestItem = chest.contents.find { it.name.lowercase().contains(target.lowercase()) }
                    if (chestItem != null) {
                        chest.contents.remove(chestItem)
                        player.takeItem(chestItem)
                        println("\nYou take the ${chestItem.name}.")
                    } else {
                        val taken = chest.contents.toList()
                        taken.forEach { player.takeItem(it) }
                        chest.contents.clear()
                        println("\nYou take everything from the ${chest.name}:")
                        taken.forEach { println("  - ${it.name}") }
                    }
                }
            }
            return
        }

        println("\nThere's nothing to loot here.")
    }

    fun handleUse(itemName: String) {
        val item = player.inventory.find {
            it.name.lowercase().contains(itemName.lowercase()) ||
                    it.id.lowercase().contains(itemName.lowercase())
        }

        if (item == null) {
            println("\nYou don't have a $itemName.")
            return
        }

        when {
            item.id == "unlit_torch" -> {
                player.dropItem("unlit_torch")
                player.takeItem(Item(
                    id = "lit_torch",
                    name = "Lit Torch",
                    description = "A torch, burning steadily. It pushes the shadows back."
                ))
                println("\nYou strike a light. The torch catches, pushing the shadows back.")
            }
            item.id == "lit_torch" -> {
                println("\nThe torch is already lit.")
            }
            item.id.endsWith("_key") -> {
                val targetId = item.id.removeSuffix("_key")

                // Check chests first
                val chest = currentRoom().chests.find { it.id == targetId }
                if (chest != null) {
                    if (!chest.isLocked) {
                        println("\nThe chest is already unlocked.")
                    } else {
                        chest.isLocked = false
                        player.dropItem(item.id)
                        println("\nThe key turns smoothly. The chest unlocks with a heavy clunk.")
                    }
                    return
                }

//                // Check west door
//                if (targetId == "mess_room" && currentRoom().id == "entrance") {
//                    handleGo("west")
//                    player.dropItem(item.id)
//                    println("\nThe key grinds in the lock. The door swings open with a groan of rusty hinges.")
//                    return
//                }

                println("\nYou can't use that key here.")
            }
            else -> println("\nYou're not sure how to use that.")
        }
    }

    fun printHelp() {
        println("\nCommands:")
        println("  go <direction>  — move in a direction (north, south, east, west)")
        println("  look            — describe your current surroundings")
        println("  help            — show this list")
        println("  quit            — exit the game")

    }

    fun currentRoom(): Room {
        return rooms[player.currentRoomId]
            ?: throw IllegalStateException("Room '${player.currentRoomId}' does not exist.")
    }
}