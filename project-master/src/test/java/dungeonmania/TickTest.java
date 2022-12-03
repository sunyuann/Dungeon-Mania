package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TickTest {
    @Test
    public void test_tickPlayerMovement_Valid() {
        // test player movement when not blocked
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Standard");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.RIGHT);
        // check entity at position is player
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.UP), "player", new Position(3,2)));
    }

    @Test
    public void test_tickPlayerMovement_blocked() {
        // test player movement when blocked by wall
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Standard");
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.DOWN), "player", new Position(1,3)));
    }

    @Test
    public void test_tickPlayerMovement_swamp() {
        // test player movement when traversing through swamp
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("swamp_tile", "Standard");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse dungeon = controller.tick(null, Direction.DOWN);
        assertTrue(TestHelper.isEntityTypeInPosition(dungeon, "player", new Position(0,2)) || TestHelper.isEntityTypeInPosition(dungeon, "player", new Position(0,3)));
    }

    @Test
    public void test_tickPlayerMovement_swamp_two() {
        // test player movement when traversing through swamp
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("swamp_two", "Standard");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        // only on this tick wil the player move out of the first tile
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.DOWN), "player", new Position(0,2)));
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.DOWN), "player", new Position(0,3)));
    }

    @Test
    public void test_tickSpiderSpawn_frequency() {
        // test spider spawns every 10 ticks
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Peaceful");
        for (int i = 0; i < 58; i++) { // runs 58 ticks 
            controller.tick(null, Direction.NONE);
        }
        // 59th tick does not spawn anything, 1 spider total
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "spider") == 1);
        // 60th tick spawns spider, 2 spiders total
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "spider") == 2);
    }

    @Test 
    public void test_tickSpiderSpawn_initialfrequency() {
        // test spider spawns at exactly 30 ticks
        // test spider spawns every 30 ticks
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Peaceful");
        for (int i = 0; i < 28; i++) { // runs 28 ticks
            controller.tick(null, Direction.NONE);
        }
        // 29th tick does not spawn anything, no spiders yet
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "spider") == 0);
        // 30th tick spawns spider, 1 spider total
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "spider") == 1);
    }

    @Test 
    public void test_tickSpiderSpawn_maxSpiders() {
        // test spider spawns limited after total of 5 spiders currently in game
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Peaceful");
        for (int i = 0; i < 149; i++) { // runs 149 ticks
            controller.tick(null, Direction.NONE);
        }
        // 150th tick, 5 spiders total
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "spider") == 5);
        for (int i = 0; i < 49; i++) { // runs 60 ticks
            controller.tick(null, Direction.NONE);
        }
        // 211th tick, no spiders spawn after 5th spider
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "spider") == 5);
    }

    @Test 
    public void test_tickSpiderSpawn_spidersWithinBound() {
        // test spider all spawns within range
        for (int i = 0; i < 100; i++) { // runs this process 100 times to ensure spiders consistently spawn within bounds
            DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("maze", "Peaceful");
            for (int j = 0; j < 29; j++) { // runs 29 ticks
                controller.tick(null, Direction.NONE);
            }
            // assert spider than spawns in 30th tick is within dungeon dimensions, which is (20, 18) for "maze"
            assertTrue(TestHelper.isInSpawnRange(controller.tick(null, Direction.NONE), "spider", new Position(20, 18)));
        }
    }

    @Test
    public void test_tickHydraSpawn_frequency() {
        // test hydra spawns every 50 ticks
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Hard");
        for (int i = 0; i < 98; i++) { // runs 98 ticks 
            controller.tick(null, Direction.NONE);
        }
        // 99th tick does not spawn anything, 1 hydra total
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "hydra") == 1);
        // 100th tick hydra spider, 2 spiders total
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "hydra") == 2);
    }

    @Test 
    public void test_tickHydraSpawn_initialfrequency() {
        // test hydra spawns at exactly 50 ticks
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Hard");
        for (int i = 0; i < 48; i++) { // runs 48 ticks
            controller.tick(null, Direction.NONE);
        }
        // 49th tick does not spawn anything, no hydras yet
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "hydra") == 0);
        // 50th tick spawns hydra, 1 hydra total
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "hydra") == 1);
    }

    @Test 
    public void test_tickHydraSpawn_hydrasWithinBound() {
        // test hydra all spawns within range
        for (int i = 0; i < 50; i++) { // runs this process 100 times to ensure hydras consistently spawn within bounds
            DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("maze", "Hard");
            for (int j = 0; j < 49; j++) { // runs 49 ticks
                controller.tick(null, Direction.NONE);
            }
            // assert hydra than spawns in 50th tick is within dungeon dimensions, which is (20, 18) for "maze"
            assertTrue(TestHelper.isInSpawnRange(controller.tick(null, Direction.NONE), "hydra", new Position(20, 18)));
        }
    }

    @Test
    public void test_tickBribableEntitySpawn_frequency_standard() {
        // test assassin/mercenary (bribableEntity) spawns every 50 ticks in standard mode
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Standard");
        for (int i = 0; i < 48; i++) { // runs 48 ticks 
            controller.tick(null, Direction.RIGHT);
        }
        // 49th tick does not spawn anything, 0 total
        DungeonResponse tick49 = controller.tick(null, Direction.NONE);
        assertTrue(TestHelper.getNumEntities(tick49, "assassin") + TestHelper.getNumEntities(tick49, "mercenary") == 0);
        // 50th tick spawns ass/merc, 1 total
        DungeonResponse tick50 = controller.tick(null, Direction.NONE);
        assertTrue(TestHelper.getNumEntities(tick50, "assassin") + TestHelper.getNumEntities(tick50, "mercenary") == 1);
    }

    @Test 
    public void test_tickBribableEntitySpawn_frequency_hard() {
        // test assassin/mercenary (bribableEntity) spawns every 40 ticks in hard mode
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("maze", "Hard");
        for (int i = 0; i < 38; i++) { // runs 38 ticks 
            controller.tick(null, Direction.RIGHT);
        }
        // 39th tick does not spawn anything, 0 total
        DungeonResponse tick39 = controller.tick(null, Direction.NONE);
        assertTrue(TestHelper.getNumEntities(tick39, "assassin") + TestHelper.getNumEntities(tick39, "mercenary") == 0);
        // 40th tick spawns ass/merc, 1 total
        DungeonResponse tick40 = controller.tick(null, Direction.NONE);
        assertTrue(TestHelper.getNumEntities(tick40, "assassin") + TestHelper.getNumEntities(tick40, "mercenary") == 1);
    }

    @Test 
    public void test_tickSpawn_BribableEntityLocation_standard() {
        // test BribableEntity spawns at spawn location in standard
        for (int i = 0; i < 50; i++) { // runs this process 100 times to ensure bribable entity consistently spawn within bounds
            DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("maze", "Standard");
            for (int j = 0; j < 49; j++) { // runs 49 ticks
                controller.tick(null, Direction.RIGHT);
            }
            // assert bribable entity that spawns in 50th tick spawns at initial player spawn location (1,1)
            DungeonResponse tick50 = controller.tick(null, Direction.NONE);
            assertTrue(TestHelper.isEntityTypeInPosition(tick50, "assassin", new Position(1,1)) || TestHelper.isEntityTypeInPosition(tick50, "mercenary", new Position(1,1)));
        }
    }
    
    @Test 
    public void test_tickSpawn_BribableEntityLocation_hard() {
        // test BribableEntity spawns at spawn location in hard
        for (int i = 0; i < 50; i++) { // runs this process 100 times to ensure bribable entity consistently spawn within bounds
            DungeonManiaController controller = new DungeonManiaController();
            controller.newGame("maze", "Hard");
            for (int j = 0; j < 39; j++) { // runs 39 ticks
                controller.tick(null, Direction.RIGHT);
            }
            // assert bribable entity that spawns in 40th tick spawns at initial player spawn location (1,1)
            DungeonResponse tick40 = controller.tick(null, Direction.NONE);
            assertTrue(TestHelper.isEntityTypeInPosition(tick40, "assassin", new Position(1,1)) || TestHelper.isEntityTypeInPosition(tick40, "mercenary", new Position(1,1)));
        }
    }

    @Test
    public void test_itemUsed_invalid() {

        // create game from map
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectableSimple", "Standard");

        // collect all items
        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.DOWN);
        }

        assertThrows(InvalidActionException.class, () -> {
            controller.tick("random", Direction.NONE);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.tick("treasure-(0, 9)", Direction.NONE);
        });

        
        assertThrows(IllegalArgumentException.class, () -> {
            controller.tick("key-(0, 7)", Direction.NONE);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.tick("wood-(0, 10)", Direction.NONE);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.tick("arrow-(0, 2)", Direction.NONE);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.tick("sword-(0, 8)", Direction.NONE);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.tick("armour-(0, 1)", Direction.NONE);
        });
        
    }


    @Test 
    public void test_itemUsed_valid() {

         // create game from map
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectableSimple", "Standard");
 
         // collect all items
         for (int i = 0; i < 10; i++) {
             controller.tick(null, Direction.DOWN);
         }
 
 
        assertDoesNotThrow(() -> {
            controller.tick("bomb-(0, 3)", Direction.NONE);
            controller.tick("health_potion-(0, 4)", Direction.NONE);
            controller.tick("invisibility_potion-(0, 6)", Direction.NONE);
            controller.tick("invincibility_potion-(0, 5)", Direction.NONE);
        });

   }


}
