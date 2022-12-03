package dungeonmania.main;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.entities.items.*;
import dungeonmania.main.entities.moving.Assassin;
import dungeonmania.main.entities.moving.Hydra;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.exceptions.InvalidActionException;

public class Inventory {

    /**
     * Attributes of Inventory
     */
    Dungeon dungeon;
    private List<Item> dungeonInventory = new ArrayList<Item>();
    private List<ArmouryAttack> attackWeapons = new ArrayList<ArmouryAttack>();
    private List<ArmouryDefense> defenseWeapons = new ArrayList<ArmouryDefense>();

    /**
     * Constructor for Inventory
     * @param dungeon
     * @param itemResponses
     */
    public Inventory(Dungeon dungeon, List<ItemResponse> itemResponses) {
        this.dungeon = dungeon;
        for (ItemResponse items : itemResponses) {
            createItem(dungeon, items);
        }
    }

    /**
     * A method to create a new item class using switch statements
     * 
     * @param item
     */
    public void createItem(Dungeon dungeon, ItemResponse item) {
        switch (item.getType()) {
            case "armour":
                ArmourItem a = new ArmourItem(dungeon, item);
                addInventoryItem(a);
                break;
            case "arrow":
                ArrowItem arrow = new ArrowItem(dungeon, item);
                addInventoryItem(arrow);
                break;
            case "bomb":
                BombItem bomb = new BombItem(dungeon, item);
                addInventoryItem(bomb);
                break;
            case "health_potion":
                HealthPotionItem hP = new HealthPotionItem(dungeon, item);
                addInventoryItem(hP);
                break;
            case "invincibility_potion":
                InvincibilityPotionItem iP = new InvincibilityPotionItem(dungeon, item);
                addInventoryItem(iP);
                break;
            case "invisibility_potion":
                InvisibilityPotionItem iP2 = new InvisibilityPotionItem(dungeon, item);
                addInventoryItem(iP2);
                break;
            case "key_gold":
                KeyItem k_g = new KeyItem(dungeon, item);
                addInventoryItem(k_g);
                break;
            case "key_silver":
                KeyItem k_s = new KeyItem(dungeon, item);
                addInventoryItem(k_s);
                break;
            case "sword": 
                SwordItem sword = new SwordItem(dungeon, item);
                addInventoryItem(sword);
                break;
            case "treasure":
                TreasureItem t = new TreasureItem(dungeon, item);
                addInventoryItem(t);
                break;
            case "wood":
                WoodItem w = new WoodItem(dungeon, item);
                addInventoryItem(w);
                break;
            case "one_ring":
                OneRingItem o = new OneRingItem(dungeon, item);
                addInventoryItem(o);
                break;
            case "bow":
                BowItem b = new BowItem(dungeon, item);
                addInventoryItem(b);
                break;
            case "shield":
                ShieldItem s = new ShieldItem(dungeon, item);
                addInventoryItem(s);
                break;
            case "sun_stone":
                SunStoneItem ss = new SunStoneItem(dungeon, item);
                addInventoryItem(ss);
                break;
            case "sceptre":
                SceptreItem sceptre = new SceptreItem(dungeon, item);
                addInventoryItem(sceptre);
                break;
            case "midnight_armour":
                MidnightArmourItem midnightArmour = new MidnightArmourItem(dungeon, item);
                addInventoryItem(midnightArmour);
                break;
            case "anduril":
                AndurilItem anduril = new AndurilItem(dungeon, item);
                addInventoryItem(anduril);
                break;

        }
        
    }

    public int destroySpawner() {
        // take the first available weapon and use its attack.
        for (ArmouryAttack attackItem : attackWeapons) {
            return attackItem.useAttack();
        }

        // if there are no weapons, just return 1
        return 1;
    }

    public int battleAttack(BattleEntities battleEntity) {
        if (battleEntity instanceof Assassin || battleEntity instanceof Hydra) {
            Item item = getItemOfType("anduril");
            if (item != null) {
                int attack = ((AndurilItem) item).useAttack()*3;
                return attack;
            }
        }
        for (ArmouryAttack attackItem : attackWeapons) {
            return attackItem.useAttack();
        }

        // if there are no weapons, just return 1
        return 1;
    }

