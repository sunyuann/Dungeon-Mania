package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.main.Dungeon;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dungeonmania.response.models.ItemResponse;

import static dungeonmania.TestHelper.assertListAreEqualIgnoringOrder;
import static dungeonmania.TestHelper.getInventoryTypes;

public class InteractTest {
    DungeonManiaController controller;
    
    // no need to make a new controller in each test
    @BeforeEach
    void setUp() {
        controller = new DungeonManiaController();
    }

    @Test
    public void test_InvalidEntityIdRandom() {
        // create a map
        controller.newGame("collectableSimple", "Standard");

        // pass in random entity id
        // assertThrows illegal argument
        assertThrows(IllegalArgumentException.class, () -> {
            controller.interact("random");
        });
    }

    @Test
    public void test_InvalidEntityIdEntityExists() {
        // create a map
        controller.newGame("collectableSimple", "standard");

        // pass in entity id that exists
        // assertThrows illegal argument
        assertThrows(IllegalArgumentException.class, () -> {
            controller.interact("wood-(0, 10)");
        });
    }

    @Test
    public void test_validActionBribeOne() {
        // create map where mercenary is 10 tiles away below
        controller.newGame("mercBribeValidOne", "standard");

        // call interact with the mercenaries id
        // should assert throw invalid action
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("mercenary-(0, 10)");
        });
        // 3 ticks where player moves down once each time
        // collects treasure first move down
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);


        // for 3rd tick, check exception is still thrown - not within 2 tiles yet
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("mercenary-(0, 10)");
        });
        // 1 more tick
        controller.tick(null, Direction.DOWN);

        // check that assert does not throw
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 10)");
        });
    }

    @Test
    public void test_validActionBribeTwo() {
        // create a map with merc 3 tiles away
        controller.newGame("mercBribeValidTwo", "Standard");

        // collects treasure when moves down 0,1
        controller.tick(null, Direction.DOWN);


        // assertDoesNotThrow
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 3)");
        });
    }

    /**
     * Test to check that a mercenary can be bribed with a sun stone
     */
    @Test
    public void test_validMercBribeSunStone() {
        // create map where mercenary is 10 tiles away below
        controller.newGame("mercBribeValidSunStone", "standard");

        // call interact with the mercenaries id
        // should assert throw invalid action
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("mercenary-(0, 10)");
        });
        // 3 ticks where player moves down once each time
        // collects sun stone first move down
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);


        // for 3rd tick, check exception is still thrown - not within 2 tiles yet
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("mercenary-(0, 10)");
        });

        // 1 more tick
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // check that sun stone is still in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(d));

        // check that assert does not throw
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 10)");
        });
    }

    /**
     * Test to check that if sun stone and treasure in inventory, calling interact for a merc bribe will use treasure
     */
    @Test
    public void test_validMercBribeSunStoneTreasure() {
        // create map where mercenary is 10 tiles away below
        controller.newGame("mercBribeValidSunStoneTreasure", "Standard");

        // call interact with the mercenaries id
        // should assert throw invalid action
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("mercenary-(0, 10)");
        });
        // 3 ticks where player moves down once each time
        // collects treasure first move down and sun stone second
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        assertListAreEqualIgnoringOrder(Arrays.asList("treasure", "sun_stone"), getInventoryTypes(d));

        // for 3rd tick, check exception is still thrown - not within 2 tiles yet
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("mercenary-(0, 10)");
        });

        // 1 more tick
        controller.tick(null, Direction.DOWN);

        d = controller.interact("mercenary-(0, 10)");

        // check that sun stone is still in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(d));
    }

    /**
      * ASSASSIN BRIBING
      6. player has sun stone, treasure and one ring - bribe should use treasure and one ring - sun stone retained
      */

    /**
     * Test to check that assassin bribing with one ring and treasure works
     */
    @Test
    public void test_validAssassinBribe() {
        controller.newGame("assassinBribeValid", "Standard");

        // collects treasure when moves down 0,1
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // check that treasure is in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("treasure"), getInventoryTypes(d));

        // assert throws error - only have a treasure, no one ring
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("assassin-(0, 5)");
        });

        // collects onering when moves down 0,2
        d = controller.tick(null, Direction.DOWN);

        // assertDoesNotThrow
        assertDoesNotThrow(() -> {
            controller.interact("assassin-(0, 5)");
        });
        
        // tick down again after bribing
        d = controller.tick(null, Direction.DOWN);

        // check that both are removed from inventory after bribing
        assertTrue(getInventoryTypes(d).isEmpty());
    }

    /**
     * Test that error is thrown is we try to bribe assassin with only one ring
     */
    @Test
    public void test_invalidAssassinBribe() {
        controller.newGame("assassinBribeOneRing", "Standard");

        // collects one ring when moves down 0,1
        controller.tick(null, Direction.DOWN);

        // assert throws error - only have a one ring, no treasure
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("assassin-(0, 5)");
        });

    }

    /**
     * Test to check that assassin bribing with one ring and sun stone works
     */
    @Test
    public void test_validAssassinBribeStone() {
        controller.newGame("assassinBribeOneRing", "Standard");

        // collects one ring when moves down 0,1
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // check that one ring is in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("one_ring"), getInventoryTypes(d));

        // assert throws error - only have a one ring, no stone yet
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("assassin-(0, 5)");
        });

        // collects sun stone when moves down 0,2
        d = controller.tick(null, Direction.DOWN);

        // assertDoesNotThrow
        assertDoesNotThrow(() -> {
            controller.interact("assassin-(0, 5)");
        });

        // tick down again after bribing
        d = controller.tick(null, Direction.DOWN);

        // check that one ring is removed after bribing
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(d));

    }

    /**
     * Test that error is thrown is we try to bribe assassin with only sun stone
     */
    @Test
    public void test_invalidAssassinBribeStoneOnly() {
        controller.newGame("assassinBribeStone", "Standard");

        // collectssun stone when moves down 0,1
        controller.tick(null, Direction.DOWN);

        // assert throws error - only have a one ring, no treasure
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("assassin-(0, 5)");
        });

    }

    @Test
    public void test_BribeMultiple() {
        controller.newGame("bribeMultiple", "Standard");

        // collects treasure when moves down 0,1
        controller.tick(null, Direction.DOWN);

        // collects onering when moves down 0,2
        controller.tick(null, Direction.DOWN);

        // assertDoesNotThrow
        assertDoesNotThrow(() -> {
            controller.interact("assassin-(0, 5)");
        });


        DungeonResponse dungeon = controller.tick(null, Direction.DOWN);

        assertTrue(TestHelper.isEntityTypeInPosition(dungeon, "assassin_ally", new Position(0, 2)));

        // assertDoesNotThrow
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 7)");
        });

        dungeon = controller.tick(null, Direction.DOWN);

        assertTrue(TestHelper.isEntityTypeInPosition(dungeon, "assassin_ally", new Position(0, 3)));
        assertTrue(TestHelper.isEntityTypeInPosition(dungeon, "mercenary_ally", new Position(0, 2)));

        // assertDoesNotThrow
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 9)");
        });

        dungeon = controller.tick(null, Direction.DOWN);


        assertTrue(TestHelper.isEntityTypeInPosition(dungeon, "assassin_ally", new Position(0, 4)));
        assertTrue(TestHelper.isEntityTypeInPosition(dungeon, "mercenary_ally", new Position(0, 3)));
        assertTrue(TestHelper.isEntityTypeInPosition(dungeon, "mercenary_ally", new Position(0, 2)));
    }

    @Test
    public void test_invalidActionDestroyingSpawner() {
        // check: doesn't work from 3 tiles away; works from adjacent
        // create a map where spawner is 3 tiles away from player + there is a weapon at (0, 2)
        controller.newGame("testSpawnerInvalid", "Standard");

        // should assert invalid action
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("zombie_toast_spawner-(0, 3)");
        });

        // call tick
        controller.tick(null, Direction.DOWN);

        // should assert invalid action again
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("zombie_toast_spawner-(0, 3)");
        });


        // call tick again - collect weapon
        controller.tick(null, Direction.DOWN);

        // assert should not throw
        assertDoesNotThrow(() -> {
            controller.interact("zombie_toast_spawner-(0, 3)");
        });
    }

    @Test
    public void test_invalidBribeNoTreasure() {
        // create a map where merc is adjacent
        controller.newGame("mercBribeInvalid", "Standard");

        // assertThrows invalid action (no treasure has been collected yet)
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("mercenary-(0, 1)");
        });
    }

    @Test
    public void test_invalidSpawnerNoWeapon() {
        // create a map where player is adjacent to spawner
        controller.newGame("testSpawnerOnly", "Standard");

        // assertThrows invalid action
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("zombie_toast_spawner-(0, 1)");
        });
    }

    @Test
    public void test_validSpawnerSword() {
        // create a map where player sword spawner
        controller.newGame("testSpawnerSword", "Standard");
        
        // tick - collect sword
        controller.tick(null, Direction.DOWN);

        // assertDoesNotThrow for interact
        assertDoesNotThrow(() -> {
            controller.interact("zombie_toast_spawner-(0, 2)");
        });
    }

    @Test
    public void test_validSpawnerBow() {
        // create a map where player sword spawner
        controller.newGame("testSpawnerBow", "Standard");
        
        // tick - collect bow
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse b = controller.build("bow");

        List<String> ourTypes = new ArrayList<>();
        for (ItemResponse item: b.getInventory()) {
            ourTypes.add(item.getType());
        }

        assertListAreEqualIgnoringOrder(Arrays.asList("arrow", "bow", "arrow"), ourTypes);

                
        // assertDoesNotThrow for interact
        assertDoesNotThrow(() -> {
            controller.interact("zombie_toast_spawner-(0, 7)");
        });
    }

    @Test
    public void test_invalidSpawnerShield() {
        // create a map where player shield spawner
        controller.newGame("testSpawnerShield", "Standard");
        
        // tick - collect wood + treasure
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        // build shield
        DungeonResponse b = controller.build("shield");

        List<String> ourTypes = new ArrayList<>();
        for (ItemResponse item: b.getInventory()) {
            ourTypes.add(item.getType());
        }

        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), ourTypes);

        // assertThrows invalid action for interact
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("zombie_toast_spawner-(0, 4)");
        });
    }

    @Test
    public void test_invalidSpawnerArmour() {
        // create a map where player armour spawner
        controller.newGame("testSpawnerAll", "Standard");
        
        // tick - collect armour
        controller.tick(null, Direction.DOWN);

        // assertThrows invalid action for interact
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("zombie_toast_spawner-(0, 12)");
        });
    }

    @Test
    public void test_validSpawnerAllArmoury() {
        // create a map where player armour shield bow sword spawner
        controller.newGame("testSpawnerAll", "Standard");

        // tick - collect all
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.build("bow");
        controller.build("shield");


        // assertDoesNotThrow for interact
        assertDoesNotThrow(() -> {
            controller.interact("zombie_toast_spawner-(0, 12)");
        });

    }

    @Test 
    public void WBtest_mindControlMercenary() {

        // create a map where player armour shield bow sword spawner
        controller.newGame("mercMindControlValid", "Standard");

        // collect items for sceptre - player at position (0,3) and mercenary (0,5) by end
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // check inventory
        DungeonResponse d = controller.build("sceptre");
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "sun_stone"), getInventoryTypes(d));

        // do valid mind control
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 7)");
        });

        // let one tick happen and get the dungeon response
        d = controller.tick(null, Direction.DOWN);

        // check sceptre has been removed
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "sun_stone"), getInventoryTypes(d));

        // check that mercenary is added as an ally
        Dungeon dungeon = new Dungeon(d);
        assertEquals(dungeon.getPlayer().getAllies().size(), 1);

        // update tick 7 times 
        for (int i = 0; i < 7; i++) {
            controller.tick(null, Direction.DOWN);
        }

        // ninth tick
        d = controller.tick(null, Direction.DOWN);

        // check edge case - mind control still happening 
        Dungeon ninthTick = new Dungeon(d);
        assertEquals(ninthTick.getPlayer().getAllies().size(), 1);

        // tenth tick
        d = controller.tick(null, Direction.DOWN);

        // create new dungeon with the dungeonResponse
        Dungeon afterMindControl = new Dungeon(d);

        // check player has no allies (mercenary not an ally anymore)
        assertEquals(afterMindControl.getPlayer().getAllies().size(), 0);

    }

    @Test
    public void WBtest_mindControlAssassin() {

        // test mind control of assassin when we only have sufficient items to mind control
        controller.newGame("assassinMindControlValid", "Standard");

        // collect items for sceptre - player at position (0,3) and assassin (0,5) by end
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        DungeonResponse d = controller.build("sceptre");
        
        // check inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "sun_stone"), getInventoryTypes(d));

        // check mind control can occur
        assertDoesNotThrow(() -> {
            controller.interact("assassin-(0, 7)");
        });

        // check sceptre has been removed
        d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "sun_stone"), getInventoryTypes(d));

        // whitebox - check that assassin is added as an ally
        Dungeon dungeon = new Dungeon(d);
        assertEquals(dungeon.getPlayer().getAllies().size(), 1);

        // update tick 7 times 
        for (int i = 0; i < 7; i++) {
            controller.tick(null, Direction.DOWN);
        }

        // ninth tick
        d = controller.tick(null, Direction.DOWN);

        // check edge case - mind control still happening 
        Dungeon ninthTick = new Dungeon(d);
        assertEquals(ninthTick.getPlayer().getAllies().size(), 1);

        // tenth tick
        d = controller.tick(null, Direction.DOWN);

        // create new dungeon with the dungeonResponse of the tenth tick
        Dungeon afterMindControl = new Dungeon(d);

        // check player has no allies (assassin not an ally anymore)
        assertEquals(afterMindControl.getPlayer().getAllies().size(), 0);

    }

    @Test
    public void WBtest_mindControlSaveTicks() {

        // test mind control of assassin when we only have sufficient items to mind control
        controller.newGame("assassinMindControlValid", "Standard");

        // collect items for sceptre - player at position (0,3) and assassin (0,5) by end
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        DungeonResponse d = controller.build("sceptre");
        
        // check inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "sun_stone"), getInventoryTypes(d));

        // check mind control can occur
        assertDoesNotThrow(() -> {
            controller.interact("assassin-(0, 7)");
        });

        // check sceptre has been removed
        d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "sun_stone"), getInventoryTypes(d));

        // whitebox - check that assassin is added as an ally
        Dungeon dungeon = new Dungeon(d);
        assertEquals(dungeon.getPlayer().getAllies().size(), 1);

        // update tick 7 times 
        for (int i = 0; i < 7; i++) {
            controller.tick(null, Direction.DOWN);
        }

        // ninth tick
        d = controller.tick(null, Direction.DOWN);

        // check edge case - mind control still happening 
        Dungeon ninthTick = new Dungeon(d);
        assertEquals(ninthTick.getPlayer().getAllies().size(), 1);

        d = controller.saveGame("assassinMindControlValid-" + new Date().getTime());
        d = controller.loadGame(d.getDungeonId());

        // tenth tick
        d = controller.tick(null, Direction.DOWN);

        // create new dungeon with the dungeonResponse of the tenth tick
        Dungeon afterMindControl = new Dungeon(d);

        // check player has no allies (assassin not an ally anymore)
        assertEquals(afterMindControl.getPlayer().getAllies().size(), 0);

    }

    @Test
    public void test_mercMindControlPriorityOverBribe() {

        // test that if there are sufficient items to both mind control and bribe, mind control happens first, but bribe can happen after mind control effects are lost
        controller.newGame("mercMindControlThenBribe", "Standard");

        // collect items for sceptre and treasure- player at position (0,4) and mercenary (0,5) by end
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build sceptre and check inventory
        DungeonResponse d = controller.build("sceptre");
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "treasure"), getInventoryTypes(d));
 
        // assertDoesNotThrow for interact
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 9)");
        });
 
        // let one tick happen and get the dungeon response
        d = controller.tick(null, Direction.DOWN);

        // check sceptre has been removed - mind control happened because sceptre removed but treasure remains
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "treasure"), getInventoryTypes(d));

        // check that we cannot interact with the mercenary i.e isInteractable is false
        assertThrows(IllegalArgumentException.class, () -> {
        controller.interact("mercenary-(0, 9)");
        });
 
         // update tick 9 more times - for the tenth tick with mind control
        for (int i = 0; i < 9; i++) {
             controller.tick(null, Direction.DOWN);
        }
         
        // assertDoesNotThrow for interact
        assertDoesNotThrow(() -> {
            controller.interact("mercenary-(0, 9)");
        });

        d = controller.tick(null, Direction.DOWN);

        // check treasure has been removed because mercenary has been bribed now
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(d));

        // let 20 more ticks elapse
        for (int i = 0; i < 20; i++) {
            controller.tick(null, Direction.DOWN);
        }
        
         d = controller.tick(null, Direction.DOWN);
 
         // create new dungeon with the dungeonResponse
         Dungeon afterBribe = new Dungeon(d);
 
         // check player has no allies (mercenary not an ally anymore)
         assertEquals(afterBribe.getPlayer().getAllies().size(), 1);

        // check that we cannot interact with the mercenary (even after 20 ticks because mercenary is permanently ally) i.e isInteractable is false 
        assertThrows(IllegalArgumentException.class, () -> {
            controller.interact("mercenary-(0, 9)");
        });
         
    }

    @Test
    public void test_assassinMindControlPriorityOverBribe() {

        // test that if there are sufficient items to both mind control and bribe, mind control happens first, but bribe can happen after mind control effects are lost

         controller.newGame("assassinMindControlThenBribe", "Standard");

         // collect items for sceptre materials, one_ring and treasure- player at position (0,5) and assassin (0,6) by end
         controller.tick(null, Direction.DOWN);
         controller.tick(null, Direction.DOWN);
         controller.tick(null, Direction.DOWN);
         controller.tick(null, Direction.DOWN);
         controller.tick(null, Direction.DOWN);


         DungeonResponse d = controller.build("sceptre");
         
         // check inventory
         assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "treasure", "one_ring"), getInventoryTypes(d));
 
        // assertDoesNotThrow for interact
        assertDoesNotThrow(() -> {
             controller.interact("assassin-(0, 11)");
        });
 
        // let one tick happen and get the dungeon response
        d = controller.tick(null, Direction.DOWN);
 
        // check sceptre has been removed - mind control happened because sceptre removed but treasure and one_ring remains
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "treasure", "one_ring"), getInventoryTypes(d));

        // check that we cannot interact with the assassin i.e isInteractable is false
        assertThrows(IllegalArgumentException.class, () -> {
            controller.interact("assassin-(0, 11)");
        });
 
        // update tick 9 more times - for the tenth tick with mind control
        for (int i = 0; i < 9; i++) {
             controller.tick(null, Direction.DOWN);
        }
         
        // assertDoesNotThrow for interact
        assertDoesNotThrow(() -> {
            controller.interact("assassin-(0, 11)");
        });

        // check treasure and one_ring has been removed because assassin has been bribed now
        d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getInventoryTypes(d));

        // let 20 more ticks elapse
        for (int i = 0; i < 20; i++) {
            controller.tick(null, Direction.DOWN);
        }
        
        // get a dungeon response and then create a dungeon
        d = controller.tick(null, Direction.DOWN);
        Dungeon afterBribe = new Dungeon(d);
 
        // check player has no allies (assassin not an ally anymore)
        assertEquals(afterBribe.getPlayer().getAllies().size(), 1);

        // check that we cannot interact with the assassin (even after 20 ticks because assassin is permanently ally) i.e isInteractable is false 
        assertThrows(IllegalArgumentException.class, () -> {
            controller.interact("assassin-(0, 11)");
        });
         
    }

    /**
     * test to check that invalidactionexception is thrown when trying to mind control an assassin that is out of range
     */
    @Test
    public void test_invalidMindControlOutOfRange() {
        // create a map where player 2x sun stone wood assassin
        controller.newGame("assassinMindcontrolInvalid", "Standard");

        // collect items for sceptre - player at position (0,3) and assassin (0,5) by end
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        DungeonResponse d = controller.build("sceptre");
        
        // check inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "sun_stone"), getInventoryTypes(d));

        assertThrows(InvalidActionException.class, () -> {
            controller.interact("assassin-(0, 10)");
        });
    }

    // TODO: update bribe tests to show they aren't mind control by doing what u did at the end of mercMindControlThenBribe tests
    // TODO: update bribe tests - check radius - the different directions


}