package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.main.entities.Player;


public class HealthPotionItem extends Item implements SelectableItem {

    // Player's default health
    private static double DEFAULT_PLAYER_HEALTH = 100;
    
    /**
     * Constructor for Health Potion Item
     * @param dungeon
     * @param entity
     */
    public HealthPotionItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Health Potion Item
     */
    public HealthPotionItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Implements SelectableItem interface method
     * Sets the player's health to it's default
     * Removed from inventory after as it can only be used once
     */
    @Override
    public void select() {
        Player player = getDungeon().getPlayer();
        player.setHealth(DEFAULT_PLAYER_HEALTH);
        getDungeon().removeFromInventory(this);
    }


}
