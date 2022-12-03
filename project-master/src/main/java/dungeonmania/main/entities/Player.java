package dungeonmania.main.entities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.moving.ObserverPlayer;
import dungeonmania.main.entities.moving.SubjectPlayer;
import dungeonmania.main.movement_StrPattern.SlowMovement;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends BattleEntities implements SubjectPlayer {

    /**
     * Attributes and default values of player
     */
    private static double DEFAULT_PLAYER_HEALTH = 100;
    private static double DEFAULT_PLAYER_ATTACK = 3;
    private int invincibleTicks = 0;
    private int invisibleTicks = 0;
    private Direction curDirection = Direction.NONE;
    private List<ObserverPlayer> allies = new ArrayList<>();

    /**
     * Constructor for Player
     */
    public Player(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, entity);
        setHealth(DEFAULT_PLAYER_HEALTH);
        setAttack(DEFAULT_PLAYER_ATTACK);
    }

    /**
     * Implements Abstract method of Entity
     */
    @Override
    public void update() {}

    /**
     * Sets the position of the player in the given direction
     * Updates ally observers and other entities of the player's movement
     */
    @Override
    public void setPosition(Direction direction) {
        if (getMBehaviour() instanceof SlowMovement) {
            getMBehaviour().move(getDungeon(), this);
            // if delay still has not ended, then we ignore user movement controls
            if (getMBehaviour() instanceof SlowMovement) {
                return;
            }
        }
        super.setPosition(direction);
        setCurDirection(direction);

        notifyObserversOfMovement();
        getDungeon().updateEntities();
    }

    /**
     * Implements SubjectPlayer interface method
     */
    public void notifyObserversOfMovement() {
        // TODO: check what happens when a player tries to move into a wall
        Position newPosition = getPosition().translateBy(Direction.reverse(getCurDirection()));

        for (ObserverPlayer ally: allies) {
            Position previous = ally.getPosition();
            ally.positionUpdate(newPosition);
            newPosition = previous;
        }
    }
    /**
     * Implements SubjectPlayer interface method
     */
    public void observerTickUpdates() {
        int size = allies.size();
        for (int i = 0; i < size; i++) {
            ObserverPlayer ally = allies.get(i);
            ally.tickUpdates();
            // maximum one ally can be removed at a time
            if (allies.size() < size) {
                size = allies.size();
                i--;
            }
        }
    }


    public int getInvincibleTicks() {
        return invincibleTicks;
    }
    public void addToInvincibleTicks(int numTicks) {
        invincibleTicks += numTicks;
    }

    public int getInvisibleTicks() {
        return invisibleTicks;
    }

    public void addToInvisibleTicks(int numTicks) {
        invisibleTicks += numTicks;
    }

    public List<ObserverPlayer> getAllies() {
        return allies;
    }

    @Override
    public void attachAlly(ObserverPlayer ally) {
        ally.attachAllyUpdate();
        allies.add(ally);
    }

    @Override
    public void detachAlly(ObserverPlayer ally) {
        ally.detachAllyUpdate();
        allies.remove(ally);
    }

    public Direction getCurDirection() {
        return curDirection;
    }
    
    public void setCurDirection(Direction direction) {
        this.curDirection = direction;
    }
    
    /**
     * battle function, simulates battle with battleEntity
     * returns 0 if battlewon, 1 if battlelost, 2 if battle skipped
     * @param battleEntity
     * @return
     */
    public int battle(BattleEntities battleEntity) {
        if (invincibleTicks > 0 ) {
            battleEntity.setHealth(0);
        } else if (invisibleTicks > 0) {
            return 2;
        }
        if (allies.contains(battleEntity)) {
            return 2;
        }   

        int battle_result = battleEntity.battle(this);
        if (battle_result == 0) {
            // if entity wins battle, returns player lost battle
            battle_result = 1;
        } else if (battle_result == 1) {
            // if entity loses battle, returns player won battle
            battle_result = 0;
        }
        // if battle evaded, returns battle_result
        return battle_result;
    }

    /**
     * for every tick - update invincibleTicks if greater than 0
     */
    public void updateInvincibileTicks() {
        if (invincibleTicks > 0) {
            invincibleTicks--;
        }
    }

    /**
     * for every tick - update invisibleTicks if greater than 0
     */
    public void updateInvisibleTicks() {
        if (invisibleTicks > 0) {
            invisibleTicks--;
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("invisible_ticks", invisibleTicks);
        json.put("invincible_ticks", invincibleTicks);
        json.put("health", getHealth());
        return json;
    }
}
