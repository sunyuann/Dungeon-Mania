package dungeonmania.main.entities;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class BattleEntities extends Entity {

    private double health;
    private double attack;

    /**
     * Constructor for Battle Entity
     * @param dungeon
     * @param entity
     */
    public BattleEntities(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
    }

    /**
     * Constructor for BattleEntity
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     */
    public BattleEntities(Dungeon dungeon, String id, String type, Position position, boolean isInteractable) {
        super(dungeon, id, type, position, isInteractable);
    }

    /**
     * battle function, simulates battle with battleEntity returns 0 if battlewon, 1
     * if battlelost, 2 if battle skipped
     * 
     * @param battleEntity
     * @return
     */
    public int battle(BattleEntities battleEntity) {
        // default battle function for curr(this) vs enemy(battleEntity)
        if (!(battleEntity instanceof Player)) {
            // since battle entities can only battle player or ally,
            // if enemy is not a player or ally then return 2
            // TODO: ally stuff - (keshiga - make assumption that only battle against players (allies not involved))
            return 2;
        }
        for (;;) { // infinite loop
            // check if battle has finished
            if (battleEntity.getHealth() <= 0) {
                // returns won if enemy health reduces to or below zero
                return 0;
            }

            // Curr Health = Curr Health - ((Enemy Health * Enemy Attack Damage) / 5)
            double currHealth = this.health - (((battleEntity.getHealth()*battleEntity.getAttack()*(getDungeon().getDungeonInventory().battleAttack(this)))/5));
            this.setHealth(currHealth);

            if (this.health <= 0) {
                // returns lost if enemy still alive and curr health reduces to or below zero
                return 1;
            }

            // set enemy and curr health after 1 turn (attack each other)
            // Enemy Health - ((Curr Health * Curr Attack Damage) / 10)
            double enemyHealth = battleEntity.getHealth() - ((this.health*this.attack/getDungeon().getDungeonInventory().battleDefense())/10);
            battleEntity.setHealth(enemyHealth);
        }

    }

    // Getters 
    public double getHealth() {
        return this.health;
    }

    public double getAttack() {
        return this.attack;
    }

    // Setters
    public void setAttack(double attack) {
        this.attack = attack;
    }

    public void setHealth(double health) {
        this.health = health;
    }

}
