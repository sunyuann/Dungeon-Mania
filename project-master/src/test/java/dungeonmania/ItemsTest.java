package dungeonmania;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;
import dungeonmania.response.models.DungeonResponse;
import static dungeonmania.TestHelper.assertListAreEqualIgnoringOrder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import dungeonmania.util.Position;

import dungeonmania.response.models.EntityResponse;
import static dungeonmania.TestHelper.getInventoryTypes;
import static dungeonmania.TestHelper.getEntitiesTypes;



public class ItemsTest {

    DungeonManiaController controller;
    
    // no need to make a new controller in each test
    @BeforeEach
    void setUp() {
        controller = new DungeonManiaController();
        controller.newGame("collectableSimple", "Standard");

        for (int i = 0; i < 14; i++) {
            controller.tick(null, Direction.DOWN);
        }
    }

    @Test
    public void test_selectableItems() {


        // test that when selectableItems are used they are removed from the inventory


        // check all items have been collected
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "bomb", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"), getInventoryTypes(controller.tick(null, Direction.DOWN)));

        // use bomb
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"),  getInventoryTypes(controller.tick("bomb-(0, 3)", Direction.NONE)));
        // use health potion
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"), getInventoryTypes(controller.tick("health_potion-(0, 4)", Direction.NONE)));
        // use invincibility potion
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"),  getInventoryTypes(controller.tick("invincibility_potion-(0, 5)", Direction.NONE)));
        // use invisibility potion
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"),  getInventoryTypes(controller.tick("invisibility_potion-(0, 6)", Direction.NONE)));
    
    }

    @Test 
    public void test_invisibility_potion() {
        // test that spider stays alive after battle because no battle happened

    }

    @Test
    public void test_invincibility_potion() {
        // test that battle did happen but health no change?? hmmmmm
    }

    @Test
    public void test_health_potion() {
        // test health is restored by x amount


    }

    @Test 
    public void test_bribe() {
        // bribe mercenary and see that thingo is removed
        
    }

