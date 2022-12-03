package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntitiesTest {
    @Test
    public void test_spider_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a spider at (4, 4)
        controller.newGame("movingtest", "Peaceful");
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(4,3)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(5,3)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(5,4)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(5,5)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(4,5)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(3,5)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(3,4)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(3,3)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(4,3)));
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider", new Position(5,3)));
    }

    @Test
    public void test_spider_movement_blocked() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a spider at (2, 2) with boulders surrounding it
        controller.newGame("spider_blocked", "Peaceful");
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        // after several ticks, spider should be in the same position
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "spider",  new Position(2, 2)));
    }

    @Test
    public void test_zombietoast_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a zombie at (1, 1)
        controller.newGame("movingtest", "Peaceful");
        DungeonResponse currentGame = controller.tick(null, Direction.NONE);
        // checks the entities adjacent to (1,1)
        Map<Direction, String> adjacent = TestHelper.getAdjacentEntityTypes(currentGame, new Position(1,1));
        assertTrue(adjacent.values().contains("zombie"));
    }

    @Test
    public void test_zombietoast_movement_blocked() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a zombie at (6, 6) with walls surrounding it
        controller.newGame("movingtest", "Peaceful");
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        // after several ticks, zombie should be in the same position
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "zombie",  new Position(6, 6)));
    }

    @Test
    public void test_hydra_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a hydra at (1,8)
        controller.newGame("movingtest", "Peaceful");
        DungeonResponse currentGame = controller.tick(null, Direction.NONE);
        // checks the entities adjacent to (1,8)
        Map<Direction, String> adjacent = TestHelper.getAdjacentEntityTypes(currentGame, new Position(1,8));
        assertTrue(adjacent.values().contains("hydra"));
    }

    @Test
    public void test_hydra_movement_blocked() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a zombie at (6, 6) with walls surrounding it
        controller.newGame("movingtest", "Peaceful");
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        // after several ticks, zombie should be in the same position
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "hydra",  new Position(7,2)));
    }

    @Test
    public void test_mercenary_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        // maze map with mercenary at one end (above exit), and player on the other
        // using dijkstra's, optimal shortest path should take 50 moves for the mercenary to be adjacent to the player
        controller.newGame("dijkstras", "Peaceful");
        for (int i = 0; i < 49; i++) {
            controller.tick(null, Direction.NONE);
        }
        // on 50th, mercenary should be below player, at (1,2)
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "mercenary",  new Position(1,2)));
    }

    @Test
    public void test_assassin_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        // same maze map used for mercenary test, assassin is above the mercenary
        // using dijkstra's, optimal shortest path should take 49 moves for the assassin to be adjacent to the player
        controller.newGame("dijkstras", "Peaceful");
        for (int i = 0; i < 48; i++) {
            controller.tick(null, Direction.NONE);
        }
        // on 49th, assassin should be below player, at (1,2)
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "assassin",  new Position(1,2)));
    }

    @Test
    public void test_mercenary_movement_swamp() {
        // map has player at 0,0 and two swamp tiles between a mercenary (all surrounded in walls)
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("swamp_merc", "Standard");
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        // only on this tick wil the mercenary move out of the first tile
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "mercenary", new Position(0,1)));
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        // assert that at this point the mercenary is out of the swamp tiles and battles the player
        assertTrue(TestHelper.getNumEntities(controller.tick(null, Direction.NONE), "mercenary") == 0);
    }
    @Test
    public void test_mercenary_movement_swamp_dijkstras() {
        // map has player at 1,1 and mercenary at 4,1, with two walls between them
        // at 2,2 there is a swamp tile
        // if the player doesnt move, mercenary should choose the path that doesnt have the swamp tile
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("swamp_dijkstra", "Standard");
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.NONE);
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "mercenary", new Position(1,0)));
    }

    @Test
    public void test_mercenary_movement_blocked() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a mercenary at (8,8) with walls around it
        controller.newGame("movingtest", "Peaceful");
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        controller.tick(null, Direction.NONE);
        // after several ticks, mercenary should be in the same position
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.NONE), "mercenary",  new Position(8, 8)));
    }

    @Test
    public void test_mercenary_speedup() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has a mercenary at (3,3) and spider at (1,0)
        controller.newGame("mercnary_speedup", "Standard");
        // after killing the spider, the mercnary should move twice as fast towards the player
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.RIGHT), "mercenary",  new Position(3, 1)));
    }

    @Test
    public void test_mercenary_bribe() {
        DungeonManiaController controller = new DungeonManiaController();
        // create a map with merc 3 tiles away
        controller.newGame("mercBribeValidTwo", "Standard");

        // collects treasure when moves down 0,1
        controller.tick(null, Direction.DOWN);
        controller.interact("mercenary-(0, 3)");

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        Map<Direction, String> adjacent = TestHelper.getAdjacentEntityTypes(controller.tick(null, Direction.DOWN), new Position(0, 5));
        assertTrue(adjacent.values().contains("mercenary_ally"));
    }
}