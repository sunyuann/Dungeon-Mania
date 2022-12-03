package dungeonmania.main;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;

public class Game {

    /**
     * A Game class includes the dungeon that is being played, whilst also including
     * the gameMode
     * 
     * @param dungeon
     * @param gameMode
     */
    private Dungeon dungeon;
    private String gameMode;
    private int tick_count;
    private Position spawn_position;

    /**
     * Constructor for Game
     * 
     * @param dungeon  Dungeon class
     * @param gameMode
     */
    public Game(Dungeon dungeon, String gameMode) {
        this.dungeon = dungeon;
        this.gameMode = gameMode;
        this.tick_count = 0;
        this.spawn_position = dungeon.getPlayer().getPosition();
    }

    /**
     * Constructor for Game
     * 
     * @param dungeon  Dungeon class
     * @param gameMode
     * @param tick_count
     * @param spawn_position
     */
    public Game(Dungeon dungeon, String gameMode, int tick_count, Position spawn_position) {
        this.dungeon = dungeon;
        this.gameMode = gameMode;
        this.tick_count = tick_count;
        this.spawn_position = spawn_position;
    }

    /**
     * Getter for tick_count
     * 
     * @return
     */
    public int getTickCount() {
        return tick_count;
    }

    /**
     * Setter for tick_count, increments by 1
     */
    public void tick() {
        tick_count++;
    }

    /**
     * if game mode is peaceful, return false (skip battle)
     * else return true
     * @return
     */
    public boolean isBattle() {
        if (gameMode.equals("peaceful")) {
            return false;
        }
        return true;
    }

    /**
     * returns true if current tick should spawn spider (every 30 ticks) 
     * and if number of spiders don't exceed 5
     * 
     * @return
     */
    public boolean isSpiderSpawn() {
        return (tick_count % 30 == 0) && (tick_count != 0) && (dungeon.getNumEntities("spider") < 5);
    }

    /**
     * returns true if current tick should spawn hydra (every 50 ticks) 
     * 
     * @return
     */
    public boolean isHydraSpawn() {
        if (gameMode.equals("hard")) {
            return (tick_count % 50 == 0) && (tick_count != 0);
        }
        return false;
    }

    /**
     * 30% chance that assassin will spawn in the place of mercenary
     * if assassin spawns, return true
     * else return false
     * @return
     */
    public boolean isAssassinSpawn() {
        // randomly generate number from 0 to 9 
        int random = new Random().nextInt(10);
        if (random < 3) { // if random is 0,1,2 then return true
            return true;
        }
        // if random is 3,4,5,6,7,8,9, then return false 
        return false;
    }

    /**
     * returns true if current tick should spawn Bribable entity 
     * (every 50 ticks if standard Gamemode)
     * (every 40 ticks if hard Gamemode) 
     * @return
     */
    public boolean isBribableEntitySpawn() {
        if (gameMode.equals("hard")) {
            return (tick_count % 40 == 0) && (tick_count != 0); 
        } else if (gameMode.equals("standard")) {
            return (tick_count % 50 == 0) && (tick_count != 0);
        }
        return false;
    }

    /**
     * returns true if current tick should spawn zombie (if hard, every 15 ticks)
     * (otherwise 20 ticks)
     * 
     * @return
     */
    public boolean isZombieSpawn() {
        if (gameMode.equals("hard")) {
            return tick_count % 15 == 0;
        } else {
            return tick_count % 20 == 0;
        }
    }

    /**
     * Getter for initial spawn position of player
     * @return
     */
    public Position getSpawnPosition() {
        return spawn_position;
    }

    /**
     * Getter for gameMode
     * 
     * @return
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * Method that returns dungeon
     * 
     * @return Dungeon
     */
    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * Method that returns dungeon info
     * 
     * @return DungeonResponse
     */
    public DungeonResponse getDungeonResponse() {
        return dungeon.getDungeonResponse();
    }

    /**
     * Getter for dungeonId
     * 
     * @return
     */
    public String getDungeonId() {
        return dungeon.getDungeonId();
    }

    /**
     * Setter for dungeon (Overload)
     * 
     * @param dungeon Dungeon class
     */
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Setter for dungeon (Overload)
     * 
     * @param dungeon DungeonResponse class
     */
    public void setDungeon(DungeonResponse dungeon) {
        this.dungeon.setDungeonResponse(dungeon);
    }

    public JSONObject toJSON() {
        JSONObject json = dungeon.toJSON();
        // stores the current gameMode into the json dungeon file as well
        json.put("gameMode", gameMode);
        json.put("tick_count", tick_count);
        json.put("spawn_x", spawn_position.getX());
        json.put("spawn_y", spawn_position.getY());
        return json;
    }
}
