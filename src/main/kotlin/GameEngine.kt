package org.example

object CommandParser {
    fun parse(input: String): Command {
        val parts = input.trim().lowercase().split("\\s+".toRegex())
        val keyword = parts[0]
        val argument = parts.getOrNull(1) ?: ""

        return when (keyword) {
            "go", "move", "walk" -> Command.Go(argument)
            "look", "l" -> Command.Look
            "inventory", "inv", "i" -> Command.Inventory
            "take", "pick" -> Command.Take(argument)
            "drop" -> Command.Drop(argument)
            "open", "pry", "try" -> Command.Open(argument)
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
            items = mutableListOf(
                Item(id = "scroll", name = "Scroll", description = "A rolled scroll tied with a leather cord.")
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
                Item(id = "torch", name = "Unlit Torch", description = "A torch, cold and unlit. It looks like it would burn well enough.")
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
            is Command.Loot -> println("\nNot implemented yet!")
            is Command.Inventory -> handleInventory()
            is Command.Unknown -> println("You mutter to yourself. Nothing happens. \nType 'help' for a list of commands.")
        }
    }

    fun handleGo(direction: String) {

        if (currentRoom().id == "entrance" && direction == "west") {
            println("\nYou stand before the door. " +
                    "\nIt definitely isn't going to open without help.")
            return
        }
        val exit = currentRoom().exits[direction]
        if (exit != null) {
            player.currentRoomId = exit
            handleLook()
        } else {
            println("You can't go that way.")
        }
    }

    fun handleLook() {
        val room = currentRoom()
        println("\n${room.description}")
        println("\nExits: ${room.exits.keys.joinToString(", ")}")
    }

    fun handleTake(itemName: String) {
        val room = currentRoom()
        val item = room.items.find { it.id.lowercase() == itemName }
        if (item != null) {
            room.items.remove(item)
            player.takeItem(item)
            println("\nYou pick up the ${item.name}.")
        } else {
            println("\nThere's nothing to take.")
        }
    }

    fun handleDrop(itemName: String) {
        val item = player.inventory.find { it.id.lowercase() == itemName }
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
        when (target) {
            "door" -> {
                val currentExits = currentRoom().exits
                if (currentRoom().id == "entrance") {
                    println("\nYou throw your shoulder against the door. The rusted hinges groan but don't give. " +
                            "\nIt isn't going anywhere without help.")
                } else if (currentExits.containsValue("mess_room") || currentExits.containsValue("entrance")) {
                    println("\nThe door is jammed solid in its frame.")
                } else {
                    println("\nThere's no door to open here.")
                }
            }
            "chest" -> {
                when (currentRoom().id) {
                    "entrance" -> {
                        println(
                            "\nYou lift the lid of the wooden chest. " +
                                    "\nInside, sitting on a bed of moldered cloth, is a scroll tied with a leather cord."
                        )
                    }
                    "barracks" -> {
                        println("\nThe iron clasps are fastened tight. The lid doesn't budge.")
                    }
                    else -> {
                        println("\nThere's no chest to open here.")
                    }
                }
            }
            "" -> {
                println("\nOpen what exactly?")
            }
            else -> {
                println("\nYou can't open that.")
            }
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