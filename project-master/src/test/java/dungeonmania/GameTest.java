package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.TestHelper.getEntitiesInfo;

public class GameTest {
    @Test
    public void test_newGame_invalidName() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> {
            controller.newGame("INVALID", "Standard");
        });
    }

    @Test
    public void test_newGame_invalidGamemode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> {
            controller.newGame("maze", "INVALID");
        });
    } 
    
    @Test
    public void test_newGame_valid() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> {
            DungeonResponse dungeon = controller.newGame("maze", "standard");
            // asserting that the dungeonName is correct
            assertEquals("maze", dungeon.getDungeonName());
            Map<Position, String> entities = getEntitiesInfo(dungeon);
            assertTrue(entities.equals(getEntitiesInfo("maze")));
        });
    }

    @Test
    public void test_saveGame() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "STANDARD");
        DungeonResponse save = controller.saveGame("advanced-" + new Date().getTime());
        assertTrue(controller.allGames().contains(save.getDungeonId()));
    }

    @Test
    public void test_saveTick() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "Standard");
        for (int i = 0; i < 15; i++) { // runs 0th-15th ticks
            controller.tick(null, Direction.NONE);
        }
        DungeonResponse save = controller.saveGame("advanced-" + new Date().getTime());
        controller.loadGame(save.getDungeonId());
        for (int i = 0; i < 14; i++) { // runs 16th-29th ticks
            controller.tick(null, Direction.NONE);
        }
        // assert that in 30th tick, 1 spider spawned
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "spider") == 1);
    }

    @Test
    public void test_saveSpawnPosition() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "Hard");
        for (int i = 0; i < 20; i++) { // runs 0th-20th ticks
            controller.tick(null, Direction.RIGHT);
        }
        DungeonResponse save = controller.saveGame("advanced-" + new Date().getTime());
        controller.loadGame(save.getDungeonId());
        for (int i = 0; i < 19; i++) { // runs 20th-39th ticks
            controller.tick(null, Direction.RIGHT);
        }
        // assert bribable entity that spawns in 40th tick spawns at initial player spawn location (1,1)
        DungeonResponse tick40 = controller.tick(null, Direction.NONE);
        assertTrue(TestHelper.isEntityTypeInPosition(tick40, "assassin", new Position(1,1)) || TestHelper.isEntityTypeInPosition(tick40, "mercenary", new Position(1,1)));
    }

    @Test
    public void test_loadGame_invalidId() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> {
            controller.loadGame("INVALID");
        });
    }

    @Test
    public void test_loadGame_valid() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("boulders", "Standard");
        DungeonResponse save = controller.saveGame("boulders-" + new Date().getTime());
        assertDoesNotThrow(() -> {
            DungeonResponse load = controller.loadGame(save.getDungeonId());
            assertTrue(getEntitiesInfo(load).equals(getEntitiesInfo(save)));
        });
    }

    @Test
    public void test_loadGame_inventory() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced-2", "Standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertTrue(TestHelper.doesInventoryContain(controller.tick(null, Direction.RIGHT), "sword"));
        assertDoesNotThrow(() -> {
            DungeonResponse save = controller.saveGame("advanced-2-" + new Date().getTime());
            DungeonResponse load = controller.loadGame(save.getDungeonId());
            assertTrue(TestHelper.doesInventoryContain(load, "sword"));
        });
    }

    @Test
    public void test_loadGame_buildables() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("buildableOverlappingBowAndShield", "Standard");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse current = controller.tick(null, Direction.DOWN);
        assertTrue(TestHelper.doesBuildableContain(current, "bow"));
        assertTrue(TestHelper.doesBuildableContain(current, "shield"));
        assertDoesNotThrow(() -> {
            DungeonResponse save = controller.saveGame("buildableOverlappingBowAndShield-" + new Date().getTime());
            DungeonResponse load = controller.loadGame(save.getDungeonId());
            assertTrue(TestHelper.doesBuildableContain(load, "bow"));
            assertTrue(TestHelper.doesBuildableContain(load, "shield"));
        });
    }

    @Test
    public void test_loadGame_buildable_item() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("buildableOverlappingBowAndShield", "Standard");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse current = controller.build("bow");
        assertTrue(TestHelper.doesInventoryContain(current, "bow"));
        assertDoesNotThrow(() -> {
            DungeonResponse save = controller.saveGame("buildableOverlappingBowAndShield-" + new Date().getTime());
            DungeonResponse load = controller.loadGame(save.getDungeonId());
            assertTrue(TestHelper.doesInventoryContain(load, "bow"));
        });
    }
}
