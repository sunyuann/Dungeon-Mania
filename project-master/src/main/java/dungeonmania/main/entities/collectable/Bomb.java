package dungeonmania.main.entities.collectable;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.collect_StrPattern.*;
import dungeonmania.main.entities.Player;
import dungeonmania.main.entities.items.BombItem;
import dungeonmania.main.entities.statics.FloorSwitch;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Bomb extends Entity {


    /**
     * Bomb Constructor
     * @param dungeon
     * @param entity
     */
    public Bomb(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity, new CollectAlways());
    }

    /**
     * Bomb Constructor given a cBehaviour
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param cBehaviour
     */
    public Bomb(Dungeon dungeon, String id, String type, Position position, CollectBehaviour cBehaviour) {
        super(dungeon, id, type, position, false, cBehaviour);
    }

    /**
     * Performs the collect behaviour of Bomb
     * This method is called when player moves
     */
    @Override
    public void update() {
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            performCollect();
        }
    }

    public void performCollect() {
        BombItem bomb = new BombItem(getDungeon(), this);
        getCBehaviour().collect(getDungeon(), this, bomb);
    }


    /**
     * Checks if Bomb should detonate
     * Called in every tick regardless of player movement or selection
     */
    public void checkDetonate() {
        
        // A bomb only detonates after it has been collected 
        // After collection and dropping, a bomb cannot be collected again so it's cBehaviour will be NoCollect
        if (!getCBehaviour().getClass().getSimpleName().equals("NoCollect")) {
            return;
        }

        // Floor switch's left, right, up and down must be triggered for detonation to occur
        for (Entity entity : getAdjacentEntities()) {
            if (entity instanceof FloorSwitch && ((FloorSwitch) entity).isTriggered()) {
                detonate();
            }        
        }
    }

    /**
     * Removes all entities (other than the player) in a 1 unit radius around the bomb
     */
    public void detonate() {
        List<Entity> blastEntities = getEntitiesInBlastRadius();
        for (Entity entity : blastEntities) {
            if (!(entity instanceof Player) && !(entity.equals(this))) {
                getDungeon().removeEntity(entity);
            }
        }

        getDungeon().removeEntity(this);
    }

    /**
     * Returns a list of entities in a one unit circle around the entity AND those in the same position
     * 
     * @return HashMap with a key of the direction and the entity as the value (no
     *         adjacent entity in a certain direction will mean that the key will
     *         not exist)
     */
    public List<Entity> getEntitiesInBlastRadius() {

        List<Position> adjacentPositions = this.getPosition().getAdjacentPositions();
        adjacentPositions.add(this.getPosition());
        List<Entity> adjacent = new ArrayList<>();
        for (Entity entity: getDungeon().getEntities()) {
            if (adjacentPositions.contains(entity.getPosition())) {
                adjacent.add(entity);
            }
        }
        return adjacent;
    }
}
