package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.response.models.ItemResponse;

public class MidnightArmourItem extends Item implements ArmouryAttack, ArmouryDefense {
    
    /**
     * Initial values for Midnight Armour Item
     */
    private int attackPower = 2;
    private int defensePower = 2;
    private int durability = 30;

    /**
     * Constructor for Midnight Armour Item
     * @param dungeon
     * @param item
     */
    public MidnightArmourItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Implements ArmouryAttack interface method
     * Reduces durability after use and returns attackPower
     * @return int with the attack power
     */
    @Override
    public int useAttack() {
        durability--;
        if (durability == 0) {
            getDungeon().removeFromInventory(this);
        }
        return attackPower;
    }

    /**
     * Implements ArmouryDefense interface method
     * Reduces durability after use and returns defensePower
     * @return int with the defense power
     */
    @Override
    public int useDefense() {
        durability--;
        if (durability == 0) {
            getDungeon().removeFromInventory(this);
        }   
        return defensePower;     
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefensePower() {
        return defensePower;
    }
}
