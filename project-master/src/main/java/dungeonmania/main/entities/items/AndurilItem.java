package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class AndurilItem extends Item implements ArmouryAttack {

    /**
     * Inital values for Anduril Item
     */
    public int attackPower = 2;
    private int durability = 25;

    /**
     * Constructor for Anduril Item
     * @param dungeon
     * @param entity
     */
    public AndurilItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Anduril Item
     * @param dungeon
     * @param item
     */
    public AndurilItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }


    /**
     * Implements ArmouryAttack interface method
     * Reduces durability of Anduril and returns the attack power
     * @return int corresponding to the attackPower
     */
    @Override
    public int useAttack() {
        durability--;
        if (durability == 0) {
            getDungeon().removeFromInventory(this);
        }
        return attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

}
