package dungeonmania;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Item;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.MapLoader;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestHelper {

    /**
     * Helper function that returns the list of entiities from a preset dungeon in the form of a hashMap (Overload)
     * (this is so that testing comparisons are easier)
     * @param dungeonName
     * @return
     * @throws IOException
     */
    public static Map<Position, String> getEntitiesInfo(String dungeonName) throws IOException {   
        JSONObject map = MapLoader.loadMapFile(dungeonName);
        JSONArray jsonEntities = map.getJSONArray("entities");
        // create a new array to store all entities' positions and type
        Map<Position, String> entities = new HashMap<Position, String>();
        for (int i = 0; i < jsonEntities.length(); i++) {
            JSONObject entity = jsonEntities.getJSONObject(i);
            //when our json maps have "layers", positions will have to be changed accordingly
            entities.put(new Position(entity.getInt("x"), entity.getInt("y")), entity.getString("type"));
        }
        return entities;
    }

    /**
     * Helper function that returns a list of entities from a DungeonResponse in the form of a hashMap (Overload)
     * @param dungeon DungeonResponse
     * @return
     * @throws IOException
     * (this is so that testing comparisons are easier)
     */
    public static Map<Position, String> getEntitiesInfo(DungeonResponse dungeon) {   
        Map<Position, String> entities = new HashMap<Position, String>();
        for (EntityResponse entity : dungeon.getEntities()) {
            entities.put(entity.getPosition(), entity.getType());
        }
        return entities;
    }

    public static boolean isEntityTypeInPosition(DungeonResponse dungeon, String type, Position position) {
        try {
            String entity_at_position = getEntitiesInfo(dungeon).get(position);
            return type.equals(entity_at_position);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Entity Type in Position 2
     */
    public static boolean isEntityTypeInPosition2(DungeonResponse dungeon, String type, Position position) {
        
        for (EntityResponse entity : dungeon.getEntities()) {
            if (entity.getType().equals(type)) {
                if (entity.getPosition().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean doesInventoryContain(DungeonResponse dungeon, String itemtype) {
        for (ItemResponse item: dungeon.getInventory()) {
            if (itemtype.equals(item.getType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesBuildableContain(DungeonResponse dungeon, String itemtype) {
        for (String buildable: dungeon.getBuildables()) {
            if (itemtype.equals(buildable)) {
                return true;
            }
        }
        return false;
    }

    public static Map<Direction, String> getAdjacentEntityTypes(DungeonResponse dungeon, Position position) {
        HashMap<Direction, String> adjacent = new HashMap<Direction, String>();
        Map<Position, String> entities = getEntitiesInfo(dungeon);
        // initialise adjacents to null
        adjacent.put(Direction.UP, null);
        adjacent.put(Direction.DOWN, null);
        adjacent.put(Direction.RIGHT, null);
        adjacent.put(Direction.LEFT, null);
        if (entities.containsKey(position.translateBy(Direction.UP))) {
            adjacent.put(Direction.UP, entities.get(position.translateBy(Direction.UP)));

        } if (entities.containsKey(position.translateBy(Direction.DOWN))) {
            adjacent.put(Direction.DOWN, entities.get(position.translateBy(Direction.DOWN)));

        } if (entities.containsKey(position.translateBy(Direction.RIGHT))) {
            adjacent.put(Direction.RIGHT, entities.get(position.translateBy(Direction.RIGHT)));

        } if (entities.containsKey(position.translateBy(Direction.LEFT))) {
            adjacent.put(Direction.LEFT, entities.get(position.translateBy(Direction.LEFT)));

        }
        return adjacent;
    }

    /**
     * Returns number of entities of given type
     * @param type
     * @return
     */
    public static int getNumEntities(DungeonResponse dungeon, String type) {
        int count = 0;
        for (EntityResponse entity: dungeon.getEntities()) {
            if (entity.getType().equals(type)) {
                count++;
            }
        }
        return count;
    }
    
    public static<T extends Comparable<? super T>> void assertListAreEqualIgnoringOrder(List<T> a, List<T> b) {
        Collections.sort(a);
        Collections.sort(b);
        assertArrayEquals(a.toArray(), b.toArray());
    }


    /**
     * Helper function that returns the position of the top left corner of the dungeon
     */
    public static Position getDungeonTopLeftCorner(DungeonResponse dungeon) {
        Position top_left = new Position(100000, 100000);
        for (EntityResponse entity: dungeon.getEntities()) {
            if (entity.getPosition().getX() < top_left.getX() || entity.getPosition().getY() < top_left.getY()) {
                top_left = entity.getPosition();
            } else if (entity.getPosition().getX() == top_left.getX()) { // if same X value, then check Y value
                if (entity.getPosition().getY() < top_left.getY()) {
                    top_left = entity.getPosition();
                }
            } else if (entity.getPosition().getY() == top_left.getY()) { // if same Y value, then check X value
                if (entity.getPosition().getX() < top_left.getX()) {
                    top_left = entity.getPosition();
                }
            }
        }
        return top_left;
    }
    
    /**
     * Helper function that returns true if object of said type are in dungeon dimensions
     * Returns false otherwise
     */
    public static boolean isInSpawnRange(DungeonResponse dungeon, String type, Position dimensions) { 
        Position top_left = getDungeonTopLeftCorner(dungeon);
        Position bottom_right = top_left.translateBy(dimensions);
        for (EntityResponse entity: dungeon.getEntities()) { // loop through all entities
            if (entity.getType().equals(type)) { // for each entity with given type check if within dimensions
                if ((entity.getPosition().getX() <= top_left.getX()) || (entity.getPosition().getX() >= bottom_right.getX())
                 || (entity.getPosition().getY() <= top_left.getY()) || (entity.getPosition().getY() >= bottom_right.getY())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<String> getInventoryTypes(DungeonResponse dungeon) {
               // check inventory
        List<String> inventory = new ArrayList<>();
        for (ItemResponse item : dungeon.getInventory()) {    
            inventory.add(item.getType());
        }


        return inventory;
               
    }

    public static List<String> getDungeonInventoryTypes(Dungeon dungeon) {
        List<String> dungeonInventory = new ArrayList<>();
        for (Item item : dungeon.getDungeonInventory().getDungeonInventory()) {    
            dungeonInventory.add(item.getType());
        }


        return dungeonInventory;
    }

    public static List<String> getEntitiesTypes(DungeonResponse dungeon) {
        List<String> entities = new ArrayList<>();
        for (EntityResponse entity : dungeon.getEntities()) {    
            entities.add(entity.getType());
        }
        return entities;
    }
}
