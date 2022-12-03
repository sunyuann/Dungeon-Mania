package dungeonmania.main;

import dungeonmania.response.models.ItemResponse;

import org.json.JSONObject;

public class Item {

    ItemResponse item;
    Dungeon dungeon;

    /**
     * Constructor of Item
     * @param dungeon
     * @param entity
     */
    public Item(Dungeon dungeon, Entity entity) {
        this.item = new ItemResponse(entity.getId(), entity.getType());
        this.dungeon = dungeon;
    }

    /**
     * Constructor of Item
     * @param dungeon
     * @param item
     */
    public Item(Dungeon dungeon, ItemResponse item) {
        this.item = item;
        this.dungeon = dungeon;
    }

    // Method to update items based on player movement
    public void update() {}

    // Getters

    public Dungeon getDungeon() {
        return dungeon;
    }

    public String getId() {
        return item.getId();
    }

    public String getType() {
        return item.getType();
    }

    public ItemResponse getItemResponse() {
        return item;
    }

    public JSONObject toJSON() {
        JSONObject jsonItem = new JSONObject();
        jsonItem.put("id", getId());
        jsonItem.put("type", getType());
        return jsonItem;
    }
}
