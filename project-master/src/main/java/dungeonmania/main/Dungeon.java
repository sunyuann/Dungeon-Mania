package dungeonmania.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.entities.Player;
import dungeonmania.main.entities.collectable.*;
import dungeonmania.main.entities.moving.Assassin;
import dungeonmania.main.entities.moving.Hydra;
import dungeonmania.main.entities.moving.Mercenary;
import dungeonmania.main.entities.moving.MovingEntities;
import dungeonmania.main.entities.moving.Spider;
import dungeonmania.main.entities.moving.ZombieToast;
import dungeonmania.main.entities.statics.StaticEntities;
import dungeonmania.main.entities.statics.SwampTile;
import dungeonmania.main.entities.statics.ZombieToastSpawner;
import dungeonmania.main.entities.statics.Door.Door;
import dungeonmania.main.entities.statics.Boulder;
import dungeonmania.main.entities.statics.Exit;
import dungeonmania.main.entities.statics.FloorSwitch;
import dungeonmania.main.entities.statics.Portal;
import dungeonmania.main.entities.statics.Wall;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Dungeon {

    /**
     * Dungeon is a class that represents a Dungeon map
     * 
     * @param dungeon         Dungeon "has-a" relationship DungeonResponse class
     * @param dimensions      dimensions of dungeon
     * @param dungeonEntities list of entities (also acts as observers)
     * @param movingEntities
     */
    private DungeonResponse dungeon;
    private Position dimensions;
    private Player player;
    private Inventory dungeonInventory;
    private Builder builder;
    private Boolean isBattle = true;
    private List<Entity> dungeonEntities = new ArrayList<Entity>();
    private List<MovingEntities> movingEntities = new ArrayList<MovingEntities>();
    private List<StaticEntities> staticEntities = new ArrayList<StaticEntities>();
    private List<BattleEntities> battleEntities = new ArrayList<BattleEntities>();

    /**
     * Constructor for Dungeon (Overload)
     * 
     * @param dungeon DungeonResponse
     */
    public Dungeon(DungeonResponse dungeon) {
        this.dungeon = dungeon;
        for (EntityResponse entity : dungeon.getEntities()) {
            createEntity(entity);
        }
        this.dungeonInventory = new Inventory(this, dungeon.getInventory());
        this.builder = new Builder(this, dungeonInventory);
    }

    /**
     * Constructor for Dungeon (Overload)
     * 
     * @param dungeonId
     * @param dungeonName
     * @param entities
     * @param inventory
     * @param buildables
     * @param goals
     * @param animations
     */
    public Dungeon(String dungeonId, String dungeonName, List<EntityResponse> entities, List<ItemResponse> inventory,
            List<String> buildables, String goals, List<AnimationQueue> animations) {
        dungeon = new DungeonResponse(dungeonId, dungeonName, entities, inventory, buildables, goals, animations);
        for (EntityResponse entity : dungeon.getEntities()) {
            createEntity(entity);
        }
        this.dungeonInventory = new Inventory(this, dungeon.getInventory());
        this.builder = new Builder(this, dungeonInventory);
    }

    /**
     * TODO: change name to update entities & items
     */
    public void updateEntities() {
        int size = dungeonEntities.size();
        for (int i = 0; i < size; i++) {
            updateDungeonDimensions();
            dungeonEntities.get(i).update();
            // if an entity was removed, readjust index accordingly
            if (dungeonEntities.size() < size) {
                size = dungeonEntities.size();
                i--;
            }
        }
    }

    public void tickUpdates() {
        // do the bomb stuff here
        for (int i = 0; i < dungeonEntities.size(); i++) {
            Entity entity = dungeonEntities.get(i);
            if (entity instanceof Bomb) {
                ((Bomb) entity).checkDetonate();
            }
        }

        dungeonInventory.updateItems();
        player.updateInvincibileTicks();
        player.updateInvisibleTicks();
        player.observerTickUpdates();
    }

    /**
     * A method to create a new entity class using switch statements
     * 
     * @param entity
     * @return
     */
    public Entity createEntity(EntityResponse entity) {
        switch (entity.getType()) {
        case "player":
            this.player = (new Player(this, entity));
            dungeonEntities.add(player);
            battleEntities.add(player);
            return player;
        case "mercenary":
            Mercenary m = new Mercenary(this, entity);
            dungeonEntities.add(m);
            movingEntities.add(m);
            battleEntities.add(m);
            return m;
        case "zombie":
            ZombieToast z = new ZombieToast(this, entity);
            dungeonEntities.add(z);
            movingEntities.add(z);
            battleEntities.add(z);
            return z;
        case "spider":
            Spider s = new Spider(this, entity);
            dungeonEntities.add(s);
            movingEntities.add(s);
            battleEntities.add(s);
            return s;
        case "hydra":
            Hydra h = new Hydra(this, entity);
            dungeonEntities.add(h);
            movingEntities.add(h);
            battleEntities.add(h);
            return h;
        case "assassin":
            Assassin ass = new Assassin(this, entity);
            dungeonEntities.add(ass);
            movingEntities.add(ass);
            battleEntities.add(ass);
            return ass;
        case "mercenary_ally":
            Mercenary mAlly = new Mercenary(this, entity);
            movingEntities.add(mAlly);
            dungeonEntities.add(mAlly);
            battleEntities.add(mAlly);
            return mAlly;
        case "assassin_ally":
            Assassin aAlly = new Assassin(this, entity);
            movingEntities.add(aAlly);
            dungeonEntities.add(aAlly);
            battleEntities.add(aAlly);
            return aAlly;
        case "armour":
            Armour a = new Armour(this, entity);
            dungeonEntities.add(a);
            return a;
        case "arrow":
            Arrow arrow = new Arrow(this, entity);
            dungeonEntities.add(arrow);
            return arrow;
        case "bomb":
            Bomb bomb = new Bomb(this, entity);
            dungeonEntities.add(bomb);
            return bomb;
        case "health_potion":
            HealthPotion hP = new HealthPotion(this, entity);
            dungeonEntities.add(hP);
            return hP;
        case "invincibility_potion":
            InvincibilityPotion invincP = new InvincibilityPotion(this, entity);
            dungeonEntities.add(invincP);
            return invincP;
        case "invisibility_potion":
            InvisibilityPotion invisP = new InvisibilityPotion(this, entity);
            dungeonEntities.add(invisP);
            return invisP;
        case "key_gold":
            Key k_g = new Key(this, entity);
            dungeonEntities.add(k_g);
            return k_g;
        case "key_silver":
            Key k_s = new Key(this, entity);
            dungeonEntities.add(k_s);
            return k_s;
        case "sword":
            Sword sword = new Sword(this, entity);
            dungeonEntities.add(sword);
            return sword;
        case "treasure":
            Treasure t = new Treasure(this, entity);
            dungeonEntities.add(t);
            return t;
        case "wood":
            Wood wo = new Wood(this, entity);
            dungeonEntities.add(wo);
            return wo;
        case "wall":
            Wall wa = new Wall(this, entity);
            dungeonEntities.add(wa);
            staticEntities.add(wa);
            return wa;
        case "switch":
            FloorSwitch fs = new FloorSwitch(this, entity);
            dungeonEntities.add(fs);
            staticEntities.add(fs);
            return fs;
        case "boulder":
            Boulder b = new Boulder(this, entity);
            dungeonEntities.add(b);
            staticEntities.add(b);
            return b;
        case "door_unlocked":
            Door du = new Door(this, entity);
            dungeonEntities.add(du);
            staticEntities.add(du);
            return du;
        case "door_locked_gold":
            Door dg = new Door(this, entity);
            dungeonEntities.add(dg);
            staticEntities.add(dg);
            return dg;
        case "door_locked_silver":
            Door ds = new Door(this, entity);
            dungeonEntities.add(ds);
            staticEntities.add(ds);
            return ds;
        case "exit":
            Exit e = new Exit(this, entity);
            dungeonEntities.add(e);
            staticEntities.add(e);
            return e;
        case "portal_blue":
            Portal p_be = new Portal(this, entity);
            dungeonEntities.add(p_be);
            staticEntities.add(p_be);
            return p_be;
        case "portal_red":
            Portal p_r = new Portal(this, entity);
            dungeonEntities.add(p_r);
            staticEntities.add(p_r);
            return p_r;
        case "portal_orange":
            Portal p_o = new Portal(this, entity);
            dungeonEntities.add(p_o);
            staticEntities.add(p_o);
            return p_o;
        case "portal_black":
            Portal p_bk = new Portal(this, entity);
            dungeonEntities.add(p_bk);
            staticEntities.add(p_bk);
            return p_bk;
        case "zombie_toast_spawner":
            ZombieToastSpawner zts = new ZombieToastSpawner(this, entity);
            dungeonEntities.add(zts);
            staticEntities.add(zts);
            return zts;
        case "one_ring":
            OneRing o = new OneRing(this, entity);
            dungeonEntities.add(o);
            return o;
        case "sun_stone":
            SunStone ss = new SunStone(this, entity);
            dungeonEntities.add(ss);
            return ss;
        case "anduril":
            Anduril anduril = new Anduril(this, entity);
            dungeonEntities.add(anduril);
            return anduril;
        case "swamp_tile":
            SwampTile st = new SwampTile(this, entity);
            dungeonEntities.add(st);
            staticEntities.add(st);
            return st;
        }
        return null;
    }

    /**
     * add an entity to the dungeonEntities list
     */
    public void addDungeonEntity(Entity entity) {
        dungeonEntities.add(entity);

    }

    /**
     * Removes an Entity from the list and movable entities from the given list
     */
    public void removeEntity(Entity entity) {
        dungeonEntities.remove(entity);
        for (int i = 0; i < movingEntities.size(); i++) {
            MovingEntities moving = movingEntities.get(i);
            if (moving.getId().equals(entity.getId())) {
                movingEntities.remove(moving);
            }
        }
        for (int j = 0; j < movingEntities.size(); j++) {
            BattleEntities battle = battleEntities.get(j);
            if (battle.getId().equals(entity.getId())) {
                battleEntities.remove(battle);
            }
        }

        if (entity instanceof StaticEntities) {
            staticEntities.remove(((StaticEntities) entity));
        }
    }

    /**
     * Helper function that returns a list of entities in the form of
     * EntityResponses
     * 
     * @param dungeon DungeonResponse
     * @return
     * @throws IOException
     */
    public List<EntityResponse> getEntitiesResponse() {
        List<EntityResponse> entities = new ArrayList<EntityResponse>();
        for (Entity entity : dungeonEntities) {
            entities.add(entity.getEntityResponse());
        }
        return entities;
    }

    /**
     * Getter for DungeonResponse
     * 
     * @return
     */
    public DungeonResponse getDungeonResponse() {
        return new DungeonResponse(dungeon.getDungeonId(), dungeon.getDungeonName(), getEntitiesResponse(),
                dungeonInventory.getInventoryResponse(), builder.getBuildable(), dungeon.getGoals(),
                dungeon.getAnimations());
    }

    /**
     * Setter for DungeonResponse
     * 
     * @param dungeon
     */
    public void setDungeonResponse(DungeonResponse dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Setter for dungeon id
     * 
     * @return
     */
    public void setDungeonId(String dungeonId) {
        this.dungeon = new DungeonResponse(dungeonId, dungeon.getDungeonName(), dungeon.getEntities(),
                dungeon.getInventory(), dungeon.getBuildables(), dungeon.getGoals(), dungeon.getAnimations());
    }

    /**
     * Setter for goals
     * 
     * @return
     */
    public void setDungeonGoals(String goals) {
        this.dungeon = new DungeonResponse(dungeon.getDungeonName(), dungeon.getDungeonName(), dungeon.getEntities(),
                dungeon.getInventory(), dungeon.getBuildables(), goals, dungeon.getAnimations());
    }

    /**
     * Setter for animations
     * 
     * @param animations
     */
    public void setAnimations(List<AnimationQueue> animations) {
        this.dungeon = new DungeonResponse(dungeon.getDungeonName(), dungeon.getDungeonName(), dungeon.getEntities(),
                dungeon.getInventory(), dungeon.getBuildables(), dungeon.getGoals(), animations);
    }

    public List<AnimationQueue> getAnimations() {
        return dungeon.getAnimations();
    }

    /**
     * Getter for dungeon name
     * 
     * @return
     */
    public String getDungeonName() {
        return dungeon.getDungeonName();
    }

    /**
     * Getter for id
     * 
     * @return
     */
    public String getDungeonId() {
        return dungeon.getDungeonId();
    }

    /**
     * Getter for goals
     * 
     * @return
     */
    public String getGoals() {
        return dungeon.getGoals();
    }

    /**
     * Getter for player
     * 
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for dungeonEntities
     * 
     * @return
     */
    public List<Entity> getEntities() {
        return dungeonEntities;
    }

    /**
     * Getter for movingEntities
     * 
     * @return
     */
    public List<MovingEntities> getMovingEntities() {
        return movingEntities;
    }

    /**
     * Getter for battleEntities
     * 
     * @return
     */
    public List<BattleEntities> getBattleEntities() {
        return battleEntities;
    }

    /**
     * Getter for staticEntities
     * 
     * @return
     */
    public List<StaticEntities> getStaticEntities() {
        return staticEntities;
    }

    /**
     * get the entity with id entityId - entityIds are unique
     * 
     * @param entityId
     * @return
     */
    public Entity getEntity(String entityId) {
        for (Entity entity : dungeonEntities) {
            if (entity.getId().equals(entityId)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Helper function that returns a hashMap that contains a list of entities
     * depending on position as a key
     * 
     * @param dungeon DungeonResponse
     * @return
     * @throws IOException
     */
    public Map<Position, List<Entity>> getEntitiesLocation() {
        Map<Position, List<Entity>> entityMap = new HashMap<Position, List<Entity>>();
        // adds all the entities
        for (Entity entity : getEntities()) {
            Position entity_pos = entity.getPosition().asLayer(0);
            if (entityMap.containsKey(entity_pos)) {
                List<Entity> entities = entityMap.get(entity_pos);
                entities.add(entity);
            } else {
                List<Entity> entities = new ArrayList<Entity>();
                entities.add(entity);
                entityMap.put(entity_pos, entities);
            }
        }
        // fills in the spaces where there isnt an entity
        // this creates a complete grid
        Position top_left = getDungeonTopLeftCorner();
        for (int i = top_left.getX(); i < dimensions.getX(); i++) {
            for (int j = top_left.getY(); j < dimensions.getY(); j++) {
                Position pos = new Position(i, j);
                if (!entityMap.containsKey(pos)) {
                    entityMap.put(pos, new ArrayList<>());
                }
            }
        }
        return entityMap;
    }

    /**
     * Helper function that returns height and width of dungeon as Position vector
     * 
     * @return
     */
    public Position getDungeonDimensions() {
        return dimensions;
    }

    /**
     * Setter for dungeon dimensions
     * 
     * @param width
     * @param height
     */
    public void setDungeonDimensions(int width, int height) {
        this.dimensions = new Position(width, height);
    }

    /**
     * Helper function that returns the position of the top left corner of the
     * dungeon
     */
    public Position getDungeonTopLeftCorner() {
        Position top_left = new Position(0, 0);
        for (Entity entity : getEntities()) { 
            if (entity.getPosition().getX() < top_left.getX()) {
                top_left = new Position(entity.getPosition().getX(), top_left.getY());
            } 
            if (entity.getPosition().getY() < top_left.getY()) {
                top_left = new Position(top_left.getX(), entity.getPosition().getY());
            }
        }
        return top_left;
    }

    /**
     * Helper function that returns the position of the bottom right corner of the
     * dungeon
     */
    public Position getDungeonBottomRightCorner() {
        Position bottom_right = getDungeonTopLeftCorner().translateBy(new Position(1, 1));
        for (Entity entity : getEntities()) { 
            if (entity.getPosition().getX() > bottom_right.getX()) {
                bottom_right = new Position(entity.getPosition().getX(), bottom_right.getY());
            } 
            if (entity.getPosition().getY() > bottom_right.getY()) {
                bottom_right = new Position(bottom_right.getX(), entity.getPosition().getY());
            }
        }
        return bottom_right;
    }

    /**
     * Updates the dungeon dimensions based on new top left and bottom right corners
     */
    public void updateDungeonDimensions() {
        this.dimensions = Position.calculatePositionBetween(getDungeonTopLeftCorner(), getDungeonBottomRightCorner()).translateBy(new Position(1,1));
    }

    /**
     * Helper function that returns randomly selected position within dungeon bounds
     * 
     * @return
     */
    public Position generateRandomSpawn() {
        // assumes entities cannot spawn in the borders of the map (must be within)
        Position top_left = getDungeonTopLeftCorner().translateBy(new Position(1, 1));
        Position dungeon_dimensions = getDungeonDimensions();
        // randomly generate height and width
        Random randomHeight = new Random();
        try {
            int randHeight = randomHeight.nextInt(dungeon_dimensions.getY() - 2);
            Random randomWidth = new Random();
            int randWidth = randomWidth.nextInt(dungeon_dimensions.getX() - 2);
            return top_left.translateBy(new Position(randWidth, randHeight));
        } catch (Exception e) {
            
        }
        return new Position(0, 0);
    }

    /**
     * Returns number of entities of given type
     * 
     * @param type
     * @return
     */
    public int getNumEntities(String type) {
        int count = 0;
        for (Entity entity : getEntities()) {
            if (entity.getType().equals(type)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns entities of given type in a list
     * 
     * @param type
     * @return
     */
    public List<Entity> getEntitiesOfType(String type) {
        List<Entity> entitiesOfType = new ArrayList<Entity>();
        for (Entity entity : getEntities()) {
            if (entity.getType().equals(type)) {
                entitiesOfType.add(entity);
            }
        }
        return entitiesOfType;
    }

    /**
     * Getter for inventory object
     * 
     * @return
     */
    public Inventory getDungeonInventory() {
        return dungeonInventory;
    }

    public void addToInventory(Item item) {
        dungeonInventory.addInventoryItem(item);
    }

    public void removeFromInventory(Item item) {
        dungeonInventory.removeFromInventory(item);
    }

    /**
     * uses the selected item if it is valid SelectableItem
     */
    public void useSelectedItem(String itemUsed) {
        if (itemUsed != null) {
            dungeonInventory.useSelectedItem(itemUsed);
        }
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setIsBattle(boolean isBattle) {
        this.isBattle = isBattle;
    }

    /**
     * simulates the battle
     * 
     * @param entity
     */
    public void simulateBattle(BattleEntities entity) {

        int result = player.battle(entity);

        // if current game is peaceful or if invisible, don't do anything
        if (!isBattle || result == 2) {
            return;
        }

        if (result == 0) { // battle won
            // remove battle entity
            removeEntity(entity);
        } else if (result == 1) { // battle lost
            // check for one ring - if it exists, call use one ring function
            if (dungeonInventory.checkForOneRing() != null) {
                dungeonInventory.checkForOneRing().useOneRing();
                removeEntity(entity);
            } else {
                // if one ring doesn't exist, game over
                removeEntity(player);
            }
        }
    }

    public JSONObject toJSON() {
        JSONObject map = new JSONObject();
        map.put("name", dungeon.getDungeonName());
        map.put("width", dimensions.getX());
        map.put("height", dimensions.getY());

        JSONArray entities = new JSONArray();
        for (Entity entity : dungeonEntities) {
            entities.put(entity.toJSON());
        }
        map.put("entities", entities);

        JSONArray inventory = new JSONArray();
        for (Item item : dungeonInventory.getDungeonInventory()) {
            inventory.put(item.toJSON());
        }
        map.put("inventory", inventory);

        JSONArray buildables = new JSONArray();
        for (String buildable : builder.getBuildable()) {
            buildables.put(buildable);
        }
        map.put("buildables", buildables);
        return map;
    }
}