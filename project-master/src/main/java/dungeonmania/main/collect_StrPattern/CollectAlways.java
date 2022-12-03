package dungeonmania.main.collect_StrPattern;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.Item;


// Concrete class of collecting behaviour
public class CollectAlways implements CollectBehaviour {
    
    public void collect(Dungeon dungeon, Entity entity, Item item) {
        
        dungeon.removeEntity(entity);
        dungeon.getDungeonInventory().addInventoryItem(item);
    }
}
