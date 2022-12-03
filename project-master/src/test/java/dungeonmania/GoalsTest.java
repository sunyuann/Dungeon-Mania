package dungeonmania;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import dungeonmania.util.Direction;

public class GoalsTest {
    // Test if exits complete the puzzle
    @Test
    public void testGoToExit() {
        // Move character into exit, goal should complete.
        DungeonManiaController controller = new DungeonManiaController();
        // Character at 1,1. Exit at 1,3.
        controller.newGame("exitgoal", "Standard");
        controller.tick(null, Direction.RIGHT);
        // Show game is complete by returning empty string of goals
        assertEquals("", controller.tick(null, Direction.RIGHT).getGoals());
    }

    // Test destroy all enemies and spawners goal
    @Test
    public void testAllEnemiesAndSpawnersGoal() {
        // kill a spider and a spawner, goal should complete.
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemiesgoal", "Standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.interact("zombie_toast_spawner-(5, 1)");
        assertEquals("", controller.tick(null, Direction.RIGHT).getGoals());
    }

    // Test all boulders on floor switches goal
    @Test
    public void testAllBouldersOnSwitchesGoal() {
        // move two boulders onto switches, goal should complete.
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bouldergoal", "Standard");
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.DOWN);
        assertEquals("", controller.tick(null, Direction.DOWN).getGoals());
    }

    // Test collecting all treasure goal
    @Test
    public void testAllTresureGoal() {
        // Collect two pieces of treasure, goal should complete.
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("treasuregoal", "Peaceful");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        assertEquals("", controller.tick(null, Direction.DOWN).getGoals());
    }

    @Test
    // Test OR goal
    public void testBoulderOrTreasure() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("boulderortreasure", "Peaceful");
        assertEquals("", controller.tick(null, Direction.DOWN).getGoals());
    }

    @Test
    // Test AND goal
    public void testExitAndTreasure() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("exitandtreasure", "Peaceful");
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.DOWN);
        assertEquals("", controller.tick(null, Direction.DOWN).getGoals());
    }

    @Test
    // Test trying to do exit first in conjunction goals.
    public void testExitFirst() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("exitandtreasure", "Peaceful");
        assertNotEquals("", controller.tick(null, Direction.DOWN).getGoals());
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.DOWN);
        assertEquals("", controller.tick(null, Direction.DOWN).getGoals());
    }

    @Test
    public void testNestedAndOr() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("nestedgoal", "Peaceful");
        // pushed boulder on switch and then go to exit
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.LEFT);
        assertEquals("", controller.tick(null, Direction.UP).getGoals());
    }

    @Test
    public void testTwoAnds() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("twoandgoal", "Peaceful");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.LEFT);
        assertEquals("", controller.tick(null, Direction.LEFT).getGoals());
    }

    @Test
    public void testTwoOrs() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("twoorgoal", "Peaceful");
        assertEquals("", controller.tick(null, Direction.RIGHT).getGoals());
    }

    @Test
    public void testEnemiesButNoEnemies() {
        DungeonManiaController controller = new DungeonManiaController();
        assertEquals("", controller.newGame("enemiesgoalbutnoenemy", "Peaceful").getGoals());
    }
}
