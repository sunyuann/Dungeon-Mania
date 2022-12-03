package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class SwordItem extends Item implements ArmouryAttack {
    
    /**
     * Intial values for Sword Item
     */
    public int attackPower = 2;
    private int durability = 20;

    /**
     * Constructor for Sword Item
     * @param dungeon
     * @param entity
     */
    public SwordItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Sword Item
     * @param dungeon
     * @param item
     */
    public SwordItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Implements ArmouryAttack interface method
     * @return int of the sword's attack power
     */
    @Override
    public int useAttack() {
        durability--;
        if (durability == 0) {
            // remove from armoury list in dungeon
            getDungeon().getDungeonInventory().removeFromInventory(this);
        }
        return attackPower;
    }

    public double getAttackPower() {
        return attackPower;
    }
}