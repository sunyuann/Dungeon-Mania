package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.entities.Player;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class OneRingItem extends Item {

    private static double DEFAULT_PLAYER_HEALTH = 100;

    /**
     * Constructor for One Ring Item
     * @param dungeon
     * @param entity
     */
    public OneRingItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }
    /**
     * Constructor for One Ring Item
     * @param dungeon
     * @param item
     */
    public OneRingItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Uses the oneRing - sets the player's health to 100;
     * Automatically called when a player's health becomes <= in a battle
     */
    public void useOneRing() {
        Player player = getDungeon().getPlayer();
        player.setHealth(DEFAULT_PLAYER_HEALTH);
        getDungeon().getDungeonInventory().removeFromInventory(this);
    }
}
