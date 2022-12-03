package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.WoodItem;
import dungeonmania.response.models.EntityResponse;

public class Wood extends Entity {

    /**
     * Performs the collect behaviour of Wood
     * This method is called when player moves
     */
    public Wood(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of Wood
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        WoodItem wood = new WoodItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, wood);
    }
}
