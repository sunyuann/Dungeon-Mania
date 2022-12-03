package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;

public class SunStoneItem extends Item {

    /**
     * Constructor for Sun Stone Item
     * @param dungeon
     * @param entity
     */
    public SunStoneItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Sun Stone Item
     * @param dungeon
     * @param item
     */
    public SunStoneItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }
}
