package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.KeyItem;
import dungeonmania.response.models.EntityResponse;

public class Key extends Entity {

    /**
     * Constructor for Key
     * @param dungeon
     * @param entity
     */
    public Key(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectOnce());
    }

    /**
     * Performs the collect behaviour of Key
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        KeyItem key = new KeyItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, key);
    }
}
