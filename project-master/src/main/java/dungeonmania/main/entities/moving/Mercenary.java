package dungeonmania.main.entities.moving;

import org.json.JSONObject;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Item;

import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.entities.Bribable;
import dungeonmania.main.entities.Player;
import dungeonmania.main.movement_StrPattern.FollowMovement;
import dungeonmania.main.movement_StrPattern.NoMovement;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Mercenary extends BattleEntities implements MovingEntities, Bribable, ObserverPlayer {
    
    /**
     * Assassin attributes and default values
     */
    private static double DEFAULT_MERCENARY_HEALTH = 90;
    private static double DEFAULT_MERCENARY_ATTACK = 10;
    private static Integer MERCENARY_RADIUS = 5;
    private static Integer BRIBING_RADIUS = 2;
    private Player player;
    private boolean ally = false;
    private int mindControlTicksRemaining = 0;

    /**
     * Constructor of Mercenary
     * @param dungeon
     * @param entity
     */
    public Mercenary(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, new EntityResponse(entity.getId(), entity.getType(), entity.getPosition(), true));
        setHealth(DEFAULT_MERCENARY_HEALTH);
        setAttack(DEFAULT_MERCENARY_ATTACK);
        setMBehaviour(new FollowMovement());
        player = dungeon.getPlayer();

        if (entity.getType().equals("mercenary_ally")) {
            player.attachAlly(this);
        }
    }

    public Mercenary(Dungeon dungeon, String id, String type, Position position, Boolean isInteractable) {
        super(dungeon, id, type, position, isInteractable);
        setHealth(DEFAULT_MERCENARY_HEALTH);
        setAttack(DEFAULT_MERCENARY_ATTACK);
        setMBehaviour(new FollowMovement());
        if (type.equals("mercenary_ally")) {
            player.attachAlly(this);
        }
    }

    /**
     * Implements abstract method of Entity
     * If the mercenary is not an ally, simulates battle with player if in same positon
     */
    @Override
    public void update() {
        if (ally) {
            return;
        }
        if (getEntitiesOnPosition().contains(getDungeon().getPlayer())) {
            getDungeon().simulateBattle(this);
        }
    }

    /**
     * Moves the entity
     */
    public void move() {
        getMBehaviour().move(getDungeon(), this);
        getDungeon().updateEntities();
    }


    /**
     * Checks if player in radius to speed up mercenary movement
     * @return true if player is in radius, false if not
     */
    public boolean inRadius() {
        Position offset = Position.calculatePositionBetween(this.getPosition(), player.getPosition());
        if ((offset.getX() <= MERCENARY_RADIUS && offset.getX() >= -MERCENARY_RADIUS)
                && (offset.getY() <= MERCENARY_RADIUS && offset.getY() >= -MERCENARY_RADIUS)) {
            return true;
        }
        return false;
    }

    /**
     * Implements Bribable interface method
     * Checks if player is within the 2 tile radius of mercenary to bribe
     * @return true if player is in radius, false if not
     */
    public boolean inBribingRadius() {
        Position offset = Position.calculatePositionBetween(this.getPosition(), player.getPosition());
        if ((offset.getX() <= BRIBING_RADIUS && offset.getX() >= -BRIBING_RADIUS)
                && (offset.getY() <= BRIBING_RADIUS && offset.getY() >= -BRIBING_RADIUS)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if player has sufficient items to bribe
     * @return true if player has sufficient items and false if not
     */
    public boolean sufficientBribingItems() {
        Item treasure = getDungeon().getDungeonInventory().getItemOfType("treasure");
        Item sunStone = getDungeon().getDungeonInventory().getItemOfType("sun_stone");

        if (treasure != null || sunStone != null) {
            return true;
        }

        return false;

    }
    
    /**
     * Implements Bribable interface method
     */
    @Override
    public boolean canBribe() {
        if (inBribingRadius() && sufficientBribingItems()) {
            return true;
        }

        return false;
    }

    /**
     * Pre-Condition: canBribe() is true
     * Implements Bribable interface method
     */
    public void bribe() {
        // remove item - if no treasure; sun_stone automatically used
        getDungeon().getDungeonInventory().removeFromInventoryOfType("treasure");
        player.attachAlly(this);
    }

    /**
     * Implements Bribable interface method
     */
    public boolean canMindControl() {
        if (inBribingRadius() && sufficientMindControlItems()) {
            return true;
        }
        return false;
    }

    /**
     * Checks if there are sufficient items in the player's inventory to mind control a mercenary
     * @return true if there is, false if not
     */
    public boolean sufficientMindControlItems() {
        Item sceptre = getDungeon().getDungeonInventory().getItemOfType("sceptre");

        if (sceptre != null) {
            return true;
        }

        return false;
    }

    /**
     * Pre-Condition: canMindControl() is true
     * Implements Bribrable interface method
     */
    public void mindControl() {
        getDungeon().getDungeonInventory().removeFromInventoryOfType("sceptre");
        setMindControlTicksRemaining(10);
        player.attachAlly(this);
    }

    /**
     * Implements ObserverPlayer interface method
     */
    public void positionUpdate(Position position) {
        setPosition(position);
    }

    /**
     * Implements ObserverPlayer interface method
     */
    public void attachAllyUpdate() {
        setAllyStatus(true);
        setType("mercenary_ally");
        setIsInteractable(false);
        setMBehaviour(new NoMovement());
        setPosition(player.getPosition());
    }

    /**
     * Implements ObserverPlayer interface method
     */
    public void detachAllyUpdate() { 
        setAllyStatus(false);
        setType("mercenary");
        setIsInteractable(true);
        setMBehaviour(new FollowMovement());
    }

    /**
     * Implements ObserverPlayer interface method
     * Updates the mercenary's's attribute if it is being mind controlled
     */
    public void tickUpdates() {
        // Detach is only called if mindControlTicks was > 0 in the previous tick
        if (mindControlTicksRemaining > 0) {
            mindControlTicksRemaining--;
            if (mindControlTicksRemaining == 0) {
                getDungeon().getPlayer().detachAlly(this);
            }
        }
    }

    // Set Methods
    public void setAllyStatus(boolean bool) {
        this.ally = bool;
    }

    public void setMindControlTicksRemaining(int numTicks) {
        this.mindControlTicksRemaining = numTicks;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        return json.put("mind_control_ticks", mindControlTicksRemaining);
    }
}
