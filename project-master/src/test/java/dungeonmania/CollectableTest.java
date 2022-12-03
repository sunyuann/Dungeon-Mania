package dungeonmania;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import static dungeonmania.TestHelper.assertListAreEqualIgnoringOrder;
import static dungeonmania.TestHelper.getInventoryTypes;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectableTest {

    DungeonManiaController controller;
    
    // no need to make a new controller in each test
    @BeforeEach
    void setUp() {
        controller = new DungeonManiaController();
    }

    // helper function to move player down and collect item
    public List<String> collectableTestHelper() {
        // make player move down once - in collectable simple this means the player should collect an armour
        DungeonResponse downOnce = controller.tick(null, Direction.DOWN);

        // get the inventory after moving down once
        List<ItemResponse> ourItems = downOnce.getInventory();
        
        // System.out.println(ourItems);

        // create a string of inventorys type
        List<String> ourTypes = new ArrayList<>();
        for (ItemResponse item: ourItems) {
            ourTypes.add(item.getType());
        }

        return ourTypes;

    }

    // /** Case 1: making sure each collectable item is able to be collected - valid case for all collectables
    @Test
    public void testValidCollectSaveLoadGame() {
        
        // create the dungeon
        controller.newGame("collectableSimple", "Standard");

        assertListAreEqualIgnoringOrder(Arrays.asList("armour"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb"), collectableTestHelper());
        
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword"), collectableTestHelper());
    
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"), collectableTestHelper());

        DungeonResponse save = controller.saveGame("collectableSimple-" + new Date().getTime());

        assertDoesNotThrow(() -> {
            DungeonResponse load = controller.loadGame(save.getDungeonId());
            assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"), getInventoryTypes(load));
        });
    }


    // * Case 2: test that multiple of the same item can be collected.

    @Test
    public void testValidCollectMultipleEntities() {
        
        // create the dungeon
        controller.newGame("collectableMultiple", "Standard");
        
        assertListAreEqualIgnoringOrder(Arrays.asList("wood"), collectableTestHelper());
        
        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood"), collectableTestHelper());
        
        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "arrow"), collectableTestHelper());
        
        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "arrow", "arrow"), collectableTestHelper());
        
        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "arrow", "arrow", "arrow"), collectableTestHelper());
        
        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "arrow", "arrow", "arrow", "wood"), collectableTestHelper());

    }
    
    //  * Case 3: invalid: only one key exists in the inventory at a time - try add one key after another - should not work.
    @Test
    public void testInvalidCollectTwoKeysSaveLoad() {
        
        // create the dungeon
        controller.newGame("collectableTwoKeys", "Standard");

        assertListAreEqualIgnoringOrder(Arrays.asList("key_silver"), collectableTestHelper());

        assertListAreEqualIgnoringOrder(Arrays.asList("key_silver"), collectableTestHelper());

        DungeonResponse save = controller.saveGame("collectableTwoKeys-" + new Date().getTime());

        assertDoesNotThrow(() -> {
            DungeonResponse load = controller.loadGame(save.getDungeonId());
            assertListAreEqualIgnoringOrder(Arrays.asList("key_silver"), getInventoryTypes(load));
        });
    }
  
    //  * Case 4: make sure inventory doesn't include uncollectable entities - door
    @Test
    public void testInvalidCollectDoor() {
        controller.newGame("doorandkey", "Standard");

        DungeonResponse move = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("key_gold"), getInventoryTypes(move));

        // player is now at (1,3)
        controller.tick(null, Direction.DOWN);

        // player should be able to move into door 
        move = controller.tick(null, Direction.DOWN);

        // check door has not been collected
        assertTrue(getInventoryTypes(move).isEmpty());
    }

}
