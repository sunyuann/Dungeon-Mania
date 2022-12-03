package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.entities.Player;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class InvisibilityPotionItem extends Item implements SelectableItem {

    /**
     * Lasting time of Invisibility Potion
     */
    private static int INVISIBILITY_LASTING_TIME = 10; 

    
    /**
     * Constructor for Invisibility Potition Item
     * @param dungeon
     * @param entity
     */
    public InvisibilityPotionItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Invisibility Potion Item
     * @param dungeon
     * @param item
     */
    public InvisibilityPotionItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Implements SelectableItem interface method
     * Adds to player's current invincible ticks by INVINSIBILITY_LASTING_TIME
     * Removed from inventory after use as it can only be used once
     */
    @Override
    public void select() {
        Player player = getDungeon().getPlayer();
        player.addToInvisibleTicks(INVISIBILITY_LASTING_TIME);
        getDungeon().removeFromInventory(this);
    }
    
}
