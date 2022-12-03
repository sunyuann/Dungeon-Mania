package dungeonmania.main;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import dungeonmania.main.entities.items.BowItem;
import dungeonmania.main.entities.items.MidnightArmourItem;
import dungeonmania.main.entities.items.SceptreItem;
import dungeonmania.main.entities.items.ShieldItem;
import dungeonmania.response.models.ItemResponse;

public class Builder {

    // attributes
    Dungeon dungeon;
    Inventory inventory;
    List<String> buildable;

    /**
     * Builder Constructor
     * @param dungeon
     * @param inventory
     */
    public Builder(Dungeon dungeon, Inventory inventory) {
        this.dungeon = dungeon;
        this.inventory = inventory;
        this.buildable = getBuildable();
    }


    /**
     * Returns a string of the buildables that can be made from inventory items
     * Either contains just shield, just bow or both shield and bow
     * @return List<String>
     */
    public List<String> getBuildable() {
        updateBuildable();
        return buildable;

    }


    /**
     * returns buildabe
     * @param buildables
     */
    public void setBuildables(List<String> buildables) {
        buildable = buildables;
    }


    /**
     * updates and creates the buildable list
     */
    public void updateBuildable() {

        List<String> newBuildable = new ArrayList<>();

        // all items used to build
        int numArrows = inventory.getNumItemsOfType("arrow");
        int numTreasure = inventory.getNumItemsOfType("treasure");
        int numWood = inventory.getNumItemsOfType("wood");
        int numKeyGold = inventory.getNumItemsOfType("key_gold");        
        int numKeySilver = inventory.getNumItemsOfType("key_silver");
        int numSunStone = inventory.getNumItemsOfType("sun_stone");
        int numArmour = inventory.getNumItemsOfType("armour");

        // different types of keys can be used interchangeably
        int numTotalKeys = numKeyGold + numKeySilver;

        // sunstone and treasure can be used interchangeably
        int numTreasureAndSunStone = numTreasure + numSunStone;

        if (numArrows >= 3 && numWood >= 1) {
            newBuildable.add("bow");
        }

        if (numWood >= 2 && (numTreasureAndSunStone >= 1 || numTotalKeys >= 1)) {
            newBuildable.add("shield");
        }

        // an note regarding numTreasureAndSunStone >= 2: 
        // if there is only one treasure - can't build; 
        // if there are two sunstones - can build; 
        // if there are two treasures - can't build - this is checked by numSunStone >= 1
        // if there is one treasure and one sun_stone - can build
        if ((numWood >= 1 || numArrows >= 2) && (numTotalKeys >= 1 || numTreasureAndSunStone >= 2) && (numSunStone >= 1)) {
            newBuildable.add("sceptre");
        }

        // check no zombies in the map
        if (numArmour >= 1 && numSunStone >= 1 && dungeon.getNumEntities("zombie") == 0) {
            newBuildable.add("midnight_armour");
        }

        setBuildables(newBuildable);

    }


    // Pre-Condition: the given 'item' is contained in buildables and can be made by it
    public void makeBuildable(String item) {
        
        if (item.equals("bow")) {
            makeBow();
        } else if (item.equals("shield")) {
            makeShield();
        } else if (item.equals("sceptre")) {
            makeSceptre();
        } else if (item.equals("midnight_armour")) {
            makeMidnightArmour();
        }
    }


     /**
     * Pre-Condition: there are enough items in the inventory to make the bow
     * makes a bow and removes the required items from the inventory
     */
    public void makeBow() {
        inventory.removeFromInventoryOfType("wood");
        inventory.removeFromInventoryOfType("arrow");
        inventory.removeFromInventoryOfType("arrow");
        inventory.removeFromInventoryOfType("arrow");

        BowItem bow = new BowItem(dungeon, new ItemResponse("bow-" + new Date().getTime(), "bow"));
        inventory.addInventoryItem(bow);
 
    }

    /**
     * Pre-Condition: there are enough items in the inventory to make the shield
     * makes a shield and removes the required items from the inventory
     */
    public void makeShield() {

        // remove the wood items required 
        inventory.removeFromInventoryOfType("wood");
        inventory.removeFromInventoryOfType("wood");

        // check the number of other items that can be used to build a shield
        int numTreasure = inventory.getNumItemsOfType("treasure");
        // int numSunStone = inventory.getNumItemsOfType("sun_stone");
        int numKeyGold = inventory.getNumItemsOfType("key_gold");        
        int numKeySilver = inventory.getNumItemsOfType("key_silver");

        if (numTreasure >= 1) { 
            inventory.removeFromInventoryOfType("treasure");
        } else if (numKeyGold >= 1) {
            inventory.removeFromInventoryOfType("key_gold");
        } else if (numKeySilver >= 1) {
            inventory.removeFromInventoryOfType("key_silver");
        } else {
            // sunstone is automatically used if needed - based on pre-condition
        }

        ShieldItem shield = new ShieldItem(dungeon, new ItemResponse("shield-" + new Date().getTime(), "shield"));
        inventory.addInventoryItem(shield);
    }


    /**
     * Pre-Condition: there are enough items in the inventory to build the sceptre
     * makes a sceptre and removes the required items from the inventory
     */
    public void makeSceptre() {

        // remove either wood or arrows

        int numWood = inventory.getNumItemsOfType("wood");
        int numArrows = inventory.getNumItemsOfType("arrow");

        // remove first section of items
        if (numWood >= 1) {
            inventory.removeFromInventoryOfType("wood");
        } else if (numArrows >= 2) {
            inventory.removeFromInventoryOfType("arrow");
            inventory.removeFromInventoryOfType("arrow");
        }

  
        // check the number of other items that can be used to build a sceptre
        int numTreasure = inventory.getNumItemsOfType("treasure");
        // int numSunStone = inventory.getNumItemsOfType("sun_stone");
        int numKeyGold = inventory.getNumItemsOfType("key_gold");        
        int numKeySilver = inventory.getNumItemsOfType("key_silver");

        if (numTreasure >= 1) { 
            inventory.removeFromInventoryOfType("treasure");
        } else if (numKeyGold >= 1) {
            inventory.removeFromInventoryOfType("key_gold");
        } else if (numKeySilver >= 1) {
            inventory.removeFromInventoryOfType("key_silver");
        }
        // presume sun_stone(s) are automatically used if they are needed (based on pre-condition)

        SceptreItem sceptre = new SceptreItem(dungeon, new ItemResponse("sceptre-" + new Date().getTime(), "sceptre"));
        inventory.addInventoryItem(sceptre);
    }

    /**
     * Pre-Condition: there are enough items in the inventory to make the midnight_armour
     */
    public void makeMidnightArmour() {
        inventory.removeFromInventoryOfType("armour");
        MidnightArmourItem midnightArmour = new MidnightArmourItem(dungeon, new ItemResponse("midnight_armour-" + new Date().getTime(), "midnight_armour"));
        inventory.addInventoryItem(midnightArmour);
    }
    
}
