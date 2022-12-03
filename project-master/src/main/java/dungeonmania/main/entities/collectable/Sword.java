package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.SwordItem;
import dungeonmania.response.models.EntityResponse;

public class Sword extends Entity {

    /**
     * Constructor for Sword
     * @param dungeon
     * @param entity
     */
    public Sword(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of Sword
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        SwordItem sword = new SwordItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, sword);
    }
}
