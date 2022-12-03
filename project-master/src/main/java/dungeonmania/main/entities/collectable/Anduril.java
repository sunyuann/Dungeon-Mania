package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.AndurilItem;
import dungeonmania.response.models.EntityResponse;

public class Anduril extends Entity {

    /**
     * Anduril constructor
     * @param dungeon
     * @param entity
     */
    public Anduril(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of anduril
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        AndurilItem anduril = new AndurilItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, anduril);
    }
}
