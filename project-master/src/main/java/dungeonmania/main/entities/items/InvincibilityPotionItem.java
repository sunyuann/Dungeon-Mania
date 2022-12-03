package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.main.entities.Player;

public class InvincibilityPotionItem extends Item implements SelectableItem {
    
    /**
     * Lasting time of Invincibility Potion
     */
    private static int INVINCIBILITY_LASTING_TIME = 10; 

    /**
     * Constructor of Invincibility Potion Item
     * @param dungeon
     * @param entity
     */
    public InvincibilityPotionItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor of Invincibility Potion Item
     * @param dungeon
     * @param item
     */
    public InvincibilityPotionItem(Dungeon dungeon, ItemResponse item) {
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
        player.addToInvincibleTicks(INVINCIBILITY_LASTING_TIME);
        getDungeon().removeFromInventory(this);
    }
    

}
