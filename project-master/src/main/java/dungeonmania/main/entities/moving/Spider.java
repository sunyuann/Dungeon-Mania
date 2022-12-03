package dungeonmania.main.entities.moving;

import java.util.List;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.entities.statics.Boulder;
import dungeonmania.main.movement_StrPattern.CircularMovement;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends BattleEntities implements MovingEntities {

    private static double DEFAULT_SPIDER_HEALTH = 70;
    private static double DEFAULT_SPIDER_ATTACK = 5;

    /**
     * Constructor for Spider
     * @param dungeon
     * @param entity
     */
    public Spider(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setHealth(DEFAULT_SPIDER_HEALTH);
        setAttack(DEFAULT_SPIDER_ATTACK);
        setMBehaviour(new CircularMovement());
    }

    /**
     * Constructor for Spider
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     */
    public Spider(Dungeon dungeon, String id, String type, Position position, Boolean isInteractable) {
        super(dungeon, id, type, position, isInteractable);
        setHealth(DEFAULT_SPIDER_HEALTH);
        setAttack(DEFAULT_SPIDER_ATTACK);
        setMBehaviour(new CircularMovement());
    }

    /**
     * Implements Abstract method in Entity
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            getDungeon().simulateBattle(this);
        }
    }

    /**
     * Implements MovingEntity interface method
     */
    public void move() {
        getMBehaviour().move(getDungeon(), this);
        getDungeon().updateEntities();
    }

    @Override
    public void setPosition(Direction direction) {
        // spider cant pass through boulders
        List<Entity> entitiesInDirection = getAdjacentEntitiesDirection().get(direction);
        for (Entity entity: entitiesInDirection) {
            if (entity instanceof Boulder) {
                return;
            }
        }
        super.setPosition(direction);
    }

    public String getId() {
        return super.getId();
    }
}
