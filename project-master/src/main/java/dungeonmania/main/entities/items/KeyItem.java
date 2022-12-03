package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class KeyItem extends Item {
    
    /**
     * Constructor for Key Item
     * @param dungeon
     * @param entity
     */
    public KeyItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Key Item
     * @param dungeon
     * @param item
     */
    public KeyItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

}
