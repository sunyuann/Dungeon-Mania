package dungeonmania.main.Goals;

import dungeonmania.main.Dungeon;
import dungeonmania.main.Entity;
import dungeonmania.main.entities.statics.ZombieToastSpawner;

public class EnemiesGoal implements Goal {
    private Dungeon dungeon;

    public EnemiesGoal(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public boolean evaluate() {
        // if all enemies and spawners are destroyed, complete game.
        for (Entity e : dungeon.getEntities()) {
            if (e instanceof ZombieToastSpawner)
                return false;
        }
        if (!dungeon.getMovingEntities().isEmpty()) {
            return false;
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
