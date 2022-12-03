package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class TreasureItem extends Item {

    /**
     * Constructor of Treasure Item
     * @param dungeon
     * @param entity
     */
    public TreasureItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor of Treasure Item
     * @param dungeon
     * @param item
     */
    public TreasureItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }
   
}
