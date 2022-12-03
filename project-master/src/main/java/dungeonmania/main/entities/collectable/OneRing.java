package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.OneRingItem;
import dungeonmania.response.models.EntityResponse;

public class OneRing extends Entity {

    /**
     * Constructor for One Ring
     * @param dungeon
     * @param entity
     */
    public OneRing(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity, new CollectAlways());
    }

    /**
     * Performs the collect behaviour of One Ring
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        OneRingItem oneRingItem = new OneRingItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, oneRingItem);
    }
}
