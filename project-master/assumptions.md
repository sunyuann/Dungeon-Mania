**_General Assumptions - Milestones 1 & 2_**
* For now, there are only 2 doors and keys - gold and silver will map to each other, but there won’t be any more
* Only one piece of gold is needed to bribe the mercenary

**_Creating / Saving / Loading games_**

**_Entities/Items_**
**Movement - moving entities, spawning movements**
* Spiders cannot spawn at the border of the map. This way, its circular movement will not bring it out of the map.
* Allies do not contribute to the player's attack, but will not attack the player
* The dungeon's width and height changes after every entity movement, and the top left corner will at least be (0, 0) (can be negative)
* If a randomly generated location for a spawnable entity cannot be found, it will spawn at (0, 0)
* Only spawn on squares that are fully vacant (not including switches etc.).
* Not random position spawn. Spawns on the next available cardinal position.

**Collectables - Collectable entities, Inventory**
A map can only have one oneRing - this is what makes it ‘rare’

**Items - item behaviour**
* Bomb blast radius = 1 UNIT CIRCLE
* Bomb Detonation behaviour: Even if bomb is in the correct position to detonate (i.e adjacent floor switch is triggered), if it has not been collected yet it should NOT detonate
* All entities other than player are removed by bomb detonation (even player's allies)
* Invincibility Potion: ‘Enemies will run away when the character is invincible’ - we assume that the enemy running away means that it is removed from the game & entity list
* The one ring (rare collectible item) is automatically used if it is in the inventory when the character dies
* Some games give a choice but that's usually for items you can save up like gems in subway surfers - this item cannot be saved for future games hence we will not be giving a choice
* Items used in battle (i.e. Sword, Shields, Armour, Bow, Midnight Armour, Anduril) will be automatically selected according to the following rules:
- If battling a boss and the player has an anduril, it will always be used.
- If not battling a boss, or the player does not have an anduril, the first available weapon in the inventory list will be selected.
* When you use a bow once, you remove an arrow from the inventory - if there are no more arrows, the bow does not worke and is removed as well.
Buildable
* If the player collects a treasure and key before collecting 2 woods, the build will automatically take the treasure to make the shield
    * We make this assumption so that the player can still use the key to open the door if needed
* Only one item of one type can be built at a time
    * E.g. If there are 5 treasures and 6 wood in the inventory, the build list should only show one shield available to build. After this shield is    built, another one may appear in the build list.
* Sun stone is only used for bribing, building and opening doors if there are no alternatives (no treasure/ key)
* Sun stone cannot be used in place of treasure for completing goals e.g. if the goal is to collect all treasure, the player cannot simply collect a sun stone and none of the treasure and complete the goal. The player does not need to collect a sun stone to complete the goal either.
* If player has a sceptre in their inventory, they cannot bribe a mercenary or assassin - they will only mind control first. Once the sceptre is used, it is removed and then they can bribe the next player normally as long as they don't have another sceptre

_Durability of items_
* Key - used once (usage is automatic)
* Health Potion - can only use once 
* Invincibility Potion - can only use once; once taken out of inventory, will only work for 10 ticks
* Invisibility Potion - can only use once; once taken out of inventory, will only work for 10 ticks
* Bomb: can only be used once
* Bows (+ Arrows): requires arrows to be used; can only be used 10 times (so requires 10 arrows for full use); after one use, the corresponding arrow is removed from the inventory
* Swords - deteriorates per use; cannot be used after 20 uses
* Shield: deteriorates per hit; cannot be used after 10 hits 
* Armour: deteriorates per hit; cannot be used after 20 hits 
* Sceptre: sceptre can only be used once for mind control, is then removed from inventory
* Anduril: deteriorates per hit; cannot be used after 25 hits
* Midnight Armour: deteriorates per hit and defense; cannot be used after 30 hits

_Building Items_
* InvalidActionException is thrown if player has sufficient items to build a midnight armour but a zombie is present in the dungeon


**Static - static entities**
* Closed doors are not passable, open doors are passable
* Zombie Toast Spawners will only spawn on fully vacant squares (can’t spawn on squares with a passable entity)
* Zombie Toast Spawners will spawn on the next available square, not on a random available square.
* When you destroy a zombie toast spawner, all the weapons you have are used in that attempt to destroy it (i.e. durability goes down by 1 for each)
* Portals teleport to an available spot adjacent to the other portal
* Portals will not teleport to a blocked portal
* Swamp tiles delay an entity's movement by 1 tick, while 30% of swamp tiles delay by 2 ticks

**_Battles_**
* Set a specific max health number for battle entities:
    * Player: 100
    * Assassin: 150
    * Hydra: 90
    * Mercenary: 90
    * Zombie: 90
    * Spider: 70
* Set a specific max attack power for battle entities:
    * Player: 3
    * Assassin: 20
    * Hydra: 9
    * Mercenary: 10
    * Zombie: 3
    * Spider: 5