    @Test
    public void test_drop_bomb() {

        // move down once and get position of player
        DungeonResponse moveDown = controller.tick(null, Direction.DOWN);
        Position dropPosition = null;
        for (EntityResponse entity: moveDown.getEntities()) {
            if (entity.getType().equals("player")) {
                dropPosition = entity.getPosition();
            }
        }

        assertNotNull(dropPosition);

        // drop bomb at current position
        DungeonResponse dropBomb = controller.tick("bomb-(0, 3)", Direction.NONE);

        // check that the bomb's position equals the players position
        assertTrue(TestHelper.isEntityTypeInPosition(dropBomb, "bomb", dropPosition));

        // move player down
        controller.tick(null, Direction.DOWN);

        // move player back up and check that the inventory list does not contain a bomb because a bomb can only be collected once; once dropped a bomb cannot be collected again
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "arrow", "health_potion", "invincibility_potion", "invisibility_potion", "key_gold", "sword", "treasure", "wood", "one_ring", "sun_stone", "anduril"), getInventoryTypes(controller.tick(null, Direction.UP)));
        
    }

    @Test
    public void test_bomb_detonation() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bombdetonation", "Peaceful");
        // collect bomb at (1,2)
        controller.tick(null, Direction.DOWN);
        // drop bomb at (1,3)
        controller.tick(null, Direction.DOWN);
        controller.tick("bomb-(1, 2)", Direction.NONE);
        // player moves (2, 3)
        controller.tick(null, Direction.RIGHT);
        // player moves to (3, 3)
        controller.tick(null, Direction.RIGHT);
        // player mvoes to (3, 4)
        controller.tick(null, Direction.DOWN);
        // player pushes boulder and moves to  to (2, 4) - bomb should detonate
        DungeonResponse d = controller.tick(null, Direction.LEFT);
        d = controller.tick(null, Direction.LEFT);


        // only player should exist
        assertListAreEqualIgnoringOrder(Arrays.asList("player"), getEntitiesTypes(d));

        
    }

    @Test
    public void test_bomb_detonation_immediate() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bombdetonation", "Peaceful");
        // collect bomb at (1,2)
        controller.tick(null, Direction.DOWN);
        // player moves to (1,3)
        controller.tick(null, Direction.DOWN);
        // player moves (2, 3)
        controller.tick(null, Direction.RIGHT);
        // player moves to (3, 3)
        controller.tick(null, Direction.RIGHT);
        // player mvoes to (3, 4)
        controller.tick(null, Direction.DOWN);
        // player pushes boulder and moves to (2,4)
        controller.tick(null, Direction.LEFT);
        // player drops bomb
        DungeonResponse d = controller.tick("bomb-(1, 2)", Direction.NONE);
        // only player should exist
        assertListAreEqualIgnoringOrder(Arrays.asList("player"), getEntitiesTypes(d));
    }

    @Test
    public void test_bomb_detonation_before_collection() {
        // Even if bomb is in the correct position to detonate (i.e adjacent floor switch is triggered), if it has not been collected yet,
        // it should NOT detonate

        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bombNoDetonation", "Peaceful");
        // push boulder onto floorswitch by moving down
        DungeonResponse triggered = controller.tick(null, Direction.DOWN);

        assertListAreEqualIgnoringOrder(Arrays.asList("player", "boulder", "switch", "bomb"), getEntitiesTypes(triggered));


    }

    /** SUN STONE TESTS */

     /**
      * 1. test opening door with sun stone - check sun stone is retained (no key and door 1)
      * 2. test opening door with sun stone - check sun stone is retained (no key and door 2)
      */
    @Test
    public void test_DoorsAndSunStone() {

        // test sun stone opens door of type one and still exists in the inventory once door is opened.
        
        // player (1,1)
        assertTrue(TestHelper.isEntityTypeInPosition(controller.newGame("doorAndSunStone", "standard"), "player",
                        new Position(1, 1)));

        // move player right to (2, 1)
        controller.tick(null, Direction.RIGHT);

        // move player down twice - player is now at (2,3)
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // move player left to pick up sun stone (1, 3)
        DungeonResponse move = controller.tick(null, Direction.LEFT);

        // sun stone should still be in inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(move));

        // move the player up once - door position (1, 2)
        move = controller.tick(null, Direction.UP);

        // player should be in the position of door
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 2)));
       
        // player should be able to move passed the door - move up
        move = controller.tick(null, Direction.UP);
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 1)));

        // sun stone should still be in inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(move));

        // use the same sun stone on door of type 2

        // move player down 3 times to stand on door 2 (1, 4)
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        move = controller.tick(null, Direction.DOWN);

        // // player should be in the position of door
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 4)));

        // player should be able to move passed the door - move up
        move = controller.tick(null, Direction.UP);
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 3)));

        // sun stone should still be in inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(move));

    }

    /**
     * 3. test opening door with correct key and sun stone - check sun stone is retained but key is not (key 1 and door 1)
     * 4. test opening door with correct key and sun stone - check sun stone is retained but key is not (key 2 and door 2)
     */
    @Test
    public void test_DoorsCorrectKeyAndSunStone() {
        
        // test that when sun stone a key both exist in inventory, key is used first to open the corresponding door - check for both silver and gold

        // map should have 1 sun stone, 1 gold key, 1 gold door, 1 silver key, 1 silver door
        
        // player (1,1)
        assertTrue(TestHelper.isEntityTypeInPosition(controller.newGame("doorSunStoneAndKey", "standard"), "player",
                        new Position(1, 1)));

        // move down 2 times to collect sun stone and gold key
        controller.tick(null, Direction.DOWN);
        DungeonResponse move = controller.tick(null, Direction.DOWN);

        // check inventory has those 2
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "key_gold"), getInventoryTypes(move));

        // move down again to go through gold door
        move = controller.tick(null, Direction.DOWN);
        
        // check that sun stone is still in the inventory - key shouldve been used
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(move));

        // make sure the the player is on the same position as door (unlocked)
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 4)));

        // move down again to collect silver key
        move = controller.tick(null, Direction.DOWN);
        
        // check inventory has sun stone + silver key
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "key_silver"), getInventoryTypes(move));

        // move down to go through silver door
        move = controller.tick(null, Direction.DOWN);

        // check that sun stone is still in the inventory - key shouldve been used
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(move));

        // make sure the the player is on the same position as door (unlocked)
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 6)));

    }

    /**
     * 5. test opening door with incorrect key and sun stone - check sun stone and key are both retained (key 1 and door 2)
     * 6. test opening door with incorrect key and sun stone - check sun stone and key are both retained (key 2 and door 1)
     */
    @Test
    public void test_DoorsIncorrectKeySAndSunStone() {
        // test that when sun stone a key both exist in inventory if we do not have a corresponding key, sun_stone is used

        // map should have 1 sun stone, 1 gold key, 1 gold door, 1 silver key, 1 silver door
        
        // player (1,1)
        assertTrue(TestHelper.isEntityTypeInPosition(controller.newGame("doorSunStoneAndWrongKey", "standard"), "player",
                        new Position(1, 1)));
        
        // move down 2 times to collect sun stone and silver key
        controller.tick(null, Direction.DOWN);
        DungeonResponse move = controller.tick(null, Direction.DOWN);

        // check inventory has those 2
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "key_silver"), getInventoryTypes(move));

        // move down again to go through gold door
        move = controller.tick(null, Direction.DOWN);
        
        // check that both key and sun stone are still in inventory - silver is wrong key so stone should have been used but not removed.
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "key_silver"), getInventoryTypes(move));

        // make sure the the player is on the same position as door (unlocked)
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 4)));

    }

    @Test
    public void test_DoorsIncorrectKeyGAndSunStone() {
        controller.newGame("doorSunStoneAndWrongKey", "standard");

        // move down once - collect sun stone
        controller.tick(null, Direction.DOWN);
        
        // move right once, down 3 times then left to collect gold key
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse move = controller.tick(null, Direction.LEFT);

        
        // check inventory has sun stone + gold key
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "key_gold"), getInventoryTypes(move));

        // move down to go through silver door
        move = controller.tick(null, Direction.DOWN);

        // check that both key and sun stone are still in inventory - gold is wrong key so stone should have been used but not removed.
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "key_gold"), getInventoryTypes(move));

        // make sure the the player is on the same position as door (unlocked)
        assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 6)));

    }

}