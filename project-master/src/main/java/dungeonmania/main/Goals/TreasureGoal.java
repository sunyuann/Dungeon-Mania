package dungeonmania.main.Goals;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.collectable.Treasure;
import dungeonmania.main.Entity;

public class TreasureGoal implements Goal {
    private Dungeon dungeon;

    public TreasureGoal(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public boolean evaluate() {
        // if all treasure is collected, complete game.
        for (Entity e : dungeon.getEntities()) {
            if (e instanceof Treasure) {
                return false;
            }
        }
        return true;
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public void add(Goal goal) {
    }
}
