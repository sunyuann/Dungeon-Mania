package dungeonmania.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.entities.moving.Assassin;
import dungeonmania.main.entities.moving.Mercenary;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

public class MapLoader {

    /**
     * Helper function that loads a preset dungeon JSONmap when given the dungeon
     * name
     * 
     * @param dungeonName
     * @return the map in the form of JSONObject
     * @throws IOException
     */
    public static JSONObject loadMapFile(String dungeonName) throws IOException {
        String path = "/dungeons";
        List<String> dungeons = FileLoader.listFileNamesInResourceDirectory(path);
        if (dungeons.contains(dungeonName)) {
            path += ("/" + dungeonName + ".json");
        } else {
            throw new FileNotFoundException(dungeonName);
        }
        return new JSONObject(FileLoader.loadResourceFile(path));
    }

    /**
     * Helper function to get the list of entities from a JSONmap
     * 
     * @param dungeon
     * @return
     */
    public static List<EntityResponse> JSONgetEntityResponse(JSONObject dungeon) {
        JSONArray jsonEntities = dungeon.getJSONArray("entities");
        List<EntityResponse> entities = new ArrayList<>();
        for (int i = 0; i < jsonEntities.length(); i++) {
            JSONObject entity = jsonEntities.getJSONObject(i);
            String type = "";
            switch (entity.getString("type")) {
            case "key":
                if (entity.getInt("key") == 1) {
                    type = "key_gold";
                } else if (entity.getInt("key") == 2) {
                    type = "key_silver";
                }
                entities.add(new EntityResponse(
                        entity.getString("type") + "-(" + entity.getInt("x") + ", " + entity.getInt("y") + ")", type,
                        new Position(entity.getInt("x"), entity.getInt("y")), false));
                break;
            case "portal":
                if (entity.getString("colour").equals("BLUE")) {
                    type = "portal_blue";
                } else if (entity.getString("colour").equals("RED")) {
                    type = "portal_red";
                } else if (entity.getString("colour").equals("ORANGE")) {
                    type = "portal_orange";
                } else if (entity.getString("colour").equals("BLACK")) {
                    type = "portal_black";
                }
                entities.add(new EntityResponse(
                        entity.getString("type") + "-(" + entity.getInt("x") + ", " + entity.getInt("y") + ")", type,
                        new Position(entity.getInt("x"), entity.getInt("y")), false));
                break;
            case "door":
                if (entity.getInt("key") == 1) {
                    type = "door_locked_gold";
                } else if (entity.getInt("key") == 2) {
                    type = "door_locked_silver";
                }
                entities.add(new EntityResponse(
                        entity.getString("type") + "-(" + entity.getInt("x") + ", " + entity.getInt("y") + ")", type,
                        new Position(entity.getInt("x"), entity.getInt("y")), false));
                break; 
            default:
                entities.add(new EntityResponse(
                        entity.getString("type") + "-(" + entity.getInt("x") + ", " + entity.getInt("y") + ")",
                        entity.getString("type"), new Position(entity.getInt("x"), entity.getInt("y")), false));
            }
        }
        return entities;
    }

    public static void updateInternalValues(Dungeon dungeon, JSONObject jsonDungeon) {
        JSONArray jsonEntities = jsonDungeon.getJSONArray("entities");
        for (int i = 0; i < jsonEntities.length(); i++) {
            JSONObject entity = jsonEntities.getJSONObject(i);
            switch (entity.getString("type")) {
                case "player":
                    if (entity.has("health")) {
                        dungeon.getPlayer().setHealth(entity.getDouble("health"));
                    }
                    if (entity.has("invisible_ticks")) {
                        dungeon.getPlayer().addToInvisibleTicks(entity.getInt("invisible_ticks"));
                    }
                    if (entity.has("invincible_ticks")) {
                        dungeon.getPlayer().addToInvincibleTicks(entity.getInt("invincible_ticks"));
                    }
                    break;
                case "mercenary_ally":
                    if (entity.has("mind_control_ticks")) {
                        for (Entity e: dungeon.getEntitiesLocation().get(dungeon.getPlayer().getPosition())) {
                            if (e instanceof Mercenary) {
                                Mercenary m = (Mercenary) e;
                                m.setMindControlTicksRemaining(entity.getInt("mind_control_ticks"));
                            }
                        }
                    }
                    break;
                case "assassin_ally":
                    if (entity.has("mind_control_ticks")) {
                        for (Entity e: dungeon.getEntitiesLocation().get(dungeon.getPlayer().getPosition())) {
                            if (e instanceof Assassin) {
                                Assassin m = (Assassin) e;
                                m.setMindControlTicksRemaining(entity.getInt("mind_control_ticks"));
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Helper function to get the list of inventory items from a JSONmap
     * 
     * @param dungeon
     * @return
     */
    public static List<ItemResponse> JSONgetInventory(JSONObject dungeon) {
        JSONArray jsonInventory = dungeon.getJSONArray("inventory");
        List<ItemResponse> inventory = new ArrayList<>();
        for (int i = 0; i < jsonInventory.length(); i++) {
            JSONObject item = jsonInventory.getJSONObject(i);
            inventory.add(new ItemResponse(item.getString("id"), item.getString("type")));
        }
        return inventory;
    }

    /**
     * Helper function to get the list of buildables from a JSONmap
     * 
     * @param dungeon
     * @return
     */
    public static List<String> JSONgetBuildables(JSONObject dungeon) {
        JSONArray jsonBuildables = dungeon.getJSONArray("buildables");
        List<String> buildables = new ArrayList<>();
        for (int i = 0; i < jsonBuildables.length(); i++) {
            String buildable = jsonBuildables.getString(i);
            buildables.add(buildable);
        }
        return buildables;
    }

    public static String JSONgetGoals(JSONObject jsonGoals) {
        String goalString = "";
        if (jsonGoals.getString("goal").equals("AND")) {
            JSONArray jsonSubGoals = jsonGoals.getJSONArray("subgoals");
            for (Integer i = 0; i < jsonSubGoals.length(); i++) {
                goalString += JSONgetGoals(jsonSubGoals.getJSONObject(i));
                if (i < jsonSubGoals.length() - 1) {
                    goalString += " AND ";
                }
            }
        } else if (jsonGoals.getString("goal").equals("OR")) {
            JSONArray jsonSubGoals = jsonGoals.getJSONArray("subgoals");
            for (Integer i = 0; i < jsonSubGoals.length(); i++) {
                goalString += JSONgetGoals(jsonSubGoals.getJSONObject(i));
                if (i < jsonSubGoals.length() - 1) {
                    goalString += " OR ";
                }
            }
        } else if (jsonGoals.getString("goal").equals("enemies")) {
            goalString += ":enemies";
        } else if (jsonGoals.getString("goal").equals("boulders")) {
            goalString += ":boulder";
        } else if (jsonGoals.getString("goal").equals("treasure")) {
            goalString += ":treasure";
        } else if (jsonGoals.getString("goal").equals("exit")) {
            goalString += ":exit";
        }
        return goalString;

    }
}
