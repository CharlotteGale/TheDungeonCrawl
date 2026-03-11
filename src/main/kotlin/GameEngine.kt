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
            description = "You stand in a dimly lit stone chamber. Torch sconces flicker on the walls. " +
                    "To the north, a crumbling archway leads deeper into the dungeon. " +
                    "To the east, a narrow corridor disappears into darkness. " +
                    "To the west, a heavy wooden door sits firmly closed. " +
                    "Behind you to the south, a sliver of daylight cuts through the gloom — the way out.",
            exits = mapOf("north" to "archway", "east" to "corridor", "south" to "exit", "west" to "door"),
            items = mutableListOf()
        ),
        "archway" to Room(
            id = "archway",
            description = "You pass through the crumbling archway into a wider chamber. " +
                    "The air is stale and cold. Moss creeps along the walls.",
            exits = mapOf("south" to "entrance"),
            items = mutableListOf()
        ),
        "corridor" to Room(
            id = "corridor",
            description = "A narrow corridor stretches before you, the torchlight barely reaching the walls. " +
                    "It feels like it goes somewhere interesting.",
            exits = mapOf("west" to "entrance"),
            items = mutableListOf()
        ),
        "west_room" to Room(
            id = "west_room",
            description = "You manage to open the heavy door and step into a dusty room. " +
                    "Cobwebs hang from the ceiling. Something glints in the corner.",
            exits = mapOf("east" to "entrance"),
            items = mutableListOf()
        ),
        "exit" to Room(
            id = "exit",
            description = "Warm sunlight spills over you as you step outside. " +
                    "The dungeon entrance looms behind you to the north. " +
                    "You could leave... but something draws you back in.",
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
            is Command.Take,
            is Command.Drop,
            is Command.Inventory -> println("Not implemented yet")
            is Command.Unknown -> println("You mutter to yourself. Nothing happens. \nType 'help' for a list of commands.")
        }
    }

    fun handleGo(direction: String) {
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