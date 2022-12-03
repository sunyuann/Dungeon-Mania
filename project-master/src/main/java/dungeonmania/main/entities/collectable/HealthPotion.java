package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.HealthPotionItem;
import dungeonmania.response.models.EntityResponse;

public class HealthPotion extends Entity {

    /**
     * Constructor for Health Potion
     * @param dungeon
     * @param entity
     */
    public HealthPotion(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of Health Potion
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        HealthPotionItem healthPotion = new HealthPotionItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, healthPotion);
    }
}
