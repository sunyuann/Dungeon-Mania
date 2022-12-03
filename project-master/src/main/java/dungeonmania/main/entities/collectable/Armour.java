package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.main.entities.items.ArmourItem;

public class Armour extends Entity {

    /**
     * Armour Constructor
     * @param dungeon
     * @param entity
     */
    public Armour(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of armour
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        ArmourItem a = new ArmourItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, a);
    }
}