    public int battleDefense() {
        // take the first available weapon and use its defense.
        for (ArmouryDefense defenseItem : defenseWeapons) {
            return defenseItem.useDefense();
        }

        // if there are no weapons, just return 1
        return 1;
    }  

    /**
     * add items to inventory
     */
    public void addInventoryItem(Item item) {
        if (item instanceof ArmouryAttack) {
            attackWeapons.add((ArmouryAttack) item);
        }
        if (item instanceof ArmouryDefense) {
            defenseWeapons.add((ArmouryDefense) item);
        }
        dungeonInventory.add(item);
    }

    /**
     * remove items from inventory
     * 
     * @param item
     */
    public void removeFromInventory(Item item) {
        if (item instanceof ArmouryAttack) {
            attackWeapons.remove((ArmouryAttack) item);
        }
        if (item instanceof ArmouryDefense) {
            defenseWeapons.remove((ArmouryDefense) item);
        }
        dungeonInventory.remove(item);
    }

    /**
     * remove an item from the inventory with the given type
     * returns true for successful removal
     */
    public boolean removeFromInventoryOfType(String type) {

        Item item = getItemOfType(type);
        if (item != null) {
            removeFromInventory(item);
            return true;
        }

        return false;
    }

    /**
     * Getter for inventory
     * 
     * @return
     */
    public List<Item> getDungeonInventory() {
        return dungeonInventory;
    }

    /**
     * Getter for armoury attack items
     * @return
     */
    public List<ArmouryAttack> getArmouryAttackItems() {
        return attackWeapons;
    }

    /**
     * Getter for armoury attack items
     * @return
     */
    public List<ArmouryDefense> getArmouryDefenseItems() {
        return defenseWeapons;
    }


    /**
     * Returns an item of the given type
     * 
     * @param type
     * @return item if it exists, otherwise returns null
     */
    public Item getItemOfType(String type) {

        for (Item item : dungeonInventory) {
            if (item.getType().equals(type)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Returns an item of the given type
     * @param type
     * @return item if it exists, otherwise returns null
     */
    public KeyItem getKey() {

        for (Item item: dungeonInventory) {
            if (item.getType().equals("key_gold") || item.getType().equals("key_silver")) {
                return ((KeyItem) item);
            }
        }

        return null;
    }


    /**
     * Helper function that returns a list of items in the form of ItemResponses
     * 
     * @return
     */
    public List<ItemResponse> getInventoryResponse() {
        List<ItemResponse> inventory = new ArrayList<ItemResponse>();
        for (Item item : dungeonInventory) {
            inventory.add(item.getItemResponse());
        }
        return inventory;
    }

    /**
     * Helper function: given an itemused string, check for exceptions then use the
     * selected item precondition itemUsed != null
     * 
     * @param currDungeon
     * @param itemUsed
     */
    public void useSelectedItem(String itemUsed) {
        SelectableItem selected = null;
        Item givenItem = null;
        for (Item item : dungeonInventory) {
            if (item.getId().equals(itemUsed)) {
                givenItem = item;
            }
        }
        if (givenItem == null) {
            throw new InvalidActionException(itemUsed);
        } else if (!(givenItem instanceof SelectableItem)) {
            throw new IllegalArgumentException(itemUsed);
        }

        selected = (SelectableItem) givenItem;

        // use selected item
        if (selected != null) {
            selected.select();
        }

    }

    public void updateItems() {
        int size = dungeonInventory.size();
        for (int i = 0; i < size; i++) {
            dungeonInventory.get(i).update();
            // if an entity was removed, readjust index accordingly
            if (dungeonInventory.size() < size) {
                size = dungeonInventory.size();
                i--;
            }
        }
    }

    /**
     * Helper function: check if the inventory has a one ring
     * 
     * @param dungeon
     */
    public OneRingItem checkForOneRing() {
        for (Item items : dungeonInventory) {
            if (items.getType().equals("one_ring")) {
                return ((OneRingItem) items);
            }
        }
        return null;
    }


    public boolean hasAnduril() {
        Item item = getItemOfType("anduril"); 
        if (item != null) {
            return true;
        }

        return false;
    }

    public int getNumItemsOfType(String type) {
        int count = 0;
        for (Item item : dungeonInventory) {
            if (item.getType().equals(type)) {
                count++;
            }
        }
        return count;
    }
}
