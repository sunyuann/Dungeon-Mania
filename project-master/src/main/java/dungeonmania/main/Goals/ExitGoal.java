package dungeonmania.main.Goals;

import dungeonmania.main.Dungeon;
import dungeonmania.main.entities.statics.Exit;
import dungeonmania.main.entities.statics.StaticEntities;

public class ExitGoal implements Goal {
    private Dungeon dungeon;

    public ExitGoal(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public boolean evaluate() {
        for (StaticEntities s : dungeon.getStaticEntities()) {
            if (s instanceof Exit) {
                Exit e = (Exit) s;
                if (dungeon.getPlayer().getPosition().equals(e.getPosition())) {
                    // empty string mean game has complete
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Implement Goal interface method
     */
    @Override
    public void add(Goal goal) {
    }
}
