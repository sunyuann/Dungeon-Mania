package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import static dungeonmania.TestHelper.getInventoryTypes;
import static dungeonmania.TestHelper.getEntitiesTypes;
import static dungeonmania.TestHelper.assertListAreEqualIgnoringOrder;

public class BuildableTest {
    
    DungeonManiaController controller;
    
    // no need to make a new controller in each test
    @BeforeEach
    void setUp() {
        controller = new DungeonManiaController();
    }

    @Test 
    public void test_valid_oneBow() {
        // get a map that only has 1 wood and three arrows
        controller.newGame("simpleBowMap", "standard");

        // collect 3 arrows
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse b = controller.tick(null, Direction.DOWN);

        // check buildables list is empty
        assertTrue(b.getBuildables().isEmpty());

        // collect 1 wood
        b = controller.tick(null, Direction.DOWN);

        // check buildable list
        // bow should be part of the buildables list now
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), b.getBuildables());
        
        // build the bow
        b = controller.build("bow");
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), getInventoryTypes(b));
    }


    @Test 
    public void test_valid_oneShield_treasure() {
        // get a map that only has 2 wood and one treasure
        controller.newGame("simpleShieldMapTreasure", "Standard");

        // collect 2 wood
        controller.tick(null, Direction.DOWN);
        DungeonResponse b = controller.tick(null, Direction.DOWN);

        // check buildables list is empty
        assertTrue(b.getBuildables().isEmpty());

        // collect 1 treasure
        b = controller.tick(null, Direction.DOWN);

        // check buildable list
        // shield should be part of the buildables list now
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), b.getBuildables());
        
        // build the shield
        b = controller.build("shield");

        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), getInventoryTypes(b));
    }

    @Test
    public void test_valid_oneShield_keyGold() {
        // get a map that only has 2 wood and one treasure
        controller.newGame("simpleShieldMapKeyG", "Standard");

        // collect 2 wood
        controller.tick(null, Direction.DOWN);
        DungeonResponse b = controller.tick(null, Direction.DOWN);

        // check buildables list is empty
        assertTrue(b.getBuildables().isEmpty());

        // collect 1 key
        b = controller.tick(null, Direction.DOWN);

        // check buildable list
        // shield should be part of the buildables list now
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), b.getBuildables());
        
        // build the shield
        b = controller.build("shield");
        
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), getInventoryTypes(b));
    }

    @Test
    public void test_valid_oneShield_keySilver() {
        // get a map that only has 2 wood and one treasure
        controller.newGame("simpleShieldMapKeyS", "Standard");

        // collect 2 wood
        controller.tick(null, Direction.DOWN);
        DungeonResponse b = controller.tick(null, Direction.DOWN);

        // check buildables list is empty
        assertTrue(b.getBuildables().isEmpty());

        // collect 1 key
        b = controller.tick(null, Direction.DOWN);

        // check buildable list
        // shield should be part of the buildables list now
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), b.getBuildables());
        
        // build the shield
        b = controller.build("shield");

        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), getInventoryTypes(b));
    }

    @Test
    public void test_valid_oneShield_key_and_treasure() {

        // get a map that only has 2 wood, one treasure and one key
        controller.newGame("simpleShieldMapKT", "Standard");

        // collect 2 wood
        controller.tick(null, Direction.DOWN);
        DungeonResponse b = controller.tick(null, Direction.DOWN);


        // check buildables list is empty
        assertTrue(b.getBuildables().isEmpty());

        // collect 1 treasure
        b = controller.tick(null, Direction.DOWN);

        // check buildable list
        // shield should be part of the buildables list now
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), b.getBuildables());
        
        // collect 1 key
        b = controller.tick(null, Direction.DOWN);
        
        // build the shield
        b = controller.build("shield");

        // our assumption is that a treasure is used when both a key and treasure are present
        // so in that case
        // inventory should have a key and a shield
        assertListAreEqualIgnoringOrder(Arrays.asList("key_gold","shield"), getInventoryTypes(b));
    }

    @Test
    public void test_valid_oneOneOfEach() {
        // map: 2 wood, 3 arrow, one treasure
        controller.newGame("buildableOneOfEach", "peaceful");

        // collect 2 wood
        controller.tick(null, Direction.DOWN);
        DungeonResponse b = controller.tick(null, Direction.DOWN);

        // check buildables list is empt
        assertTrue(b.getBuildables().isEmpty());

        // collect 2 arrows
        b = controller.tick(null, Direction.DOWN);
        b = controller.tick(null, Direction.DOWN);

        // check buildables list is still empty
        assertTrue(b.getBuildables().isEmpty());

        // collect one more arrow
        b = controller.tick(null, Direction.DOWN);

        // buildables list should now have bow in it
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), b.getBuildables());

        // collect 1 treasure and 1 more wood
        controller.tick(null, Direction.DOWN);
        b = controller.tick(null, Direction.DOWN);

        // buildables list has bow and shield now
        assertListAreEqualIgnoringOrder(Arrays.asList("bow", "shield"), b.getBuildables());

        // collect a wood, treasure and sun_stone
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        b = controller.tick(null, Direction.DOWN);
        
        // buildables list has bow, shield and sceptre now
        assertListAreEqualIgnoringOrder(Arrays.asList("bow", "shield", "sceptre"), b.getBuildables());

        // collect armour
        b = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("bow", "shield", "sceptre", "midnight_armour"), b.getBuildables());

        // build bow
        controller.build("bow");

        // build the shield
        controller.build("shield");

        // build sceptre
        b = controller.build("sceptre");

        // check that sun_stone is retained
        assertListAreEqualIgnoringOrder(Arrays.asList("bow", "shield", "sceptre", "armour","sun_stone"), getInventoryTypes(b));

        // build midnight armour
        b = controller.build("midnight_armour");


        // check all items have been built
        assertListAreEqualIgnoringOrder(Arrays.asList("bow", "shield", "sceptre", "sun_stone", "midnight_armour"), getInventoryTypes(b));
    }

    // These next two tests have same map
    @Test
    public void test_valid_oneShield_and_oneBow_overlap_shield_first() {
        controller.newGame("buildableOverlappingBowAndShield", "Standard"); 

        // collect 3 arrows
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // collect 1 treasure
        DungeonResponse b = controller.tick(null, Direction.DOWN);

        // check buildable is empty
        assertTrue(b.getBuildables().isEmpty());

        // collect one wood
        b = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), b.getBuildables());

        // collect one more wood
        b = controller.tick(null, Direction.DOWN);

        assertListAreEqualIgnoringOrder(Arrays.asList("shield", "bow"), b.getBuildables());

        b = controller.build("shield");
        
        // check buildable is empty
        assertTrue(b.getBuildables().isEmpty());
        
        // check inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("arrow", "arrow", "arrow", "shield"), getInventoryTypes(b));

    }

    @Test
    public void test_valid_oneShield_and_oneBow_overlap_bow_first() {
        controller.newGame("buildableOverlappingBowAndShield", "Standard"); 

        // collect 3 arrows
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // collect 1 treasure
        DungeonResponse b = controller.tick(null, Direction.DOWN);

        // check buildable is empty
        assertTrue(b.getBuildables().isEmpty());

        // collect one wood
        b = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), b.getBuildables());

        // collect one more wood
        b = controller.tick(null, Direction.DOWN);

        assertListAreEqualIgnoringOrder(Arrays.asList("shield", "bow"), b.getBuildables());

        b = controller.build("bow");
        
        // check buildable is empty
        assertTrue(b.getBuildables().isEmpty());

        // check inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("treasure", "wood", "bow"), getInventoryTypes(b));
    }

    @Test 
    public void test_valid_multipleShields() {
        controller.newGame("buildableMultipleShields", "Standard"); 

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

        DungeonResponse dmc = controller.tick(null, Direction.DOWN);

        // check that shield is in buildable list
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), dmc.getBuildables());

        // BUILD FIRST SHIELD
        // build a shield
        dmc = controller.build("shield");

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "wood", "wood", "treasure", "treasure", "treasure", "treasure", "shield"), getInventoryTypes(dmc));
        // check that shield is in buildable list
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), dmc.getBuildables());


        // BUILD SECOND SHIELD
        dmc = controller.build("shield");

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "treasure", "treasure", "treasure", "shield", "shield"), getInventoryTypes(dmc));

        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), dmc.getBuildables());

        // BUILD THIRD SHIELD
        dmc = controller.build("shield");

        assertListAreEqualIgnoringOrder(Arrays.asList("treasure", "treasure", "shield", "shield", "shield"), getInventoryTypes(dmc));

        assertTrue(dmc.getBuildables().isEmpty());

    }

    @Test 
    public void test_valid_multipleBows() {

        controller.newGame("buildableMultipleBows", "Standard"); 

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
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        DungeonResponse dmc = controller.tick(null, Direction.DOWN);

        // check that bow is in buildable list
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), dmc.getBuildables());

        // BUILD FIRST BOW
        // build a bow
        dmc = controller.build("bow");

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "wood", "arrow", "arrow", "arrow", "arrow", "arrow", "arrow", "bow"), getInventoryTypes(dmc));
        // check that bow is in buildable list
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), dmc.getBuildables());


        // BUILD SECOND bow
        dmc = controller.build("bow");

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "wood", "arrow", "arrow", "arrow", "bow", "bow"), getInventoryTypes(dmc));

        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), dmc.getBuildables());

        // BUILD THIRD BOW
        dmc = controller.build("bow");

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "bow", "bow", "bow"), getInventoryTypes(dmc));

        assertTrue(dmc.getBuildables().isEmpty());
    }

    @Test 
    public void test_buildable_illegalArgument() {
        // illegalArgumentExceotion should be thrown if build parameter is anything other than "bow","shield","sceptre","midnight_armour"
        controller.newGame("collectableSimple", "Standard");

        // try to build RANDOM
        assertThrows(IllegalArgumentException.class, () -> {
            controller.build("RANDOM");
        });

        // try to build armour
        assertThrows(IllegalArgumentException.class, () -> {
            controller.build("armour");
        });

        // try to build nothing
        assertThrows(IllegalArgumentException.class, () -> {
            // try to build from the armour
            controller.build(null);
        });

    }

    @Test 
    public void test_buildable_invalidAction() {
        // create a map
        controller.newGame("collectableSimple", "Standard");
        
        // player has no items - has not moved

        assertThrows(InvalidActionException.class, () -> {
            controller.build("shield");
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.build("bow");
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.build("sceptre");
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.build("midnight_armour");
        });

    }


    @Test
    public void test_buildableSceptreWoodKeyGoldAndSunStone() {

        // Test sceptre can be built with a wood, gold key and a sun stone

        controller.newGame("buildableSceptreWood", "peaceful");

        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.UP);
        DungeonResponse d = controller.tick(null, Direction.UP);

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "key_gold", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        // check inventory contains sceptre and sun_stone (sun_stone is retained after use)
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone"), getInventoryTypes(d));



    }

    @Test
    public void test_buildableSceptreWoodKeySilverAndSunStone() {
        // Test sceptre can be built with a wood, silver key and a sun stone
        controller.newGame("buildableSceptreWood", "peaceful");

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "key_silver", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone"), getInventoryTypes(d));

    }

    @Test
    public void test_buildableSceptreWoodTreasureAndSunStone() {

        // Test sceptre can be built with a wood, treasure and a sun stone
        controller.newGame("buildableSceptreWood", "peaceful");

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        DungeonResponse d = controller.tick(null, Direction.RIGHT);

        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "treasure", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone"), getInventoryTypes(d));
    }

    @Test
    public void test_buildableSceptreWoodAndTwoSunStone() {

        // Test sceptre can be built with a wood, two sun stones
        controller.newGame("buildableSceptreWood", "peaceful");
        
        // collect a wood and 1 sun_stone
        controller.tick(null, Direction.LEFT);
        DungeonResponse d = controller.tick(null, Direction.LEFT);

        // check this list is empty because you only have 1 sun_stone
        assertTrue(d.getBuildables().isEmpty());

        // attempting to build sceptre will throw an exception
        assertThrows(InvalidActionException.class, () -> {
            controller.build("sceptre");
        });

        // collect another sun_stone
        d = controller.tick(null, Direction.LEFT);

        // check inventory contains necessary items
        assertListAreEqualIgnoringOrder(Arrays.asList("wood", "sun_stone", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        // check both sun_stones are retained
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "sun_stone"), getInventoryTypes(d));


    }


    @Test
    public void test_buildableSceptreArrowsKeyGoldAndSunStone() {

        // Test sceptre can be built with two arrows, a gold key and a sun stone
        controller.newGame("buildableSceptreArrows", "peaceful");

        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.UP);
        DungeonResponse d = controller.tick(null, Direction.UP);

        assertListAreEqualIgnoringOrder(Arrays.asList("arrow", "arrow", "key_gold", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        // check inventory contains sceptre and sun_stone (sun_stone is retained after use)
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone"), getInventoryTypes(d));

    }


    @Test
    public void test_buildableSceptreArrowsKeySilverAndSunStone() {
        
        // Test sceptre can be built with two arrows, a gold key and a sun stone

        controller.newGame("buildableSceptreArrows", "peaceful");

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        assertListAreEqualIgnoringOrder(Arrays.asList("arrow", "arrow", "key_silver", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        // check inventory contains sceptre and sun_stone (sun_stone is retained after use)
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone"), getInventoryTypes(d));

    }

    @Test
    public void test_buildableSceptreArrowsTreasureAndSunStone() {

        // Test sceptre can be built with two arrows, a treasure and a sun stone

        controller.newGame("buildableSceptreArrows", "peaceful");

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        DungeonResponse d = controller.tick(null, Direction.RIGHT);

        assertListAreEqualIgnoringOrder(Arrays.asList("arrow", "arrow", "treasure", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        // check inventory contains sceptre and sun_stone (sun_stone is retained after use)
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone"), getInventoryTypes(d));

    }

    @Test
    public void test_buildableSceptreArrowsAndTwoSunStone() {

        // Test sceptre can be built with a two arorws and two sun stones
        controller.newGame("buildableSceptreArrows", "peaceful");
        
        // collect two arrows and 1 sun_stone
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        DungeonResponse d = controller.tick(null, Direction.LEFT);

        // check this list is empty because you only have 1 sun_stone
        assertTrue(d.getBuildables().isEmpty());

        // attempting to build sceptre will throw an exception
        assertThrows(InvalidActionException.class, () -> {
            controller.build("sceptre");
        });

        // collect another sun_stone
        d = controller.tick(null, Direction.LEFT);

        // check inventory contains necessary items
        assertListAreEqualIgnoringOrder(Arrays.asList("arrow", "arrow", "sun_stone", "sun_stone"), getInventoryTypes(d));


        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre"), d.getBuildables());

        d = controller.build("sceptre");

        // check both sun_stones are retained
        assertListAreEqualIgnoringOrder(Arrays.asList("sceptre", "sun_stone", "sun_stone"), getInventoryTypes(d));

    }

    @Test
    public void test_buildableMidnightArmourOneArmourOneStone() {

        // test case to make sure midnight armour can be built

        // make map with two armours and one sun stone
        controller.newGame("buildableMidnightArmour", "peaceful");
        
        // player starts at (1, 1)
        // make player move down once to collect armour
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // buildables list should be empty
        assertTrue(d.getBuildables().isEmpty());

        // attempting to build midnight armour will throw an exception since we only have armour
        assertThrows(InvalidActionException.class, () -> {
            controller.build("midnight_armour");
        });

        // make player move down again to collect sun stone
        d = controller.tick(null, Direction.DOWN);

        // check inventory contains necessary items
        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "sun_stone"), getInventoryTypes(d));

        // buildables list should now have midnight armour
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour"), d.getBuildables());

        // call build
        d = controller.build("midnight_armour");

        // check inventory - should have midnight armour and sun stone
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour", "sun_stone"), getInventoryTypes(d));
        
        // check buildables list - should be empty
        assertTrue(d.getBuildables().isEmpty());

        // attempting to build midnight armour will throw an exception because inventory only contains a sun_stone
        assertThrows(InvalidActionException.class, () -> {
            controller.build("midnight_armour");
        });

        // move down again to collect another armour
        d = controller.tick(null, Direction.DOWN);

        // check inventory contains necessary items
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour", "armour", "sun_stone"), getInventoryTypes(d));

        // check buildables list - should have midnight armour again
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour"), d.getBuildables());

        // call build
        d = controller.build("midnight_armour");

        // check inventory - should now have two midnight armours and one sun stone
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour", "midnight_armour", "sun_stone"), getInventoryTypes(d));

    }


    // =
    //   3. create a map with zombie toast spawner; collect all the items. let 15 ticks pass. 
    //   - assert zombie is in the map
    //   - buildables string should not contain midnight armour
    //   - when calling build with midnight armour, assert throws invalid actions exception
    //   */

    @Test
    public void test_buildableMidnightArmourZombiePresentHard() {

        
        // make map with two armours and one sun stone
        controller.newGame("buildableMidnightArmourZombie", "hard");
        
        // player starts at (0, 1)
        // make player move down twice to collect armour and sun_stone
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour"), d.getBuildables());

        // elapse ticks 12 times in no direction
        d = elapseTicks(controller, Direction.NONE, 12);

        // midnight armour still here because we are on the 14th tick in hard mode
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour"), d.getBuildables());
        assertListAreEqualIgnoringOrder(Arrays.asList("player", "zombie_toast_spawner"), getEntitiesTypes(d));
        // 15th tick
        d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("zombie", "player", "zombie_toast_spawner"), getEntitiesTypes(d));

        // midnight armour is now invalid
        assertTrue(d.getBuildables().isEmpty());

        
        // attempting to build midnight armour will throw an exception because dungeon has a zombie
        assertThrows(InvalidActionException.class, () -> {
            controller.build("midnight_armour");
        });
        
    }

    @Test
    public void test_buildableMidnightArmourZombiePresentPeaceful() {

        
        // make map with two armours and one sun stone
        controller.newGame("buildableMidnightArmourZombie", "peaceful");
        
        // player starts at (0, 1)
        // make player move down twice to collect armour and sun_stone
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour"), d.getBuildables());

        // elapse ticks 12 times in no direction
        d = elapseTicks(controller, Direction.NONE, 17);

        // midnight armour still here because we are on the 14th tick in hard mode
        assertListAreEqualIgnoringOrder(Arrays.asList("midnight_armour"), d.getBuildables());
        assertListAreEqualIgnoringOrder(Arrays.asList("player", "zombie_toast_spawner"), getEntitiesTypes(d));
        // 15th tick
        d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("zombie", "player", "zombie_toast_spawner"), getEntitiesTypes(d));

        // midnight armour is now invalid
        assertTrue(d.getBuildables().isEmpty());

        
        // attempting to build midnight armour will throw an exception because dungeon has a zombie
        assertThrows(InvalidActionException.class, () -> {
            controller.build("midnight_armour");
        });
        
    }

    /**
     * helper function to elapse ticks
     */
    // no matter what value of numTicks is given, this method will at least elapse one tick
    public DungeonResponse elapseTicks(DungeonManiaController dmc, Direction direction, int numTicks) {
        // first tick is elapsed here
        DungeonResponse d = dmc.tick(null, direction);

        for (int i = 0; i < numTicks-1; i++) {
            d = dmc.tick(null, direction);
        }

        return d;
    }

    @Test
    public void test_buildableSwordSunStone() {{

        // test sword can be built with two wood and one sun_stone
        // make map with two wood and one sun_stone
        controller.newGame("buildableSwordSunStone", "peaceful");

        // player starts at (1,1) and moves down to collect three items
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), d.getBuildables());

        d = controller.build("shield");

        // check shield and sun_stone is in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("shield", "sun_stone"), getInventoryTypes(d));

        // buildable should be empty
        assertTrue(d.getBuildables().isEmpty());

    }}
}