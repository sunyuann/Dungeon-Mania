package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.main.entities.items.ArrowItem;

public class Arrow extends Entity {

    /**
     * Arrow Constructor
     * @param dungeon
     * @param entity
     */
    public Arrow(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behavioru of arrow
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        ArrowItem a = new ArrowItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, a);
    }
}
