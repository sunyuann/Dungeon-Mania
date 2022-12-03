package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.response.models.ItemResponse;

public class BowItem extends Item implements ArmouryAttack {
    
    /**
     * Initial values for Bow Item
     */
    public int attackPower = 2;
    private int durability = 10;

    /**
     * Constructor for Bow Item
     * @param dungeon
     * @param item
     */
    public BowItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Reduces durability of Bow and returns the attack power
     * @return int corresponding to the attackPower
     */
    @Override
    public int useAttack() {
        // can only use bow if you have an arrow
        Item item = getDungeon().getDungeonInventory().getItemOfType("arrow");
        if (item != null) {
            durability--;
            // the used arrow is removed from the inventory
            getDungeon().removeFromInventory(item);
            if (durability == 0) {
                getDungeon().removeFromInventory(this);
            }
            return attackPower;
        }
        return 1;
    }

    public int getAttackPower() {
        return attackPower;
    }

}
