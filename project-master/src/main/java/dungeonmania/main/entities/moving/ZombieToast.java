package dungeonmania.main.entities.moving;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.movement_StrPattern.RandomMovement;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class ZombieToast extends BattleEntities implements MovingEntities {

    // default attributes of zombies
    private static double DEFAULT_ZOMBIETOAST_HEALTH = 90;
    private static double DEFAULT_ZOMBIETOAST_ATTACK = 3;

    /**
     * Constructor of Zombie
     * @param dungeon
     * @param entity
     */
    public ZombieToast(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setHealth(DEFAULT_ZOMBIETOAST_HEALTH);
        setAttack(DEFAULT_ZOMBIETOAST_ATTACK);
        setMBehaviour(new RandomMovement());
    }

    /**
     * Constructor of Zombie
     */
    public ZombieToast(Dungeon dungeon, String id, String type, Position position, Boolean isInteractable) {
        super(dungeon, id, type, position, isInteractable);
        setHealth(DEFAULT_ZOMBIETOAST_HEALTH);
        setAttack(DEFAULT_ZOMBIETOAST_ATTACK);
        setMBehaviour(new RandomMovement());
    }

    /**
     * Implements abstract method of Entity
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            getDungeon().simulateBattle(this);
        }
    }

    /**
     * Implements MovingEntities interface method
     */
    public void move() {
        getMBehaviour().move(getDungeon(), this);
        getDungeon().updateEntities();
    }

    public String getId() {
        return super.getId();
    }
}
