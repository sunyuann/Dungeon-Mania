package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.BattleEntities;
import dungeonmania.main.entities.moving.Hydra;
import dungeonmania.main.entities.moving.Mercenary;
import dungeonmania.main.entities.moving.Spider;
import dungeonmania.main.entities.moving.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class WBBattleTest {

    @Test
    public void test_battlePeaceful() {
        // entities should not go into battle in peaceful
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryBattleTest", "Peaceful");
        DungeonResponse current = controller.tick(null, Direction.DOWN);
        // map has 6 walls, player and mercenary
        // this number should maintain the same as battle 
        assertEquals(current.getEntities().size(), 8);
    }

    @Test
    public void testWB_battleLost() {
        // player at (1,1). Moving down twice will cause two mercenaries to attack, and eventual death
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("battle_lost", "Standard");
        controller.tick(null, Direction.DOWN);
        Dungeon dungeon = new Dungeon(controller.tick(null, Direction.DOWN));
        assertTrue(dungeon.getPlayer() == null);
    }

    @Test
    public void test_battleWon() {
        // battle with mercenary, assert true if won (player in position after battle, indicating battle won)
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "Standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        assertTrue(TestHelper.isEntityTypeInPosition(controller.tick(null, Direction.DOWN), "player", new Position(2,3)));
    }

    @Test
    public void testWB_playerBattleSpider_health() {
        // test player health after battling spider
        DungeonManiaController controller = new DungeonManiaController();
        Dungeon curr_dung = new Dungeon(controller.newGame("spiderBattleTest", "Standard"));
        for (BattleEntities entity: curr_dung.getBattleEntities()) {
            if (entity instanceof Spider) {
                curr_dung.getPlayer().battle(entity);
            }
        }
        assertTrue(curr_dung.getPlayer().getHealth() == 95);
    }

    @Test
    public void testWB_playerBattleMercenary_health() {
        // test player health after battling mercenary
        DungeonManiaController controller = new DungeonManiaController();
        Dungeon curr_dung = new Dungeon(controller.newGame("mercenaryBattleTest", "Standard"));
        for (BattleEntities entity: curr_dung.getBattleEntities()) {
            if (entity instanceof Mercenary) {
                curr_dung.getPlayer().battle(entity);
            }
        }
        assertTrue(curr_dung.getPlayer().getHealth() == 70);
    }

    @Test
    public void testWB_playerBattleZombie_health() {
        // test player health after battling zombie
        DungeonManiaController controller = new DungeonManiaController();
        Dungeon curr_dung = new Dungeon(controller.newGame("zombieBattleTest", "Standard"));
        for (BattleEntities entity: curr_dung.getBattleEntities()) {
            if (entity instanceof ZombieToast) {
                curr_dung.getPlayer().battle(entity);
            }
        }
        assertTrue(curr_dung.getPlayer().getHealth() == 91);
    }

    @Test
    public void testWB_playerBattleZombie() {
        // test player starts battle with zombie, battle won and spider killed
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("zombieBattleTest", "Standard");
        Dungeon curr_dung = new Dungeon(controller.tick(null, Direction.DOWN));
        assertTrue(curr_dung.getNumEntities("zombie") == 0);
    }

    @Test
    public void testWB_playerBattleMercenary() {
        // test player starts battle with mercenary, battle won and hp not decreased & spider killed
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryBattleTest", "Standard");
        Dungeon curr_dung = new Dungeon(controller.tick(null, Direction.DOWN));
        assertTrue(curr_dung.getNumEntities("mercenary") == 0);
    }

    @Test
    public void testWB_playerBattleSpider() {
        // test player starts battle with spider, battle won and hp not decreased & spider killed
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("spiderBattleTest", "Standard");
        Dungeon curr_dung = new Dungeon(controller.tick(null, Direction.DOWN));
        assertTrue(curr_dung.getNumEntities("spider") == 0);
    }

    @Test
    public void testWB_OneRing_BattleLost() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has two mercenaries below the player, with a one ring above the player
        controller.newGame("battle_lost", "Standard");
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);  
        Dungeon dungeon = new Dungeon(controller.tick(null, Direction.DOWN));
        // player should be revived
        assertTrue(dungeon.getPlayer() != null);
    }

    @Test
    public void testWB_invincible_battle() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has two mercenaries below the player, with an invincible pot to the left
        controller.newGame("battle_potions", "Standard");
        controller.tick(null, Direction.LEFT);
        controller.tick("invincibility_potion-(-1, 0)", Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        Dungeon dungeon = new Dungeon(controller.tick(null, Direction.DOWN));
        // player should not die
        assertTrue(dungeon.getPlayer() != null);
    }

    @Test
    public void test_invisible_battle() {
        DungeonManiaController controller = new DungeonManiaController();
        // map has two mercenaries below the player, with an invisible pot to the right
        controller.newGame("battle_potions", "Standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick("invisibility_potion-(1, 0)", Direction.NONE);
        controller.tick(null, Direction.DOWN);
        DungeonResponse dungeon = controller.tick(null, Direction.DOWN);
        // all the entities should still be alive, as invisibility potion prevents battling
        // includes player, two mercenaries, and the other potion
        assertEquals(dungeon.getEntities().size(), 4);
    }
    
    @Test
    public void test_playerBattleHydra_health() {
        // test player health after battling hydra
        Double min_hydra_health = Double.POSITIVE_INFINITY;
        Double min_player_health = Double.POSITIVE_INFINITY;
        for (int i = 0; i < 50; i++) {
            DungeonManiaController controller = new DungeonManiaController();
            Dungeon curr_dung = new Dungeon(controller.newGame("hydraBattleTest", "Hard"));
            for (BattleEntities entity: curr_dung.getBattleEntities()) {
                if (entity instanceof Hydra) {
                    curr_dung.getPlayer().battle(entity);

                    if (min_hydra_health > entity.getHealth()) {
                        min_hydra_health = entity.getHealth();
                    }
                    if (min_player_health > curr_dung.getPlayer().getHealth()) {
                        min_player_health = curr_dung.getPlayer().getHealth();
                    }
                }
            }
        }
        assertTrue(min_hydra_health <= 0);
        assertTrue(min_player_health <= 0);
    }
}
