package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.TreasureItem;
import dungeonmania.response.models.EntityResponse;

public class Treasure extends Entity {


    /**
     * Constructor for Treasure
     * @param dungeon
     * @param entity
     */
    public Treasure(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of Treasure
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        TreasureItem treasure = new TreasureItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, treasure);
    }

}
