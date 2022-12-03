package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.response.models.ItemResponse;

public class ShieldItem extends Item implements ArmouryDefense  {
    
    /**
     * Initial values for Shield Item
     */
    public int defensePower = 2;
    private int durability = 10;

    /**
     * Constructor for Shield Item
     * @param dungeon
     * @param item
     */
    public ShieldItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }
    
    /**
     * Implements ArmouryDefense interface method
     * @return int with the defense power of the shield
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
