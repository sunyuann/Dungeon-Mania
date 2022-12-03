package dungeonmania.main.entities.items;

import dungeonmania.main.Item;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.Player;
import dungeonmania.main.entities.collectable.Bomb;

public class BombItem extends Item implements SelectableItem {

    /**
     * Constructor for Bomb Item
     * @param dungeon
     * @param entity
     */
    public BombItem(Dungeon dungeon, Entity entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for Bomb Item
     * @param dungeon
     * @param item
     */
    public BombItem(Dungeon dungeon, ItemResponse item) {
        super(dungeon, item);
    }

    /**
     * Implements SelectableItem interface method
     * Drops a bomb entity at the player's position and sets the bomb's collectBehaviour to NoCollect 
     * so that it cannot be collected anymore
     */
    @Override
    public void select() {
        Player player = getDungeon().getPlayer();
        Bomb bomb = new Bomb(getDungeon(), getId(), getType(), player.getPosition(), new NoCollect());
        getDungeon().addDungeonEntity(bomb);
        getDungeon().removeFromInventory(this);
    }

}
