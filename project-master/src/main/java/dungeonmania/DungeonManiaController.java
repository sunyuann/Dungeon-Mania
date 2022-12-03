package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.main.Builder;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.Game;
import dungeonmania.main.Goals.*;
import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.entities.Bribable;
import dungeonmania.main.entities.Player;
import dungeonmania.main.entities.collectable.Treasure;
import dungeonmania.main.entities.statics.Boulder;
import dungeonmania.main.entities.statics.ZombieToastSpawner;
import dungeonmania.main.entities.statics.Door.Door;
import dungeonmania.main.entities.moving.Mercenary;
import dungeonmania.main.entities.moving.MovingEntities;
import dungeonmania.main.entities.statics.StaticEntities;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.MapLoader;
import dungeonmania.util.Position;
import java.util.Collections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class DungeonManiaController {

    /**
     * @param currentGame: used to represent the game that is currently being played
     *                     by the user
     */
    private Game currentGame = null;
    private Goal currentGoals = null;
    List<AnimationQueue> animations = new ArrayList<AnimationQueue>();

    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Method that creates a new game when given a preset dungeon map, and a chosen
     * gamemode.
     * 
     * @param dungeonName name of the preset dungeon map
     * @param gameMode    represents difficulty
     * @return a DungeonResponse that stores the new dungeon's info
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {

        // made the given string lowercase so "Standard" and "standard" and "STANDARD"
        // will all work (as mentioned in Ed forum post)
        gameMode = gameMode.toLowerCase();

        // catching illegal gamemode argument
        if (!getGameModes().contains(gameMode)) {
            throw new IllegalArgumentException(gameMode);
        }
        // catching illegal dungeon name argument
        JSONObject map;
        try {
            map = MapLoader.loadMapFile(dungeonName);
        } catch (Exception e) {
            throw new IllegalArgumentException(dungeonName);
        }

        String id = dungeonName + "-" + new Date().getTime();
        String goal = "";
        if (map.has("goal-condition")) {
            goal = MapLoader.JSONgetGoals(map.getJSONObject("goal-condition"));
        }

        Dungeon dungeon = new Dungeon(id, dungeonName, MapLoader.JSONgetEntityResponse(map), new ArrayList<>(),
                new ArrayList<>(), goal, new ArrayList<>());
        
        dungeon.updateDungeonDimensions();
        updateHealthAnimation(dungeon);
        constantAnimations(dungeon);
        dungeon.setAnimations(animations);

        // changes current game to this game
        this.currentGame = new Game(dungeon, gameMode);
        this.currentGoals = createGoalComposite(dungeon);
        checkGoalStatus(dungeon);
        dungeon.setIsBattle(currentGame.isBattle());
        return dungeon.getDungeonResponse();
    }

    /**
     * Method that saves the current game into a file
     * 
     * @param name represent the new id of the saved dungeon
     * @return DungeonResponse representing the saved dungeon
     * @throws IllegalArgumentException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {

        if (currentGame == null) {
            throw new IllegalArgumentException(name);
        }

        // get the dungeon that is currently being played
        Dungeon dungeon = currentGame.getDungeon();
        JSONObject JSONdungeon = currentGame.toJSON();

        try {
            FileLoader.createResourceFile("saves", name + ".json", JSONdungeon.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        // return the exact same dungeon that is currently being played, except the "id"
        // is changed
        dungeon.setDungeonId(name);
        DungeonResponse save = dungeon.getDungeonResponse();

        // updates the current game to this game
        currentGame.setDungeon(save);
        return save;
    }

    /**
     * Method that loads an existing savefile
     * 
     * @param name represents the name of the file
     * @return DungeonResponse representing the loaded dungeon
     * @throws IllegalArgumentException
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {

        JSONObject map;

        // catching illegal filename
        try {
            map = new JSONObject(FileLoader.loadNonResourceFile("saves/" + name + ".json"));
        } catch (Exception e) {
            throw new IllegalArgumentException(name);
        }

        String goal = "";
        if (map.has("goal-condition")) {
            goal = MapLoader.JSONgetGoals(map.getJSONObject("goal-condition"));
        }

        Dungeon load = new Dungeon(name, map.getString("name"), MapLoader.JSONgetEntityResponse(map),
                MapLoader.JSONgetInventory(map), MapLoader.JSONgetBuildables(map), goal, new ArrayList<>());

        load.setDungeonDimensions(map.getInt("width"), map.getInt("height"));
        MapLoader.updateInternalValues(load, map);

        // changes the game that is currently being played to the loaded game
        this.currentGame = new Game(load, map.getString("gameMode"), map.getInt("tick_count"),
                new Position(map.getInt("spawn_x"), map.getInt("spawn_y")));
        load.setIsBattle(currentGame.isBattle());
        return load.getDungeonResponse();
    }

    /**
     * Method that returns a list of all saved gamefiles
     * 
     * @return list of names for all the saved gamefiles
     */
    public List<String> allGames() {
        try {
            return FileLoader.listFileNamesInDirectoryOutsideOfResources("saves");
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {

        Dungeon currDungeon = currentGame.getDungeon();

        // store the number of moving entities before battle
        Integer numMoveEntities = currDungeon.getMovingEntities().size();

        // tick current game
        currentGame.tick();

        // raise exceptions for invalid itemUsedID
        currDungeon.useSelectedItem(itemUsed);

        // get the player
        Player player = currDungeon.getPlayer();

        // move the player
        movePlayer(currDungeon, player, movementDirection);

        // move all entities and do any battle that might occur
        moveEntities(currDungeon);

        // mercenary speed up
        speedUpMercenary(currDungeon, numMoveEntities);

        spawnEntities(currDungeon);

        updateHealthAnimation(currDungeon);
        currDungeon.tickUpdates();

        // check goal status
        checkGoalStatus(currDungeon);

        currentGame.setDungeon(currDungeon);
        return currDungeon.getDungeonResponse();
    }

    /**
     * Either destroys a zombie_toast_spawner or bribes a mercenary
     * 
     * @param entityId - the id of the entity being interacted with
     * @return DungeonResponse
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {

        Dungeon currDungeon = currentGame.getDungeon();
        Player player = currDungeon.getPlayer();

        Entity interactingEntity = currDungeon.getEntity(entityId);

        // throw exception if entityId either does not match an entity or if the
        // relevant entity is not interactable
        if (interactingEntity == null || !interactingEntity.isInteractable()) {
            throw new IllegalArgumentException(entityId);
        }

        if (interactingEntity.getType().equals("zombie_toast_spawner")) {
            // throw exception if given zombie toast spawner is not adjacent OR player does
            // not have a weapon (destroySpawner returns 1 if no weapon)
            if (!interactingEntity.getAdjacentEntities().contains(player)
                    || currDungeon.getDungeonInventory().destroySpawner() == 1) {
                throw new InvalidActionException(entityId);
            }
            // destroySpawner automatically reduces the durability of the used weapon
            currDungeon.removeEntity(interactingEntity);
        }

        // attempt mind control first and then attempt to bribe
        if (interactingEntity instanceof Bribable) {
            Bribable bribableInteracting = ((Bribable) interactingEntity);
            if (bribableInteracting.canMindControl()) {
                bribableInteracting.mindControl();
            } else if (!bribableInteracting.canBribe()) {
                throw new InvalidActionException(entityId);
            } else {
                bribableInteracting.bribe();
            }
        }
        currentGame.setDungeon(currDungeon);
        return currDungeon.getDungeonResponse();

    }

    /**
     * Build the given buildable if it is part of the valid buildable list
     * 
     * @param buildable
     * @return DungeonResponse
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {

        Dungeon currDungeon = currentGame.getDungeon();
        Builder builder = currDungeon.getBuilder();

        List<String> validBuildable = Arrays.asList("bow", "shield", "sceptre", "midnight_armour");

        if (!validBuildable.contains(buildable)) {
            throw new IllegalArgumentException(buildable);
        }

        if (!builder.getBuildable().contains(buildable)) {
            throw new InvalidActionException(buildable);
        }

        builder.makeBuildable(buildable);

        currentGame.setDungeon(currDungeon);
        return currDungeon.getDungeonResponse();

    }

    // HELPER FUNCTIONS FOR TICK//

    /**
     * Helper function: given dungeon, player and direction to move, the player is
     * moved
     * 
     * @param currDungeon
     * @param player
     * @param movementDirection
     */
    private void movePlayer(Dungeon currDungeon, Player player, Direction movementDirection) {
        List<Entity> entitiesInMoveDirection = player.getAdjacentEntitiesDirection().get(movementDirection);

        if (movementDirection == Direction.NONE) {
            entitiesInMoveDirection = new ArrayList<>();
        }

        // check if there is a door in the movementDirectionn
        Door door = null;
        for (Entity entity : entitiesInMoveDirection) {
            if (entity instanceof Door) {
                door = (Door) entity;
            }
        }

        // if there is a door, try and open it 
        if (door != null) {
            door.open();
        }

        Boulder boulder = null;
        for (Entity entity : entitiesInMoveDirection) {
            if (entity instanceof Boulder) {
                boulder = (Boulder) entity;
            }
        }

        // setPosition is not called which means the relevant entities will not update
        // If it is passable, pass into it, else be blocked by it.
        if (player.isEntityInDirectionPassable(movementDirection)) {
            // Pass through it
            player.setPosition(movementDirection);
        } else if (boulder != null) {
            // If the non passable object is a boulder, and pushable in movementDirection,
            // then move both player and boulder in that direction at the same time.
            if (boulder.pushable(movementDirection)) {
                boulder.setPosition(movementDirection);
                player.setPosition(movementDirection);
            }
            // Blocked by it (Don't Move)
        }
    }

    /**
     * Helper function: moves entities in battle
     * 
     * @param currDungeon
     * @param player
     */
    private void moveEntities(Dungeon currDungeon) {
        // move all entities
        for (int k = 0; k < currDungeon.getMovingEntities().size(); k++) {
            MovingEntities moving_entity = currDungeon.getMovingEntities().get(k);
            moving_entity.move();
        }
    }

    /**
     * Helper function: mercenaries speeds up
     * 
     * @param currDungeon
     * @param numMoveEntities
     */
    private void speedUpMercenary(Dungeon currDungeon, Integer numMoveEntities) {
        int size = currDungeon.getMovingEntities().size();
        for (int i = 0; i < size; i++) {
            MovingEntities entity = currDungeon.getMovingEntities().get(i);
            if (entity instanceof Mercenary) {
                Mercenary merc = (Mercenary) entity;
                // checks if a moving entity has been defeated in the mercenary's radius
                if (merc.inRadius() && currDungeon.getMovingEntities().size() < numMoveEntities) {
                    // mercenary moves again
                    merc.move();
                }
            }
            // if an entity was removed, readjust index accordingly
            if (currDungeon.getMovingEntities().size() < size) {
                size = currDungeon.getMovingEntities().size();
                i--;
            }
        }
    }

    /**
     * Helper function: spawns entities
     * 
     * @param currDungeon
     */
    private void spawnEntities(Dungeon currDungeon) {
        if (currentGame.isSpiderSpawn()) {
            // every 10 ticks, spawn spider at random location within dungeon range
            Position spawn_location = currDungeon.generateRandomSpawn();
            currDungeon.createEntity(
                    new EntityResponse("spider-" + new Date().getTime(), "spider", spawn_location, false));
        }
        if (currentGame.isZombieSpawn()) {
            for (StaticEntities entity : currDungeon.getStaticEntities()) {
                if (entity instanceof ZombieToastSpawner) {
                    ZombieToastSpawner zts = (ZombieToastSpawner) entity;
                    zts.spawnZombie();
                }
            }
        }
        if (currentGame.isHydraSpawn()) {
            // every 50 ticks, spawn hydra at random location within dungeon range if
            // gamemode is hard
            Position spawn_location = currDungeon.generateRandomSpawn();
            for (int i = 0; i < 50; i++) {
                if (currDungeon.getEntitiesLocation().containsKey(spawn_location)) {
                    // checks if curr location has entity on it. if so, create hydra entity and
                    // break loop
                    currDungeon.createEntity(
                            new EntityResponse("hydra-" + new Date().getTime(), "hydra", spawn_location, false));
                    break;
                }
                // if spawn_location is not empty, generate new spawn location
                spawn_location = currDungeon.generateRandomSpawn();
            }
        }

        if (currentGame.isBribableEntitySpawn()) {
            // checks if bribable entity spawn
            Position spawn_location = currentGame.getSpawnPosition();
            if (currentGame.isAssassinSpawn()) { // spawns assassin 30% of the time at spawn location
                currDungeon.createEntity(
                        new EntityResponse("assassin-" + new Date().getTime(), "assassin", spawn_location, true));
            } else { // if not, spawns mercenary (70% of time) at spawn location
                currDungeon.createEntity(
                        new EntityResponse("mercenary-" + new Date().getTime(), "mercenary", spawn_location, true));
            }
        }
    }

    /**
     * Helper Function: Returns specific goal object
     * 
     * @param dungeon
     * @param goalStrList
     * @param i
     * @return
     */
    private Goal returnGoalStrat(Dungeon dungeon, List<String> goalStrList, int i) {
        if (goalStrList.get(i).equals(":boulder")) {
            return new BoulderGoal(dungeon);
        }
        if (goalStrList.get(i).equals(":treasure")) {
            return new TreasureGoal(dungeon);
        }
        if (goalStrList.get(i).equals(":exit")) {
            return new ExitGoal(dungeon);
        }
        if (goalStrList.get(i).equals(":enemies")) {
            return new EnemiesGoal(dungeon);
        }
        return null;
    }

    /**
     * Helper function: returns the goal composite
     * 
     * @param dungeon
     * @return
     */
    private Goal createGoalComposite(Dungeon dungeon) {
        List<String> goalStringList = Arrays.asList(dungeon.getGoals().split(" "));
        Collections.reverse(goalStringList);
        Goal head = null;
        Goal comp = null;
        if (goalStringList.size() > 1) {
            // means theres an AND/OR
            if (goalStringList.get(1).equals("AND")) {
                head = new AndGoal();
            } else if (goalStringList.get(1).equals("OR")) {
                head = new OrGoal();
            }
            for (int i = 0; i < goalStringList.size(); i++) {
                if ((i & 1) == 0) {
                    // even
                    head.add(returnGoalStrat(dungeon, goalStringList, i));
                } else {
                    if (i > 2) {
                        comp = head;
                        if (goalStringList.get(i).equals("AND")) {
                            head = new AndGoal();
                            head.add(comp);
                        } else if (goalStringList.get(i).equals("OR")) {
                            head = new OrGoal();
                            head.add(comp);
                        }
                    }
                }
            }
        } else {
            head = returnGoalStrat(dungeon, goalStringList, 0);
        }
        return head;
    }

    private void checkGoalStatus(Dungeon dungeon) {
        if (currentGoals != null && currentGoals.evaluate()) {
            dungeon.setDungeonGoals("");
        }
    }

    private void updateHealthAnimation(Dungeon dungeon) {
        String hexTint = null;
        boolean update_flag = false;
        for (BattleEntities b : dungeon.getBattleEntities()) {
            update_flag = false;
            hexTint = "0x00ff00";
            if (b.getHealth() < 0.5) {
                hexTint = "0xff0000";
            }
            for (int i = 0; i < animations.size(); i++) {
                // if entity already exists in animation list, then update its health.
                if (animations.get(i).getEntityId().equals(b.getId())) {
                    animations.set(i,
                            new AnimationQueue("PostTick", b.getId(),
                                    Arrays.asList(String.format("healthbar set %f", b.getHealth() / 100),
                                            String.format("healthbar tint %s", hexTint)),
                                    false, -1));
                    update_flag = true;
                }
            }
            // If not then add the new battle entity with a health bar.
            if (!update_flag) {
                animations.add(new AnimationQueue("PostTick", b.getId(),
                        Arrays.asList(String.format("healthbar set %f", b.getHealth() / 100),
                                String.format("healthbar tint %s", hexTint)),
                        false, -1));
            }
        }
        dungeon.setAnimations(animations);
    }

    private void constantAnimations(Dungeon dungeon) {
        for (Entity e : dungeon.getEntities()) {
            if (e instanceof Treasure) {
                animations.add(new AnimationQueue("PostTick", e.getId(), Arrays.asList("sprite treasure",
                        "sprite treasure1", "sprite treasure2", "sprite treasure3", "sprite treasure4"), true, -20));
            }
        }
        dungeon.setAnimations(animations);
    }
}