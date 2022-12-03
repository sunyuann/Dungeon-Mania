package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dungeonmania.main.Dungeon;
import dungeonmania.main.Item;
import dungeonmania.main.entities.items.*;
import dungeonmania.main.entities.moving.Assassin;
import dungeonmania.main.entities.moving.Hydra;
import dungeonmania.main.entities.moving.Mercenary;
import dungeonmania.main.entities.moving.Spider;
import dungeonmania.main.entities.moving.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import static dungeonmania.TestHelper.assertListAreEqualIgnoringOrder;
import static dungeonmania.TestHelper.getDungeonInventoryTypes;


/**
 * This file contains whiteBox tests for the collectable items
 */

public class WBItemTest {

    // create an itemlist

    private static double DEFAULT_PLAYER_HEALTH = 100;

    DungeonManiaController controller;

    @BeforeEach
    void setup() {
        controller = new DungeonManiaController();
    }

    @Test
    public void test_health_potion_valid() {

        controller.newGame("itemHealthPotion", "Standard");

        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.DOWN);
        }

        DungeonResponse afterCollection = controller.tick(null, Direction.LEFT);

        Dungeon dungeon = new Dungeon(afterCollection);

        dungeon.getPlayer().setHealth(5);
        SelectableItem selectableItem = null;
        for (Item item: dungeon.getDungeonInventory().getDungeonInventory()) {
            if (item instanceof HealthPotionItem) {
                selectableItem = (SelectableItem) item;
            }
        }

        assert(selectableItem != null);
        selectableItem.select();

        assertEquals(dungeon.getPlayer().getHealth(), DEFAULT_PLAYER_HEALTH);

        assertListAreEqualIgnoringOrder(Arrays.asList("health_potion", "invincibility_potion", "invisibility_potion"), getDungeonInventoryTypes(dungeon));

    }

    @Test
    public void test_health_potion_noChange() {

        controller.newGame("itemHealthPotion", "Standard");

        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.DOWN);
        }

        // player health is already max; no change should happen when they use the health potion
        DungeonResponse afterCollection = controller.tick(null, Direction.LEFT);

        Dungeon dungeon = new Dungeon(afterCollection);

        dungeon.getPlayer().setHealth(DEFAULT_PLAYER_HEALTH);
        SelectableItem selectableItem = null;
        for (Item item: dungeon.getDungeonInventory().getDungeonInventory()) {
            if (item instanceof HealthPotionItem) {
                selectableItem = (SelectableItem) item;
            }
        }

        assert(selectableItem != null);
        selectableItem.select();

        assertEquals(dungeon.getPlayer().getHealth(), DEFAULT_PLAYER_HEALTH);
    }

    @Test
    public void test_invisibility_potion_valid() {

        controller.newGame("itemHealthPotion", "Standard");

        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.DOWN);
        }

        DungeonResponse afterCollection = controller.tick(null, Direction.LEFT);

        Dungeon dungeon = new Dungeon(afterCollection);

        SelectableItem selectableItem = null;
        for (Item item: dungeon.getDungeonInventory().getDungeonInventory()) {
            if (item instanceof InvisibilityPotionItem) {
                selectableItem = (SelectableItem) item;
            }
        }

        assert(selectableItem != null);
        selectableItem.select();

        assertEquals(dungeon.getPlayer().getInvisibleTicks(),10);
    }

    @Test
    public void test_invincibility_potion_valid() {

        controller.newGame("itemHealthPotion", "Standard");

        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.DOWN);
        }

        DungeonResponse afterCollection = controller.tick(null, Direction.LEFT);

        Dungeon dungeon = new Dungeon(afterCollection);

        SelectableItem selectableItem = null;
        for (Item item: dungeon.getDungeonInventory().getDungeonInventory()) {
            if (item instanceof InvincibilityPotionItem) {
                selectableItem = (SelectableItem) item;
            }
        }

        assert(selectableItem != null);
        selectableItem.select();

        assertEquals(dungeon.getPlayer().getInvincibleTicks(),10);
    }

    @Test
    public void test_SwordAttack() {
        // create map with 2 swords
        controller.newGame("twoSwords", "Standard");

        // controller.tick.. move down twice
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);
        // create a new assassin object to battle
        Assassin assassin = new Assassin(dungeon, "1", "assassin", new Position(1, 1), false);
        // create a new hydra object to battle
        Hydra hydra = new Hydra(dungeon, "1", "hydra", new Position(1, 1), false);
        // create a new zombie object to battle
        ZombieToast zombie = new ZombieToast(dungeon, "1", "zombie", new Position(1, 1), false);  
        // create a new mercenary object to battle
        Mercenary mercenary = new Mercenary(dungeon, "1", "mercenary", new Position(1, 1), false);  

        // collected 2 sword, attack = 2; defense = 1
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 1);

        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(zombie), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(mercenary), 2);

    }

    @Test
    public void test_ArmourDefense() {
        // create map with 2 armours
        controller.newGame("twoArmour", "Standard");

        // controller.tick.. move down twice
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);
        // create a new assassin object to battle
        Assassin assassin = new Assassin(dungeon, "1", "assassin", new Position(1, 1), false);
        // create a new hydra object to battle
        Hydra hydra = new Hydra(dungeon, "1", "hydra", new Position(1, 1), false);
        // create a new zombie object to battle
        ZombieToast zombie = new ZombieToast(dungeon, "1", "zombie", new Position(1, 1), false);  
        // create a new mercenary object to battle
        Mercenary mercenary = new Mercenary(dungeon, "1", "mercenary", new Position(1, 1), false); 


        // collected 2 armour, attack = 1; defense = 2
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(zombie), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(mercenary), 1);

        assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);

    }

    @Test
    public void test_BowAttack() {
        // create map with 2 bows
        controller.newGame("twoBow", "Standard");

        // controller.tick.. move down to collect 1 wood + 3 arrows
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build one bow
        controller.build("bow");

        // move down to collect another 1 wood + 3 arrows
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build another bow
        DungeonResponse d = controller.build("bow");

        // collect 4 more arrows
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        d = controller.tick(null, Direction.DOWN);

        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);
        // create a new assassin object to battle
        Assassin assassin = new Assassin(dungeon, "1", "assassin", new Position(1, 1), false);
        // create a new hydra object to battle
        Hydra hydra = new Hydra(dungeon, "1", "hydra", new Position(1, 1), false);
        // create a new zombie object to battle
        ZombieToast zombie = new ZombieToast(dungeon, "1", "zombie", new Position(1, 1), false);  
        // create a new mercenary object to battle
        Mercenary mercenary = new Mercenary(dungeon, "1", "mercenary", new Position(1, 1), false); 

        // collected 2 bows check that attack = 2; defense = 1
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 1);

        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(zombie), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(mercenary), 2);
    }

    @Test
    public void test_ShieldDefense() {
        // create map with 2 shields
        controller.newGame("twoShield", "Standard");

        // controller.tick.. move down to collect 2 wood + 1 treasure
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build one shield
        controller.build("shield");

        // move down to collect another 2 wood + 1 treasure
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build another shield
        DungeonResponse d = controller.build("shield");

        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);
        // create a new assassin object to battle
        Assassin assassin = new Assassin(dungeon, "1", "assassin", new Position(1, 1), false);
        // create a new hydra object to battle
        Hydra hydra = new Hydra(dungeon, "1", "hydra", new Position(1, 1), false);
        // create a new zombie object to battle
        ZombieToast zombie = new ZombieToast(dungeon, "1", "zombie", new Position(1, 1), false);  
        // create a new mercenary object to battle
        Mercenary mercenary = new Mercenary(dungeon, "1", "mercenary", new Position(1, 1), false); 

        // collected 2 shields, ceck that attack = 1; defense = 2
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(zombie), 1);
        assertEquals(dungeon.getDungeonInventory().battleAttack(mercenary), 1);

        assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);
    }

    @Test
    public void test_durabilitySword() {
        // test the case where durability == 0
        controller.newGame("twoSwords", "Standard");

        // controller.tick.. move down twice - collect both swords
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        Dungeon dungeon = new Dungeon(d);

        // check that two swords in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sword", "sword"), getDungeonInventoryTypes(dungeon));

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);

        // use sword 20 times - durability will become 0 for one sword
        // then, check that only one sword is in the inventory
        for (int i = 0; i < 20; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        }

        assertListAreEqualIgnoringOrder(Arrays.asList("sword"), getDungeonInventoryTypes(dungeon));

        // use sword 19 times 
        for (int i = 0; i < 19; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        }

        // check sword still exists
        assertListAreEqualIgnoringOrder(Arrays.asList("sword"), getDungeonInventoryTypes(dungeon));


        //  20th use of the second sword
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);

        // check that no swords remain
        assertTrue(getDungeonInventoryTypes(dungeon).isEmpty());

        // check that attack is now 1 because no attack weapons are in inventory
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 1);


    }

    @Test
    public void test_durabilityArmour() {
        // test the case where durability == 0
        controller.newGame("twoArmour", "Standard");

        // controller.tick.. move down twice and collect two armours
        controller.tick(null, Direction.DOWN);
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // durability - automatically should equal 25 now (sword not used yet)
        Dungeon dungeon = new Dungeon(d);

        assertListAreEqualIgnoringOrder(Arrays.asList("armour", "armour"), getDungeonInventoryTypes(dungeon));

        // loop call use 39 times
        for (int i = 0; i < 39; i++) {
            assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);
        }

        // check one armour still present
        assertListAreEqualIgnoringOrder(Arrays.asList("armour"), getDungeonInventoryTypes(dungeon));

        // 40th hit
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);

        // armours have no been used and should be removed from inventory
        assertTrue(getDungeonInventoryTypes(dungeon).isEmpty());

        // check defense is 1 now because no more armour items
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 1);


    }

    @Test
    public void test_durabilityBow() {
        // test the case where durability == 0
        controller.newGame("bow25Arrows", "Standard");

        // controller.tick.. move down to collect 1 wood + 28 arrows
        for (int i = 0; i < 28; i++) {
            controller.tick(null, Direction.DOWN);
        }

        // build one bow
        DungeonResponse d = controller.build("bow");

        // durability - automatically should equal 25 now (sword not used yet)
        Dungeon dungeon = new Dungeon(d);

        // check bow is built
        assertTrue(getDungeonInventoryTypes(dungeon).contains("bow"));

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);

        // loop call use 9 times 
        for (int i = 0; i < 9; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        }

        // check bow still exists
        assertTrue(getDungeonInventoryTypes(dungeon).contains("bow"));

        // 10th hit
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);

        // check that there is no more bow
        assertTrue(!getDungeonInventoryTypes(dungeon).contains("bow"));
        
        // check that attack is just 1 
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 1);

    }

    @Test
    public void test_durabilityBowNoArrow() {

        // test the case where bows durability does not decrease after many battles because there are no bows (so it is never used)
        controller.newGame("bow25Arrows", "Standard");

        // controller.tick.. move down to collect 1 wood + 3 arrows ONLY
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build one bow - we now have now arrows
        DungeonResponse d = controller.build("bow");

        // durability - automatically should equal 25 now (sword not used yet)
        Dungeon dungeon = new Dungeon(d);

        // check the bow has been built
        assertTrue(getDungeonInventoryTypes(dungeon).contains("bow"));

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);

        // we have used the bow more than 20 times and we should have an attack of 1 because we had no arrows to use with it each time
        for (int i = 0; i < 25; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 1);
        }

        // bow should still exist in the inventory since we have no arrows to use it with
        assertListAreEqualIgnoringOrder(Arrays.asList("bow"), getDungeonInventoryTypes(dungeon));
    }

    @Test
    public void test_durabilityShield() {
        // test the case where durability == 0
        controller.newGame("twoShield", "Standard");

        // controller.tick.. move down to collect 2 wood + 1 treasure
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build one shield
        DungeonResponse d = controller.build("shield");

        Dungeon dungeon = new Dungeon(d);

        /// check that shield exists - durability is 20
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), getDungeonInventoryTypes(dungeon));


        // edge case - check exists after 9 hits
        for (int i = 0; i < 9; i++) {
            assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);
        }

        // check shield still exists
        assertListAreEqualIgnoringOrder(Arrays.asList("shield"), getDungeonInventoryTypes(dungeon));

        // tenth hit
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);

        // check that shield has been removed
        assertTrue(dungeon.getDungeonInventory().getDungeonInventory().isEmpty());

        // check that defense is 1 because there are no more defense items
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 1);


    }

    /**
     * Test anduril attack on all enemies except assassin and hydra
     */
    @Test
    public void test_andurilAttack() {
        controller.newGame("anduril", "standard");

        // controller.tick.. move down once to collect anduril
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // durability - automatically should equal 30 now
        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);
        // create a new zombie object to battle
        ZombieToast zombie = new ZombieToast(dungeon, "1", "zombie", new Position(1, 1), false);  
        // create a new mercenary object to battle
        Mercenary mercenary = new Mercenary(dungeon, "1", "mercenary", new Position(1, 1), false); 

        assertEquals(dungeon.getDungeonInventory().battleDefense(), 1);

        assertEquals(dungeon.getDungeonInventory().battleAttack(zombie), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(mercenary), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
    }

    /**
     * anduril battle attack should be 6 insteda of 2 with assassin
     */
    @Test
    public void test_durabilityAndurilAssassin() {
        // test the case where durability == 0
        controller.newGame("anduril", "standard");

        // controller.tick.. move down once to collect anduril
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // durability - automatically should equal 25 now (anduril not used yet)
        Dungeon dungeon = new Dungeon(d);

        // create a new assassin object to battle
        Assassin assassin = new Assassin(dungeon, "1", "assassin", new Position(1, 1), false);

        // loop call use 24 times - durability will become 1

        for (int i = 0; i < 24; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 6);
        }

        // check that anduril is still in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("anduril"), getDungeonInventoryTypes(dungeon));

        // use anduril again - one last time
        assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 6);

        // inventory should have nothing left in it
        assertTrue(getDungeonInventoryTypes(dungeon).isEmpty());

        // attack power should now = 1 since there are no attack weapons left
        assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 1);
    }

    /**
     * anduril battle attack should be 6 insteda of 2 with hydra
     */
    @Test
    public void test_durabilityAndurilHydra() {
        // test the case where durability == 0
        controller.newGame("anduril", "standard");

        // controller.tick.. move down once to collect anduril
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // durability - automatically should equal 25 now (anduril not used yet)
        Dungeon dungeon = new Dungeon(d);

        // create a new assassin object to battle
        Hydra hydra = new Hydra(dungeon, "1", "hydra", new Position(1, 1), false);

        // loop call use 24 times - durability will become 1

        for (int i = 0; i < 24; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 6);
        }

        // check that anduril is still in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("anduril"), getDungeonInventoryTypes(dungeon));

        // use anduril again - one last time
        assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 6);

        // inventory should have nothing left in it
        assertTrue(getDungeonInventoryTypes(dungeon).isEmpty());

        // attack power should now = 1 since there are no attack weapons left
        assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 1);
    }

    @Test
    public void test_andurilDurability() {
        // test the case where durability == 0
        controller.newGame("anduril", "standard");

        // controller.tick.. move down once to collect anduril
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        // durability - automatically should equal 30 now (anduril not used yet)
        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);

        // loop call use 24 times - durability will become 1

        for (int i = 0; i < 24; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        }

        // check that anduril is still in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("anduril"), getDungeonInventoryTypes(dungeon));

        // use anduril again - one last time
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);

        // inventory should have nothing left in it
        assertTrue(getDungeonInventoryTypes(dungeon).isEmpty());

        // attack power should now = 1 since there are no attack weapons left
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 1);
    }

    @Test
    public void test_midnightArmourAttackAndDefense() {
        // that it midnight armour attack of 2 (no matter which entity is passerd in) and defense of 2 as well

        // first build the midnight armour
        controller.newGame("midnightArmour", "standard");

        // controller.tick.. move down twice to collect an armour and a sun stone
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build one midnight)armour
        DungeonResponse d = controller.build("midnight_armour");

        // durability - automatically should equal 30 now
        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);
        // create a new assassin object to battle
        Assassin assassin = new Assassin(dungeon, "1", "assassin", new Position(1, 1), false);
        // create a new hydra object to battle
        Hydra hydra = new Hydra(dungeon, "1", "hydra", new Position(1, 1), false);
        // create a new zombie object to battle
        ZombieToast zombie = new ZombieToast(dungeon, "1", "zombie", new Position(1, 1), false);  
        // create a new mercenary object to battle
        Mercenary mercenary = new Mercenary(dungeon, "1", "mercenary", new Position(1, 1), false); 

        assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);

        assertEquals(dungeon.getDungeonInventory().battleAttack(assassin), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(hydra), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(zombie), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(mercenary), 2);
        assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
    }

    @Test
    public void test_midnightArmourDurability() {
        // test the case where durability == 0
        controller.newGame("midnightArmour", "standard");

        // controller.tick.. move down twice to collect an armour and a sun stone
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build one midnight)armour
        DungeonResponse d = controller.build("midnight_armour");

        // durability - automatically should equal 30 now
        Dungeon dungeon = new Dungeon(d);

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);

        // loop call attack 15 times
        for (int i = 0; i < 15; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        }

        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "midnight_armour"), getDungeonInventoryTypes(dungeon));

        // loop call defense 14 times - durability will become 1
        for (int i = 0; i < 14; i++) {
            assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);
        }

        // check that midnight armour still exists
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone", "midnight_armour"), getDungeonInventoryTypes(dungeon));

        // 30th use of midnight armour
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);

        // midnight armour should be removed from inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getDungeonInventoryTypes(dungeon));

        // defense power should only be 1 because there are no more defense weapons in inventory
        assertEquals(dungeon.getDungeonInventory().battleDefense(), 1);

    }

    /**
     * test to check that weapons are used individually and in the order of their collection
     */
    @Test
    public void test_allWeaponsUse() {
        controller.newGame("allWeapons", "standard");

        // collect armour
        controller.tick(null, Direction.DOWN);

        
        // collect sword
        controller.tick(null, Direction.DOWN);


        // collect anduril
        controller.tick(null, Direction.DOWN);


        // collect necessary items for bow
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);


        // build bow
        controller.build("bow");

        // collect necessary items for shield
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build shield
        controller.build("shield");

        // collect necessary items for midnight armour
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // build midnight armour
        controller.build("midnight_armour");

        // collect more arrows for bow to use (tick down 10 times in a loop to have sufficient bows)
        for (int i = 0; i < 9; i++) {
            controller.tick(null, Direction.DOWN);
        }
        DungeonResponse d = controller.tick(null, Direction.DOWN);

        Dungeon dungeon = new Dungeon(d);
        
        // check that those 6 weapons + sun stone are in the inventory
        assertTrue(getDungeonInventoryTypes(dungeon).containsAll(Arrays.asList("armour", "sword", "anduril", "bow", "shield", "sun_stone", "midnight_armour")));

        // create a new spider object to battle
        Spider spider = new Spider(dungeon, "1", "spider", new Position(1, 1), false);

        // loop call attack 60 times - using armour
        for (int i = 0; i < 60; i++) {
            assertEquals(dungeon.getDungeonInventory().battleDefense(), 2);
        }

        // check defense weapons are no longer in inventory
        assertFalse(getDungeonInventoryTypes(dungeon).contains("armour"));
        assertFalse(getDungeonInventoryTypes(dungeon).contains("shield"));
        assertFalse(getDungeonInventoryTypes(dungeon).contains("midnight_armour"));

      // check that those 5 weapons + sun stone are in the inventory
        assertTrue(getDungeonInventoryTypes(dungeon).containsAll(Arrays.asList("sword", "anduril", "bow", "sun_stone")));

        // loop call attack 55 times - using sword
        for (int i = 0; i < 55; i++) {
            assertEquals(dungeon.getDungeonInventory().battleAttack(spider), 2);
        }
        
        // check that only sun stone is in the inventory
        assertListAreEqualIgnoringOrder(Arrays.asList("sun_stone"), getDungeonInventoryTypes(dungeon));

    }
}
