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

public class Assassin extends BattleEntities implements MovingEntities, Bribable, ObserverPlayer {

    /**
     * Assassin attributes and default values
     */
    private static double DEFAULT_ASSASSIN_HEALTH = 150;
    private static double DEFAULT_ASSASSIN_ATTACK = 20;
    private static Integer ASSASSIN_RADIUS = 5;
    private static Integer BRIBING_RADIUS = 2;
    private Player player;
    private boolean ally = false;
    private int mindControlTicksRemaining = 0;

    /**
     * Constructor of Assassin
     * @param dungeon
     * @param entity
     */
    public Assassin(Dungeon dungeon, EntityResponse entity) {
        super(dungeon, new EntityResponse(entity.getId(), entity.getType(), entity.getPosition(), true));
        setHealth(DEFAULT_ASSASSIN_HEALTH);
        setAttack(DEFAULT_ASSASSIN_ATTACK);
        setMBehaviour(new FollowMovement());
        player = dungeon.getPlayer();

        if (entity.getType().equals("assassin_ally")) {
            player.attachAlly(this);
        }
    }

    /**
     * Constructor of Assassin
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     */
    public Assassin(Dungeon dungeon, String id, String type, Position position, Boolean isInteractable) {
        super(dungeon, id, type, position, isInteractable);
        setHealth(DEFAULT_ASSASSIN_HEALTH);
        setAttack(DEFAULT_ASSASSIN_ATTACK);
        setMBehaviour(new FollowMovement());

        if (type.equals("assassin_ally")) {
            player.attachAlly(this);
        }
    }

    /**
     * Implements abstract method of Entity
     * If the assassin is not an ally, simulates battle with player if in same position
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
    @Override
    public void move() {
        getMBehaviour().move(getDungeon(), this);
        getDungeon().updateEntities();
    }

    /**
     * Implements Bribable interface method
     * Checks if player is within the 2 tile radius of assassin to bribe
     * @return true if player is in radius, false if not
     */
    @Override
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
        Item oneRing = getDungeon().getDungeonInventory().getItemOfType("one_ring");

        if ((treasure != null || sunStone != null) && oneRing != null) {
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
    @Override
    public void bribe() {
        // remove items - if no treasure, sun_stone automatically used
        getDungeon().getDungeonInventory().removeFromInventoryOfType("treasure");
        getDungeon().getDungeonInventory().removeFromInventoryOfType("one_ring");
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
     * Pre-Condition: canMindControl() is true
     * Implements Bribrable interface method
     */
    @Override
    public void mindControl() {
        getDungeon().getDungeonInventory().removeFromInventoryOfType("sceptre");

        setMindControlTicksRemaining(10);
        player.attachAlly(this);
        
    }

    /**
     * Checks if there are sufficient items in the player's inventory to mind control an assassin
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
     * Implements ObserverPlayer interface method
     */
    public void positionUpdate(Position position) {
        setPosition(position);
    }

    /**
     * Implements ObserverPlayer interface method
     */
    @Override
    public void attachAllyUpdate() {
        setAllyStatus(true);
        setType("assassin_ally");
        setIsInteractable(false);
        setMBehaviour(new NoMovement());    
        setPosition(player.getPosition());
    }

    /**
     * Implements ObserverPlayer interface method
     */
    @Override
    public void detachAllyUpdate() {
        setAllyStatus(false);
        setType("assassin");
        setIsInteractable(true);
        setMBehaviour(new FollowMovement());
    }


    /**
     * Implements ObserverPlayer interface method
     * Updates the assassin's attribute if it is being mind controlled
     */
    @Override
    public void tickUpdates() {
        // detatchAlly is only called if mindControlTicks was > 0 in the previous tick
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

