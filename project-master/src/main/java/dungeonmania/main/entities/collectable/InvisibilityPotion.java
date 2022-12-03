package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.InvisibilityPotionItem;
import dungeonmania.response.models.EntityResponse;

public class InvisibilityPotion extends Entity {

    /**
     * Constructor for Invisibility Potion
     * @param dungeon
     * @param entity
     */
    public InvisibilityPotion(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of Invisibility Potion
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        InvisibilityPotionItem invisibilityPotion = new InvisibilityPotionItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, invisibilityPotion);
    }
}
