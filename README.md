# The Dungeon Crawl

> *The entrance looms before you. Torchlight flickers somewhere within. You probably shouldn't go in.*
>
> *You go in anyway.*

A text-based dungeon crawl built in Kotlin. Navigate a crumbling dungeon, interact with your environment, manage your inventory, and try not to think too hard about what's been eating those apples.

---

## Getting Started

### Prerequisites
- JDK 17+
- Gradle

### Running the game

```bash
./gradlew run
```

That's it. The dungeon awaits.

---

## How to Play

The game is driven entirely by text commands. Read the room description, decide what to do, type it in.

### Navigation

| Command | Description |
|---|---|
| `go <direction>` | Move in a direction |
| `look` / `l` | Re-read your current surroundings |

Directions: `north`, `south`, `east`, `west`

### Inventory

| Command | Description |
|---|---|
| `take <item>` | Pick up an item from the room |
| `drop <item>` | Drop an item from your inventory |
| `inventory` / `inv` / `i` | List what you're carrying |

### Interaction

| Command | Description |
|---|---|
| `open <target>` | Open a door or chest |
| `examine <target>` / `x <target>` | Inspect an item closely |
| `loot <target>` | Take an item or everything from an open chest |

### General

| Command | Description |
|---|---|
| `help` / `h` / `?` | Show available commands |
| `quit` / `exit` / `q` | Leave the dungeon (coward) |

---

## The Dungeon

A rough map of what you can currently explore:

```
                         [Altar Room]
                              |
                            north
                              |
[Mess Room] — west — [Entrance Chamber] — east — [Eastern Corridor] — [Barracks]
                              |
                            south
                              |
                        [Sunlight/Exit]
                        
[Supply Store] — north — [Mess Room]
```

*Some doors are jammed. Some chests are locked. Some things are better left alone.*

---

## Roadmap

The dungeon is far from finished. Planned features in rough order:

- **Key & lock system** — find the key, open the west door, explore the mess room and supply store
- **Examine command** — inspect items in detail before deciding what to take
- **NPC encounters** — not everything down here is dead
- **Combat system** — some things are dead but won't stay that way
- **Traps** — that lever in the corridor does *something*
- **Browser port** — Kotlin/JS build for GitHub Pages
- **ASCII art** — because every good dungeon deserves a map you can see
- **Deeper dungeon** — whatever is in the dark beyond the mess room

---

## Built With

- [Kotlin](https://kotlinlang.org/)
- Gradle
- A probably unwise amount of atmospheric description