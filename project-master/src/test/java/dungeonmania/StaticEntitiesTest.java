package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;
import static dungeonmania.TestHelper.getInventoryTypes;
import static dungeonmania.TestHelper.assertListAreEqualIgnoringOrder;

public class StaticEntitiesTest {
        // Test if you can push boulder
        @Test
        public void testBoulderPush() {
                // Move character into boulder and then check positions
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("boulders", "Standard");
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.RIGHT), "boulder",
                                new Position(4, 2)));
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "player",
                                new Position(3, 2)));
        }

        // Test if you can push a boulder next to a wall (you shouldn't be able to)
        @Test
        public void testBoulderNextToWall() {
                // Move character into boulder than is next to wall, positions shouldn't change
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("boulders", "Standard");
                controller.tick(null, Direction.RIGHT);
                controller.tick(null, Direction.RIGHT);
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.RIGHT), "boulder",
                                new Position(5, 2)));
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "player",
                                new Position(4, 2)));
        }

        //// FLOOR SWITCH TESTS ////

        // Test floor switch trigger.
        @Test
        public void testFloorSwitchBoulderTrigger() {
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("boulderonswitch", "Peaceful");
                DungeonResponse current = controller.tick(null, Direction.DOWN);
                assertEquals(current.getGoals(), "");
                // how to check for trigger?
                // Move boulder onto floor switch, check for trigger
        }

        // Test if floor switch triggers on other entities other than boulder.
        @Test
        public void testEntityOnFloorSwitch() {
                // Move spider on floor switch, shouldn't trigger
                DungeonManiaController controller = new DungeonManiaController();
                // map has a spider at (4, 4) and switch on (4, 3)
                controller.newGame("spideronswitch", "Peaceful");
                DungeonResponse current = controller.tick(null, Direction.NONE);
                // Goal is boulders. So if spider is on switch goal shouldn't complete.
                assertEquals(current.getGoals(), ":boulder");
        }

        //// DOOR TESTS ////

        // Test opening door with key in inventory
        @Test
        public void testDoorWithKey() {
                // Move character into door with key
                DungeonManiaController controller = new DungeonManiaController();
                // player (1,1), key(1,2), door(1,4)
                assertTrue(TestHelper.isEntityTypeInPosition(controller.newGame("doorandkey", "Standard"), "player",
                                new Position(1, 1)));

                DungeonResponse move = controller.tick(null, Direction.DOWN);
                assertListAreEqualIgnoringOrder(Arrays.asList("key_gold"), getInventoryTypes(move));
                assertTrue(TestHelper.isEntityTypeInPosition(move, "player", new Position(1, 2)));

                // player is now at (1,3)
                controller.tick(null, Direction.DOWN);

                // player should be able to move into door 
                move = controller.tick(null, Direction.DOWN);

                // and the key should be removed from inventory
                assertTrue(getInventoryTypes(move).isEmpty());
                // player should be in the position of door
                assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(1, 4)));
               
                // player should be able to move passed the door
                move = controller.tick(null, Direction.DOWN);
                assertTrue(TestHelper.isEntityTypeInPosition(move, "player", new Position(1, 5)));

                // collect silver key
                move = controller.tick(null, Direction.DOWN);
                assertListAreEqualIgnoringOrder(Arrays.asList("key_silver"), getInventoryTypes(move));



        }

        // Test opening door with key in inventory
        @Test
        public void testDoorWithWrongKey() {
                // Move character into door with key
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("doorandkey", "Standard");
          
                controller.tick(null, Direction.RIGHT);
                DungeonResponse move = controller.tick(null, Direction.DOWN);

                assertListAreEqualIgnoringOrder(Arrays.asList("key_silver"), getInventoryTypes(move));
                assertTrue(TestHelper.isEntityTypeInPosition(move, "player", new Position(2, 2)));

                // player is now at (2,3)
                controller.tick(null, Direction.DOWN);

                // player is now at (2,4)
                controller.tick(null, Direction.DOWN);

                // player attempts to move left to (1,4) but it should not work
                move = controller.tick(null, Direction.LEFT);

                // and the key should still be in the inventory because the key was not correct
                assertListAreEqualIgnoringOrder(Arrays.asList("key_silver"), getInventoryTypes(move));

                // player should still be in its original position at (2,4)
                assertTrue(TestHelper.isEntityTypeInPosition2(move, "player", new Position(2, 4)));
                
        
        }
        

        // Test opening door with no key / sun stone in inventory
        @Test
        public void testDoorNoKeyNoSunStone() {
                // Move character into door with key
                DungeonManiaController controller = new DungeonManiaController();
                // player (1,1), key(1,2), door(1,4)
                controller.newGame("doorandkey", "Standard");
                controller.tick(null, Direction.RIGHT);
                controller.tick(null, Direction.DOWN);
                controller.tick(null, Direction.DOWN);
                controller.tick(null, Direction.DOWN);
                DungeonResponse move = controller.tick(null, Direction.LEFT);
                // Door shouldn't open, player should stay where he is right now
                assertTrue(TestHelper.isEntityTypeInPosition(move, "player", new Position(2, 4)));
        }

        //// PORTAL TESTS ////
        @Test
        public void testGoIntoPortal() {
                // Move character into a portal, check positions
                // player(0,0), portal(1,0), portal(4,0)
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("portals", "Standard");
                controller.tick(null, Direction.RIGHT);
                // assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.RIGHT), "player",
                //                 new Position(5, 0)));
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "player", new Position(5, 0)));
        }

        @Test
        public void testMultiplePortals() {
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("multipleportals", "Standard");
                controller.tick(null, Direction.DOWN);
                controller.tick(null, Direction.DOWN);
                controller.tick(null, Direction.DOWN);
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.DOWN), "player", new Position(10, 11)));

        }

        @Test
        public void testBlockedPortals() {
                // features a portal to the right of the player, the other portal is blocked by walls
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("portal_blocked", "Standard");
                controller.tick(null, Direction.RIGHT);
                // after moving right and left, player should be in the starting position
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.LEFT), "player", new Position(0, 0)));

                // map also has a different coloured portal to the left, leading to a portal with walls around it except the bottom tile is empty
                // after moving to the left, player should be teleported to the only space available, the bottom tile
                assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.LEFT), "player", new Position(0, 5)));
        }

        //// ZOMBIE TOAST SPAWNER TESTS ////

        // Test spawner is spawning zombies every 20 ticks.
        @Test
        public void testZombieToastSpawner() {
                // Spawn entities with zombie spawner (1 per 20tick), check amount of entities
                // after on screen
                DungeonManiaController controller = new DungeonManiaController();
                DungeonResponse current = controller.newGame("zombiespawner", "Peaceful");
                for (Integer i = 0; i < 40; i++)
                        current = controller.tick(null, Direction.NONE);
                // 1 player, 2 zombies, 1 zombie spawner, 1 sp
                assertEquals(current.getEntities().size(), 5);
        }

        // Test spawner is not spawning because blocked
        @Test
        public void testZombieToastSpawnerBlocked() {
                // Spawn entities with zombie spawner (1 per 20tick), check amount of entities
                // after on screen
                // Four different zombie spawners ranging from having 1 to 4 adjacent walls
                DungeonManiaController controller = new DungeonManiaController();
                DungeonResponse current = controller.newGame("zombiespawnerblocked", "Peaceful");
                for (Integer i = 0; i < 20; i++)
                        current = controller.tick(null, Direction.NONE);
                // the spawner with 4 walls adjacent should not spawn a zombie
                // 1 player, 3 zombies, 4 spawner, 10 walls
                assertEquals(current.getEntities().size(), 18);
        }
}
