package dungeonmania.main.collect_StrPattern;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.Item;

public class CollectOnce implements CollectBehaviour {
    
    public void collect(Dungeon dungeon, Entity entity, Item item) {

        // check the inventory to see if the item already exists in it
        for (Item inInventory : dungeon.getDungeonInventory().getDungeonInventory()) {
            
            // in the case that item is a key, only one of either colour can be collected
            if (inInventory.getType().equals("key_gold") || inInventory.getType().equals("key_silver")) {
                return;
            }

            // all other cases
            if (inInventory.getType().equals(item.getType())) {
                return;
            }
            
        }

        // if no matches found, we can remove the entity from dungeon and add item to inventory
        dungeon.removeEntity(entity);
        dungeon.addToInventory(item);
    }
}