package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class WoodItem extends Item {
    
    /**
     * Constructor for Wood Item
     * @param dungeon
     * @param entity
     */
    public WoodItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Wood Item
     * @param dungeon
     * @param item
     */
    public WoodItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

}
