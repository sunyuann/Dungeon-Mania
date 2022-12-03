package dungeonmania.main.entities.moving;

import java.util.Random;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.entities.Player;
import dungeonmania.main.movement_StrPattern.RandomMovement;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Hydra extends BattleEntities implements MovingEntities {


    private static double DEFAULT_HYDRA_HEALTH = 90;
    private static double DEFAULT_HYDRA_ATTACK = 9;

    /**
     * Constructor for Hydra
     * @param dungeon
     * @param entity
     */
    public Hydra(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setHealth(DEFAULT_HYDRA_HEALTH);
        setAttack(DEFAULT_HYDRA_ATTACK);
        setMBehaviour(new RandomMovement());
    }

    /**
     * Constructor for Hydra
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     */
    public Hydra(Dungeon dungeon, String id, String type, Position position, Boolean isInteractable) {
        super(dungeon, id, type, position, isInteractable);
        setHealth(DEFAULT_HYDRA_HEALTH);
        setAttack(DEFAULT_HYDRA_ATTACK);
        setMBehaviour(new RandomMovement());
    }

    /**
     * Implements Abstract method of Entity
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            getDungeon().simulateBattle(this);
        }
    }

    /**
     * moves entity
     */
    public void move() {
        getMBehaviour().move(getDungeon(), this);
        getDungeon().updateEntities();
    }

    /**
     * battle function, simulates battle with battleEntity
     * returns 0 if battlewon, 1 if battlelost, 2 if battle skipped
     * @param battleEntity
     * @return
     */
    public int battle(BattleEntities battleEntity) {
        // default battle function for curr(this) vs enemy(battleEntity)
        if (!(battleEntity instanceof Player)) {
            // TODO: ally stuff
            return 2;
        }
        for (;;) { // infinite loop
            // check if battle has finished
            if (battleEntity.getHealth() <= 0) { 
                // returns won if enemy health reduces to or below zero
                return 0;
            }

            // randomly generate either 0 or 1 
            int coinFlip = new Random().nextInt(2);

            // check hasAnduril - anduril will automatically be used
            boolean hasAnduril = getDungeon().getDungeonInventory().hasAnduril();

            // Curr Health = Curr Health - ((Enemy Health * Enemy Attack Damage) / 5)
            double enemyAttackDamage = (((battleEntity.getHealth()*battleEntity.getAttack()*(getDungeon().getDungeonInventory().battleAttack(this)))/5));
            if (coinFlip == 0 || hasAnduril) { // if heads or an anduril was used in battle, health decreases by enemy attack damage
                this.setHealth(this.getHealth() - enemyAttackDamage);
            } else if (coinFlip == 1) { // if tails, health increases by enemy attack damage
                this.setHealth(this.getHealth() + enemyAttackDamage);
            }

            if (this.getHealth() <= 0) { 
                // returns lost if enemy still alive and curr health reduces to or below zero
                return 1;
            }

            // set enemy and curr health after 1 turn (attack each other)
            // Enemy Health - ((Curr Health * Curr Attack Damage) / 10)
            double enemyHealth = battleEntity.getHealth() - ((this.getHealth()*this.getAttack()/getDungeon().getDungeonInventory().battleDefense())/10);
            battleEntity.setHealth(enemyHealth);
        }
    }


    // Setters and Getters

    
    public String getId() {
        return super.getId();
    }

    @Override
    public void setHealth(double health) {
        // health set for Hydra cannot exceed default hydra health (maximum hp)
        if (health <= DEFAULT_HYDRA_HEALTH) {
            super.setHealth(health);
            return;
        }
        super.setHealth(DEFAULT_HYDRA_HEALTH);
    }
}
