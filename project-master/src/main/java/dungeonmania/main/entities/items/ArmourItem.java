package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class ArmourItem extends Item implements ArmouryDefense {

    /**
     * Initial values for Armour Item
     */
    private int defensePower = 2;
    private int durability = 20;

    /**
     * Constructor for Armour Item
     * @param dungeon
     * @param entity
     */
    public ArmourItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Armour Item
     */
    public ArmourItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Implements ArmouryDefense interface method
     * Reduces durability of Armour and returns the defense power
     * @return int corresponding to the defensePower
     */
    @Override
    public int useDefense() {
        durability--;
        if (durability == 0) {
            getDungeon().removeFromInventory(this);
        }   
        return defensePower;     
    }

    public int getDefensePower() {
        return defensePower;
    }
    
}
