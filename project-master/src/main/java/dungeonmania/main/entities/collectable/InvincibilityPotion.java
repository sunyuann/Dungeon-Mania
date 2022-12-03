package dungeonmania.main.entities.collectable;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.items.InvincibilityPotionItem;
import dungeonmania.response.models.EntityResponse;


public class InvincibilityPotion extends Entity {

     /**
      * Constructor for Invincibility Potion
      * @param dungeon
      * @param entity
      */
    public InvincibilityPotion(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setCBehaviour(new CollectAlways());
    }

    /**
     * Performs the collect behaviour of Invincibility Potion
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        InvincibilityPotionItem invincibilityPotion = new InvincibilityPotionItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, invincibilityPotion);
    }
}
