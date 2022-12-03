package dungeonmania.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.main.collect_StrPattern.CollectBehaviour;
import dungeonmania.main.collect_StrPattern.NoCollect;
import dungeonmania.main.movement_StrPattern.MoveBehaviour;
import dungeonmania.main.movement_StrPattern.NoMovement;

public abstract class Entity {

    /**
     * @param entity  Entity "has-a" relationship EntityResponse class
     * @param dungeon subject for entity observer (allows for entity to be able to
     *                tell where they are on a map)
     */
    private EntityResponse entity;
    private Dungeon dungeon;
    private CollectBehaviour cBehaviour = new NoCollect();
    private MoveBehaviour mBehaviour = new NoMovement();
    private boolean passable;

    /**
     * Constructor for Entity (Overload)
     * 
     * @param dungeon
     * @param entity
     */
    public Entity(Dungeon dungeon, EntityResponse entity) {
        this.entity = entity;
        this.dungeon = dungeon;
        this.passable = true;
    }

    /**
     * Constructor for Entity (Overload)
     * @param dungeon
     * @param entity
     * @param cBehaviour
     */
    public Entity(Dungeon dungeon, EntityResponse entity, CollectBehaviour cBehaviour) {
        this.entity = entity;
        this.dungeon = dungeon;
        this.cBehaviour = cBehaviour;
        this.passable = true;
    }

    /**
     * Constructor for Entity (Overload)
     * 
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     */
    public Entity(Dungeon dungeon, String id, String type, Position position, boolean isInteractable) {
        this.entity = new EntityResponse(id, type, position, isInteractable);
        this.dungeon = dungeon;
        this.passable = true;
    }

    /**
     * Constructor for Entity (Overload) Updates the map for this entity observer
     * 
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     */
    public Entity(Dungeon dungeon, String id, String type, Position position, boolean isInteractable,
            CollectBehaviour cBehaviour) {
        this.entity = new EntityResponse(id, type, position, isInteractable);
        this.dungeon = dungeon;
        this.cBehaviour = cBehaviour;
        this.passable = true;

    }

    /**
     * Abstract method for updates everytime Player moves
     */
    public abstract void update();

    /**
     * Returns a list of entities in the same position, including this entity
     * 
     * @return
     */
    public List<Entity> getEntitiesOnPosition() {
        return dungeon.getEntitiesLocation().get(this.getPosition());
    }

    /**
     * Returns a list of adjacent entities in each direction - up, down, left and
     * right
     * 
     * @return
     */
    public List<Entity> getAdjacentEntities() {
        List<Entity> adjacent = new ArrayList<Entity>();
        for (List<Entity> entities : getAdjacentEntitiesDirection().values()) {
            adjacent.addAll(entities);
        }
        return adjacent;
    }

    /**
     * Returns a hashmap that tells us the adjacent entities in each direction
     * 
     * @return HashMap with a key of the direction and the entity as the value (no
     *         adjacent entity in a certain direction will mean that the list is
     *         empty)
     */
    public Map<Direction, List<Entity>> getAdjacentEntitiesDirection() {
        HashMap<Direction, List<Entity>> adjacent = new HashMap<Direction, List<Entity>>();
        Map<Position, List<Entity>> entities = dungeon.getEntitiesLocation();
        Position position = getPosition();
        // initialise adjacents to empty list
        adjacent.put(Direction.UP, new ArrayList<>());
        adjacent.put(Direction.DOWN, new ArrayList<>());
        adjacent.put(Direction.RIGHT, new ArrayList<>());
        adjacent.put(Direction.LEFT, new ArrayList<>());

        if (entities.containsKey(position.translateBy(Direction.UP))) {
            adjacent.put(Direction.UP, entities.get(position.translateBy(Direction.UP)));
        }
        if (entities.containsKey(position.translateBy(Direction.DOWN))) {
            adjacent.put(Direction.DOWN, entities.get(position.translateBy(Direction.DOWN)));
        }
        if (entities.containsKey(position.translateBy(Direction.RIGHT))) {
            adjacent.put(Direction.RIGHT, entities.get(position.translateBy(Direction.RIGHT)));
        }
        if (entities.containsKey(position.translateBy(Direction.LEFT))) {
            adjacent.put(Direction.LEFT, entities.get(position.translateBy(Direction.LEFT)));
        }
        return adjacent;
    }

    public boolean isEntityInDirectionPassable(Direction direction) {
        if (direction.equals(Direction.NONE)) {
            return false;
        }

        List<Entity> inDirection = getAdjacentEntitiesDirection().get(direction);
        for (Entity entity : inDirection) {
            if (!entity.isPassable()) {
                return false;
            }
        }
        return true;
    }


    // Get Methods 
    


    public Dungeon getDungeon() {
        return dungeon;
    }

    public boolean isInteractable() {
        return entity.isInteractable();
    }

    public String getId() {
        return entity.getId();
    }

    public Position getPosition() {
        return entity.getPosition();
    }


    public String getType() {
        return entity.getType();
    }
    
    public CollectBehaviour getCBehaviour() {
        return cBehaviour;
    }

    public MoveBehaviour getMBehaviour() {
        return mBehaviour;
    }

    public void setType(String type) {
        this.entity = new EntityResponse(getId(), type, getPosition(), isInteractable());
    }

    public EntityResponse getEntityResponse() {
        return entity;
    }


    // Set Methods

    public void setIsInteractable(boolean bool) {
        this.entity = new EntityResponse(getId(), getType(), getPosition(), bool);
    }

    public void setPosition(Position position) {
        this.entity = new EntityResponse(getId(), getType(), position, isInteractable());
    }

    public void setPosition(Direction direction) {
        this.entity = new EntityResponse(getId(), getType(), getPosition().translateBy(direction), isInteractable());
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public boolean isPassable() {
        return this.passable;
    }


    public void setCBehaviour(CollectBehaviour collectBehaviour) {
        this.cBehaviour = collectBehaviour;
    }

    public void setMBehaviour(MoveBehaviour moveBehaviour) {
        this.mBehaviour = moveBehaviour;
    }


    /**
     * produces a JSON object for this entity
     * @return
     */
    public JSONObject toJSON() {

        JSONObject jsonEntity = new JSONObject();
        jsonEntity.put("x", getPosition().getX());
        jsonEntity.put("y", getPosition().getY());
        // TODO: Replace with override toJSON functions in the respective entity classes
        switch (getType()) {
        case "key_gold":
            jsonEntity.put("type", "key");
            jsonEntity.put("key", 1);
            break;
        case "key_silver":
            jsonEntity.put("type", "key");
            jsonEntity.put("key", 2);
            break;
        case "portal_blue":
            jsonEntity.put("type", "portal");
            jsonEntity.put("colour", "BLUE");
            break;
        case "portal_red":
            jsonEntity.put("type", "portal");
            jsonEntity.put("colour", "RED");
            break;
        case "portal_orange":
            jsonEntity.put("type", "portal");
            jsonEntity.put("colour", "ORANGE");
            break;
        case "portal_black":
            jsonEntity.put("type", "portal");
            jsonEntity.put("colour", "BLACK");
            break;
        case "door_locked_gold":
            jsonEntity.put("type", "door");
            jsonEntity.put("key", 1);
            break;
        case "door_locked_silver":
            jsonEntity.put("type", "door");
            jsonEntity.put("key", 2);
            break;
        default:
            jsonEntity.put("type", getType());
        }
        return jsonEntity;
    }
}
