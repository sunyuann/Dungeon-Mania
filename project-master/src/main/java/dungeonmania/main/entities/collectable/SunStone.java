package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.SunStoneItem;
import dungeonmania.response.models.EntityResponse;

public class SunStone extends Entity{

    /**
     * Constructor for Sun Stone
     * @param dungeon
     * @param entity
     */
    public SunStone(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of Sun Stone
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        SunStoneItem sunStone = new SunStoneItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, sunStone);
    }
}
